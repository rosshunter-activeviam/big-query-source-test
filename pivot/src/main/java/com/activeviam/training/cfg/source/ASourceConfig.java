/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.source;

import com.qfs.gui.impl.JungSchemaPrinter;
import com.qfs.msg.IMessageChannel;
import com.qfs.msg.csv.*;
import com.qfs.msg.csv.filesystem.impl.FileSystemCSVTopicFactory;
import com.qfs.msg.csv.impl.CSVParserConfiguration;
import com.qfs.msg.csv.impl.CSVSource;
import com.qfs.source.impl.CSVMessageChannelFactory;
import com.qfs.store.IDatastore;
import com.qfs.store.IDatastoreSchemaMetadata;
import com.qfs.store.impl.SchemaPrinter;
import com.qfs.store.transaction.ITransactionManager;
import com.qfs.util.timing.impl.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.activeviam.training.constants.StoreAndFieldConstants.*;

/**
 * Spring configuration for data sources
 * 
 * @author ActiveViam
 *
 */
@Configuration
public abstract class ASourceConfig<I> {

    protected static final Logger LOGGER = Logger.getLogger(ASourceConfig.class.getSimpleName());

    /*  Topics */
    public static final String TRADES_TOPIC = "TradesTopic";
    public static final String STATIC_BOOK_TOPIC = "StaticBookTopic";
    public static final String RISK_BUCKET_TOPIC = "RiskBucketTopic";
    public static final String MD_BUCKET_SOD_TOPIC = "MDTopicSOD";
    public static final String MD_BUCKET_INTRADAY_TOPIC = "MDTopicIntraday";
    public static final String FX_TOPIC = "FXTopic";
    public static final String BUCKET_TOPIC = "BucketTopic";

    @Autowired
    protected Environment env;

    @Autowired
    protected IDatastore datastore;

    /*
     * **************************** CSV Source **********************************
     * This is an example of CSV source configuration, which you could modify to fit your needs
     * **************************************************************************
     */


	/**
	 * Topic factory bean. Allows to create CSV topics and watch changes to directories. Autocloseable.
	 * @return the topic factory
	 */
	@Bean
	public FileSystemCSVTopicFactory csvTopicFactory() {
		return new FileSystemCSVTopicFactory(false);
	}

	/**
	 * Creates the {@link CSVSource} responsible for loading the initial data in
	 * the datastore from csv files.
	 *
	 * @return the {@link CSVSource}
	 * @throws IOException if the source file can't be open or the header is missing
	 */
    
	/**
	 * @param topic        The name of the topic to create
	 * @param file         The relative path of the file to watch
	 * @param parserConfig the configuration for parsing the CSV file
	 * @return the created topic
	 */
	protected abstract ICSVTopic<I> createTopic(String topic, String file, ICSVParserConfiguration parserConfig);

	/**
	 * @param topic        The name of the topic to create
	 * @param filePattern  The pattern of the file
	 * @param parserConfig the configuration for parsing the CSV files
	 * @return the created topic
	 */
	protected abstract ICSVTopic<I> createDirectoryTopic(String topic, String filePattern,
														 ICSVParserConfiguration parserConfig);


	private void fetchBQData(ITransactionManager transactionManager) throws IOException {
		BigQueryProject bigQueryProject = new BigQueryProject(
				"C:\\Users\\ActiveViam\\Downloads\\BQ2AP-61c3af6d412c.json",
				"bq2ap-239312",
				"performance");
		bigQueryProject.startConnection();
		String[] storeNames = {TRADES_STORE_NAME,STATIC_BOOK_STORE_NAME,MD_BUCKET_SOD_STORE_NAME,FX_STORE_NAME,BUCKETS_STORE_NAME,RISK_BUCKET_STORE_NAME};
		String[] tableNames = {"Trades", "Static_Book", "MD_Bucket_SoD", "FX", "Buckets", "Risk_Bucket"};
		bigQueryProject.readTables(storeNames, tableNames, transactionManager);
		bigQueryProject.closeConnection();
	}


    /*
     * ************************* Other Sources **********************************
     * Suggestion: You might use instead or add other sources types like jdbc, POJO, ...
     * **************************************************************************
     */

	/*
	 * **************************** Initial load *********************************
	 */

	@Bean
    @DependsOn(value = "startManager")
    public Void initialLoad() throws Exception {
		System.out.println("IKBO");

    	// Start Transaction
    	final long before = System.nanoTime();
    	final ITransactionManager transactionManager = datastore.getTransactionManager();
    	transactionManager.startTransaction();
    	
    	// fetch the source(s) and perform bulk transaction
    	//csvSource().fetch(csvChannels);
    	fetchBQData(transactionManager);
    	
    	// Commit Transaction
    	transactionManager.commitTransaction();
    	final long elapsed = System.nanoTime() - before;
    	
    	LOGGER.info("Initial load completed in " + elapsed / 1000000L + "ms");
    	
    	printStoreSizes();
    	
    	return null;
    	
    }

	private void printStoreSizes() {
		// add some logging
		if (Boolean.parseBoolean(env.getProperty("storeStructure.display", "true"))) {
			// display the graph
			new JungSchemaPrinter(false).print("Datastore", datastore);
		}

		// Print stop watch profiling
		StopWatch.get().printTimings();
		// print sizes
		SchemaPrinter.printStoresSizes(datastore.getHead().getSchema());
	}

    private ICSVParserConfiguration createParserConfig(final int columnCount, final List<String> columns) {
        final CSVParserConfiguration cfg = columns == null ? new CSVParserConfiguration(columnCount) : new CSVParserConfiguration(columns);
        
        // If file has header, skip first line, else don't
        int skippedLines = Boolean.valueOf(env.getProperty("source.files.header", "true")) ? 1 : 0;
        
        cfg.setCharset(Charset.forName("UTF-8"));
        cfg.setNumberSkippedLines(skippedLines);
        cfg.setSeparator(env.getProperty("source.files.seperator", ",").charAt(0));
        return cfg;
    }

}
