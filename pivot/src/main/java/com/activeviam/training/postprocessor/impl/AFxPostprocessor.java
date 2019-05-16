package com.activeviam.training.postprocessor.impl;

import static com.activeviam.training.constants.StoreAndFieldConstants.FX_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.FX__RATE;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import com.qfs.store.IDatastoreVersion;
import com.qfs.store.query.impl.DatastoreQueryHelper;
import com.qfs.store.record.IRecordReader;
import com.quartetfs.biz.pivot.cube.hierarchy.ILevelInfo;
import com.quartetfs.biz.pivot.cube.hierarchy.measures.IPostProcessorCreationContext;
import com.quartetfs.biz.pivot.postprocessing.impl.ADynamicAggregationPostProcessor;
import com.quartetfs.fwk.QuartetException;

public abstract class AFxPostprocessor extends ADynamicAggregationPostProcessor<Double, Double> {

		/** serialVersionUID */
		private static final long serialVersionUID = 15874126988574L;

		/**
		 * Reference currency property. This must be defined in the PP definition.
		 * A real project would use a context value or something, but for ease of use we don't
		 */
		public static final String REF_CURRENCY_PROPERTY = "refCurrency";
		
		/** used for debugging and counting operations */
		protected AtomicInteger leafCount = new AtomicInteger(0);
		protected AtomicInteger datastoreQueryCount = new AtomicInteger(0);

		/** currency level info */
		protected ILevelInfo currencyLevelInfo = null;
		
		/** referenceCurrency to do the Calc in */
		protected String refCurrency;


		/** the object to return when an exchange rate is not found in the data store */
		protected static final Double RATE_NOT_FOUND = Double.MIN_VALUE;

		/**
		 * Constructor
		 * @param name The name of the post-processor
		 * @param creationContext The {@link IPostProcessorCreationContext creation context} of this post-processor.
		 */
		public AFxPostprocessor(String name, IPostProcessorCreationContext creationContext) {
			super(name, creationContext);
		}

		@Override
		public void init(Properties properties) throws QuartetException {
			super.init(properties);

			// init required level values
			currencyLevelInfo = this.leafLevelsInfo.get(0);
			refCurrency = properties.getProperty("refCurrency");			

		}
		
		/**
		 * Retrieve the rate from the forex store
		 * @param dv the datastore version to query
		 * @param currency the currency to retrieve rate for
		 * @param targetCurrency the target currency to retrieve rate for
		 * @return the conversion rate from {@code currency} to {@code targetCurrency}
		 */
		protected Double getRate(IDatastoreVersion dv, String targetCurrency, String currency) {
			// Uncomment this method to count how many times we  query the datastore
			// System.out.println(datastoreQueryCount.getAndIncrement());
			
			final IRecordReader r = DatastoreQueryHelper.getByKey(dv, FX_STORE_NAME, new Object[] { currency,
					targetCurrency }, FX__RATE);
			if (null == r) {
				if (logger.isLoggable(Level.WARNING)) {
					logger.log(Level.WARNING, "Exchange rate from currency [" + currency + "] to currency [" + targetCurrency + "] not found.");
				}
				return RATE_NOT_FOUND;
			}

			return r.readDouble(0);
		}



}
