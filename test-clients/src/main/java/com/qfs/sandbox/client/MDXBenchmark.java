/*
 * (C) Quartet FS 2011
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.qfs.sandbox.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.activeviam.training.cfg.security.SecurityConfig;
import com.quartetfs.biz.pivot.context.impl.QueriesTimeLimit;
import com.quartetfs.biz.pivot.dto.CellSetDTO;
import com.quartetfs.biz.pivot.query.IMDXQuery;
import com.quartetfs.biz.pivot.query.impl.MDXQuery;
import com.quartetfs.biz.pivot.server.http.impl.HTTPServiceFactory;
import com.quartetfs.biz.pivot.webservices.IQueriesService;

/**
 *
 * Simple MDX benchmark.
 *
 * <p>
 * The benchmark uses Spring HTTP Invoker mechanism to
 * connect to the ActivePivot Sandbox queries service,
 * and to serialize the results.
 *
 * @author Quartet Financial Systems
 *
 */
public class MDXBenchmark {

	/** Base url for Spring HTTP Remoting Services */
	static final String BASE_URL = "http://localhost:9090/remoting/";

	/** User name */
	static final String USER = SecurityConfig.USER_ADMIN;

	/** User password */
	static final String PASSWORD = SecurityConfig.PASSWORD_ADMIN;

	/** The number of ns in one ms. */
	static final int NANO_IN_MILLI = 1_000_000;

	/** Number of query iterations (per client) */
	protected int iterations;

	/** Number of concurrent clients */
	protected int clientCount;

	/** List of MDX queries */
	protected List<String> queries;

	/** Execute queries in the list sequentially or randomly? */
	protected final boolean random;

	public MDXBenchmark(List<String> queries, int iterations, int clientCount) {
		this(queries, iterations, clientCount, true);
	}

	public MDXBenchmark(List<String> queries, int iterations, int clientCount, boolean random) {
		this.iterations = iterations;
		this.clientCount = clientCount;
		this.queries = queries;
		this.random = random;
	}

