/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.datastore;

import static com.activeviam.training.constants.StoreAndFieldConstants.*;
import static com.qfs.literal.ILiteralType.DOUBLE;
import static com.qfs.literal.ILiteralType.INT;
import static com.qfs.literal.ILiteralType.LONG;
import static com.qfs.literal.ILiteralType.STRING;

import java.util.Collection;
import java.util.LinkedList;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qfs.desc.IDatastoreSchemaDescription;
import com.qfs.desc.IReferenceDescription;
import com.qfs.desc.IStoreDescription;
import com.qfs.desc.impl.DatastoreSchemaDescription;
import com.qfs.desc.impl.ReferenceDescription;
import com.qfs.desc.impl.StoreDescriptionBuilder;
import com.qfs.server.cfg.IDatastoreDescriptionConfig;
import com.quartetfs.fwk.format.impl.LocalDateParser;

/**
 * Spring configuration file that exposes the datastore
 * {@link IDatastoreSchemaDescription description}.
 *
 * @author ActiveViam
 *
 */
@Configuration
public class DatastoreDescriptionConfig implements IDatastoreDescriptionConfig {

	/******************** Formatters ***************************/
	public static final String DATE_FORMAT = "localDate["+LocalDateParser.DEFAULT_PATTERN+"]";
	public static final String TIME_FORMAT = "localTime[HH:mm[:ss]]";

	@Bean
	public IStoreDescription tradesStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(TRADES_STORE_NAME)
		.withField(TRADES__SEQID, LONG).asKeyField()
		.withField(TRADES__BOOKINGSYSTEM, STRING)
		.withField(TRADES__INSTRUMENTID, INT)
		.withField(TRADES__TRADECURRENCY, STRING)
		.withField(TRADES__LIFECYCLESTATUS, STRING)
		.withField(TRADES__CLEAREDTRADESTATUS, STRING)
		.withField(TRADES__SETTLEDTRADESTATUS, STRING)
		.withField(TRADES__NOTIONAL, DOUBLE)
		.withField(TRADES__STATICTRADERID, INT)
		.withField(TRADES__STATICMOID, INT)
		.withField(TRADES__STATICSALESPERSONID, INT)
		.withField(TRADES__STATICCOUNTERPARTYID, INT)
		.withField(TRADES__STATICBOOKID, INT)
//		.onDuplicateKeyWithinTransaction().logException()
		.build();
	}

	@Bean
	public IStoreDescription staticBookStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(STATIC_BOOK_STORE_NAME)
		.withField(STATIC_BOOK__ID, INT).asKeyField()
		.withField(STATIC_BOOK__BOOKNAME, STRING)
		.withField(STATIC_BOOK__BOOKINGSYSTEM, STRING)
		.withField(STATIC_BOOK__REGION, STRING)
		.withField(STATIC_BOOK__CUTTIME, STRING)
		.withField(STATIC_BOOK__STATUS, STRING)
		.withField(STATIC_BOOK__BOOKTYPE, STRING)
		.withField(STATIC_BOOK__INTERNALRISKCONTROLFRAMEWORK, STRING)
		.withField(STATIC_BOOK__FXTREATMENT, STRING)
		.withField(STATIC_BOOK__RISKRELEVENT, STRING)
		.withField(STATIC_BOOK__PNLRELEVENT, STRING)
		.withField(STATIC_BOOK__DESCRIPTION, STRING)
		.withField(STATIC_BOOK__BOOKOWNERID, STRING)
		.withField(STATIC_BOOK__COSTCENTER, STRING)
		.withField(STATIC_BOOK__COSTCENTERTYPE, STRING)
		.withField(STATIC_BOOK__SUBDESKID, STRING)
		.withField(STATIC_BOOK__SUBDESK, STRING)
		.withField(STATIC_BOOK__SUBDESKTYPE, STRING)
		.withField(STATIC_BOOK__DESKID, STRING)
		.withField(STATIC_BOOK__DESKCODE, STRING)
		.withField(STATIC_BOOK__DESK, STRING)
		.withField(STATIC_BOOK__DESKREGION, STRING)
		.withField(STATIC_BOOK__DESKCITY, STRING)
		.withField(STATIC_BOOK__FUNCTIONID, STRING)
		.withField(STATIC_BOOK__FUNCTION, STRING)
		.withField(STATIC_BOOK__FUNCTIONCAT, STRING)
		.withField(STATIC_BOOK__SEGMENTID, STRING)
		.withField(STATIC_BOOK__SEGMENT, STRING)
		.withField(STATIC_BOOK__AREAID, STRING)
		.withField(STATIC_BOOK__AREA, STRING)
		.withField(STATIC_BOOK__GROUPID, STRING)
		.withField(STATIC_BOOK__GROUP, STRING)
//		.onDuplicateKeyWithinTransaction().logException()
		.build();
	}

	@Bean
	public IStoreDescription riskBucketStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(RISK_BUCKET_STORE_NAME)
		.withField(RISK_BUCKET__ID, LONG)
		.withField(RISK_BUCKET__TRADEID, LONG).asKeyField()
		.withField(RISK_BUCKET__RISKTYPE, STRING)
		.withField(RISK_BUCKET__RISKCURRENCY, STRING).dictionarized()
		.withField(RISK_BUCKET__VALUE, DOUBLE)
		.withField(RISK_BUCKET__MARKETDATAID, INT).asKeyField()
		.withField(RISK_BUCKET__SCENARIOID, INT)
		.withField(RISK_BUCKET__VALUATIONMODEL, STRING)
		.withField(RISK_BUCKET__RUN, STRING)
		.withField(RISK_BUCKET__ERRORSTATUS, STRING)
