package com.activeviam.training.cfg.datastore;

import static com.activeviam.training.constants.StoreAndFieldConstants.*;
import static com.qfs.literal.ILiteralType.DOUBLE;
import static com.qfs.literal.ILiteralType.INT;
import static com.qfs.literal.ILiteralType.LONG;
import static com.qfs.literal.ILiteralType.STRING;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qfs.desc.IReferenceDescription;
import com.qfs.desc.IStoreDescription;
import com.qfs.desc.impl.StoreDescriptionBuilder;

/**
 * 
 * This configuration is used when loading vectorised data.
 * All other stores stay the same
 *
 */
@Configuration
public class VectorisedDatastoreDescriptionConfig extends DatastoreDescriptionConfig {
	
	
	@Bean
	@Override
	public IStoreDescription riskBucketStoreDescription() {
		return new StoreDescriptionBuilder().withStoreName(RISK_BUCKET_STORE_NAME)
		.withField(RISK_BUCKET__ID, LONG)
		.withField(RISK_BUCKET__TRADEID, LONG).asKeyField()
		.withField(RISK_BUCKET__RISKTYPE, STRING)
		.withField(RISK_BUCKET__RISKCURRENCY, STRING)
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
	
	/**
	 * remove the md to bucket reference
	 */
	@Override
	@Bean
	public Collection<IReferenceDescription> references() {
		return super.references().stream()
				//.filter(r -> !r.getName().equals(joinName(MD_BUCKET_SOD_STORE_NAME, BUCKETS_STORE_NAME)))
				.collect(Collectors.toCollection(LinkedList::new));
	}
	
}