	public void run() {
		List<MDXClient> clients = new ArrayList<>(clientCount);
		for(int i = 0; i < clientCount; i++) {
			clients.add(new MDXClient());
		}
		for(int i = 0; i < clientCount; i++) {
			clients.get(i).start();
		}
		for(int i = 0; i < clientCount; i++) {
			try {
				clients.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		double[] avg_query = new double[queries.size()];
		double[] std_query = new double[queries.size()];
		for (int i = 0; i < clientCount; i++) {
			final MDXClient c = clients.get(i);

			for (int j = 0; j < clients.get(i).getQueryCount(); j++) {
				System.out.println(
						String.format(
								"Client " + i + " took an average of %.2f" + "ms with a standard dev of %.2f" + "ms.",
								c.average(j) / NANO_IN_MILLI,
								c.std(j) / NANO_IN_MILLI) + " for query #" + j);
				avg_query[j] += c.average(j);
				std_query[j] += c.std(j);
			}

		}
		for (int i = 0; i < queries.size(); i++) {
			System.out.println(
					String.format(
							"Average time for query #" + i + " : %.2f" + " ms with a standard dev of %.2f ms",
							avg_query[i] / clients.size() / NANO_IN_MILLI,
							std_query[i] / clients.size() / NANO_IN_MILLI));
		}
	}

	/** Counts the instantiated MDX clients */
	static final AtomicInteger COUNTER = new AtomicInteger(0);

	/**
	 * Single MDX Client, designed to run
	 * in its own thread, over its own
	 * HTTP connection.
	 */
	public class MDXClient extends Thread {

		/** Query error count */
		int errorCount = 0;

		/** Recorded executions */
		List<QueryExecution> executions[];

		/** Random generator */
		final Random rand;

		@SuppressWarnings("javadoc")
		public MDXClient() {
			super("mdx-client-" + COUNTER.getAndIncrement());
			this.rand = new Random();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			// Setup Spring HTTP Invoker for the queries service
			final HTTPServiceFactory<IQueriesService> factory = new HTTPServiceFactory<>();
			factory.setAddress(BASE_URL + "Queries");
			factory.setServiceClass(IQueriesService.class);
			factory.setUsername(USER);
			factory.setPassword(PASSWORD);
			factory.setTimeout(0);
			IQueriesService queriesService = factory.create();

			int queryCount = queries.size();
			this.executions = new ArrayList[queryCount];
			for (int i = 0; i < queryCount; i++) {
				this.executions[i] = new ArrayList<>();
			}

			for(int i = 0; i < iterations; i++) {
				try {
					int queryIndex = selectQuery(queryCount, i);
					String mdx = queries.get(queryIndex);
					IMDXQuery query = new MDXQuery(mdx, Collections.singletonList(QueriesTimeLimit.of(3600, TimeUnit.SECONDS)));
					long before = System.nanoTime();
					CellSetDTO cellSet = queriesService.execute(query);
					long elapsed = System.nanoTime() - before;

					int cellSetSize = cellSet.getCells() == null ? 0 : cellSet.getCells().size();
					QueryExecution execution = new QueryExecution(getName(), i, queryIndex, cellSetSize, elapsed);
					executions[queryIndex].add(execution);
					System.out.println(execution);

				} catch(Exception e) {
					errorCount++;
					e.printStackTrace();
				}
			}
		}

		int selectQuery(final int queryCount, final int iteration) {
			if(random) {
				return rand.nextInt(queryCount);
			} else {
				return iteration % queryCount;
			}
		}

		int getQueryCount() {
			return queries.size();
		}

		/** Query execution entry */
		class QueryExecution {

			final String name;

			final int iteration;

			final int queryIndex;

			final int cellCount;

			final long elapsed;

			public QueryExecution(String name, int iteration, int queryIndex, int cellCount, long elapsed) {
				this.name = name;
				this.iteration = iteration;
				this.queryIndex = queryIndex;
				this.cellCount = cellCount;
				this.elapsed = elapsed;
			}

			String getQuery() {
				return queries.get(queryIndex);
			}

			@Override
			public String toString() {
				return name
						+ ", iteration-"
						+ iteration
						+ ", query-"
						+ queryIndex
						+ ", result size = "
						+ cellCount
						+ ", elapsed = "
						+ (elapsed / NANO_IN_MILLI)
						+ "ms";
			}
		}

		@SuppressWarnings("javadoc")
		public long totalTime(int queryIndex) {
			long time = 0;
			final int nbrExecs = executions[queryIndex].size();
			for (int i = nbrExecs / 10; i < nbrExecs; ++i) {
				final QueryExecution qe = executions[queryIndex].get(i);
				time += qe.elapsed;
			}
			return time;
		}

		@SuppressWarnings("javadoc")
		public double average(int queryIndex) {
			// Skip the first 10%
			final int nbrExecs = executions[queryIndex].size() - (executions[queryIndex].size() / 10);
			if (nbrExecs != 0) {
				return (totalTime(queryIndex) / nbrExecs);
			} else {
				return 0;
			}
		}

		@SuppressWarnings("javadoc")
		public double std(int queryIndex) {
			final double avg = average(queryIndex);
			final int nbrRmv = executions[queryIndex].size() / 10;
			final int nbrExecs = executions[queryIndex].size() - nbrRmv;

			double total = 0;
			for (int i = nbrRmv; i < executions[queryIndex].size(); ++i) {
				final QueryExecution qe = executions[queryIndex].get(i);
				total += Math.pow(qe.elapsed - avg, 2);
			}
			if (nbrExecs != 0) {
				return Math.sqrt((total / nbrExecs));
			} else {
				return 0;
			}
		}

	}

	/**
	 * Manual launcher.
	 * A collections of queries is populated, and we launch
	 * an instance of the MDX benchmark that will concurrently execute those
	 * queries from several clients in parallel. Queries in the list can
	 * be executed sequentially, or randomly.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

	//	System.out.println(System.getProperty("java.library.path"));

		int iterations = 10;
		int clientCount = 10;

		List<String> queries = new ArrayList<>();
		
		// Need to add some queries here!
		queries.add("SELECT  {    [Measures].[contributors.COUNT],    [Measures].[Delta.SUM]  } ON COLUMNS,  NON EMPTY [Risk Currency].[Risk Currency].[Risk Currency].Members ON ROWS  FROM (    SELECT    {      [Risk Currency].[Risk Currency].[ALL].[AllMember].[AUD],      [Risk Currency].[Risk Currency].[ALL].[AllMember].[CAD],      [Risk Currency].[Risk Currency].[ALL].[AllMember].[GBP],      [Risk Currency].[Risk Currency].[ALL].[AllMember].[USD]    } ON COLUMNS    FROM [RiskCube]  )");
		MDXBenchmark benchmark = new MDXBenchmark(queries, iterations, clientCount, false);

		long before = System.currentTimeMillis();
		benchmark.run();
		long elapsed = System.currentTimeMillis() - before;

		System.out.println(clientCount + " concurrent clients each executed " + iterations + " queries, in " + elapsed/1000 + " seconds.");
	}

}