//		.onDuplicateKeyWithinTransaction().logException()
		.build();
	}

	@Bean
	public IStoreDescription marketDataStaticStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(MD_BUCKET_SOD_STORE_NAME)
		.withField(MD_BUCKET__ID, INT).asKeyField()
		.withField(MD_BUCKET__INSTRUMENTID, INT)
		.withField(MD_BUCKET__CURVENAME, STRING)
		.withField(MD_BUCKET__MDTYPE, STRING)
		.withField(MD_BUCKET__MDBUCKET, STRING)
		.withField(MD_BUCKET__VALUE_SOD, DOUBLE)
		.withField(MD_BUCKET__MARKETDATAVECTORID, INT)
		.withField(MD_BUCKET__VALUATIONMODEL, STRING)
		.withField(MD_BUCKET__RUN, STRING)
		.withField(MD_BUCKET__VERSION_SOD, INT)
		.withField(MD_BUCKET__ERRORSTATUS, STRING)
//		.onDuplicateKeyWithinTransaction().logException()
		.build();
	}

	@Bean
	public IStoreDescription mdIntradayStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(MD_BUCKET_INTRADAY_STORE_NAME)
			.withField(MD_BUCKET__ID, INT).asKeyField()
			.withField(MD_BUCKET__VALUE_INTRADAY, DOUBLE)
//			.onDuplicateKeyWithinTransaction().logException()
			.build();
	}

	@Bean
	public IStoreDescription fxStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(FX_STORE_NAME)
		.withField(FX__BASE_CURRENCY, STRING).asKeyField()
		.withField(FX__TARGET_CURRENCY, STRING).asKeyField()
		.withField(FX__RATE, DOUBLE)
//		.onDuplicateKeyWithinTransaction().logException()
		.build();
	}

	@Bean
	public IStoreDescription bucketStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(BUCKETS_STORE_NAME)
			.withField(MD_BUCKET__MDBUCKET, STRING).asKeyField()
			.withField(MD_BUCKET__MDSUMMARYGROUP, STRING)
			.withField(MD_BUCKET__MDSUMMARYBUCKET, STRING)
//			.onDuplicateKeyWithinTransaction().logException()
			.build();
	}

	@Bean
	public Collection<IReferenceDescription> references() {
		final Collection<IReferenceDescription> references = new LinkedList<>();
		// Trade store to static book
		references.add(ReferenceDescription.builder()
				.fromStore(TRADES_STORE_NAME)
				.toStore(STATIC_BOOK_STORE_NAME)
				.withName(joinName(TRADES_STORE_NAME, STATIC_BOOK_STORE_NAME))
				.withMapping(TRADES__STATICBOOKID, STATIC_BOOK__ID)
				.build());

		// Risk store to market data
		references.add(ReferenceDescription.builder()
				.fromStore(RISK_BUCKET_STORE_NAME)
				.toStore(MD_BUCKET_SOD_STORE_NAME)
				.withName(joinName(RISK_BUCKET_STORE_NAME, MD_BUCKET_SOD_STORE_NAME))
				.withMapping(RISK_BUCKET__MARKETDATAID, MD_BUCKET__ID)
				.build());

		// Risk store to trade data
		references.add(ReferenceDescription.builder()
				.fromStore(RISK_BUCKET_STORE_NAME)
				.toStore(TRADES_STORE_NAME)
				.withName(joinName(RISK_BUCKET_STORE_NAME, TRADES_STORE_NAME))
				.withMapping(RISK_BUCKET__TRADEID, TRADES__SEQID)
				.build());


		// MD store to bucket data
		references.add(ReferenceDescription.builder()
				.fromStore(MD_BUCKET_SOD_STORE_NAME)
				.toStore(BUCKETS_STORE_NAME)
				.withName(joinName(MD_BUCKET_SOD_STORE_NAME, BUCKETS_STORE_NAME))
				.withMapping(MD_BUCKET__MDBUCKET, MD_BUCKET__MDBUCKET)
				.build());

		return references;
	}

	/**
	 *
	 * Provide the schema description of the datastore.
	 * <p>
	 * It is based on the descriptions of the stores in the datastore, the descriptions of the
	 * references between those stores, and the optimizations and constraints set on the schema.
	 *
	 * @return schema description
	 */
	@Override
	@Bean
	public IDatastoreSchemaDescription schemaDescription() {

		final Collection<IStoreDescription> stores = new LinkedList<>();
		stores.add(tradesStoreDescription());
		stores.add(staticBookStoreDescription());
		stores.add(marketDataStaticStoreDescription());
		stores.add(mdIntradayStoreDescription());
		stores.add(riskBucketStoreDescription());
		stores.add(fxStoreDescription());
		stores.add(bucketStoreDescription());
		return new DatastoreSchemaDescription(stores, references());
	}

	public static String joinName(String from, String to) {
		return from + " To " + to;
	}

}
