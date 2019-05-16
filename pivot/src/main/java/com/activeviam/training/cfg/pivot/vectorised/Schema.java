package com.activeviam.training.cfg.pivot.vectorised;

import static com.activeviam.training.cfg.datastore.DatastoreDescriptionConfig.joinName;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET_SOD_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.BUCKETS_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__ID;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__MDBUCKET;
import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__BOOKNAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__BOOKTYPE;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__DESCRIPTION;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__DESK;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__REGION;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__SEGMENT;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__STATUS;
import static com.activeviam.training.constants.StoreAndFieldConstants.STATIC_BOOK__SUBDESK;
import static com.activeviam.training.constants.StoreAndFieldConstants.TRADES_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.TRADES__SEQID;

import com.activeviam.builders.StartBuilding;
import com.qfs.desc.IDatastoreSchemaDescription;
import com.quartetfs.biz.pivot.definitions.ISelectionDescription;

public class Schema {
	
	/**
	 * Creates the {@link ISelectionDescription} for Pivot Schema.
	 * 
	 * @param datastoreDescription : The datastore description
	 * @return The created selection description
	 */
	public static ISelectionDescription createSchemaSelectionDescription(
			final IDatastoreSchemaDescription datastoreDescription) {
		return StartBuilding.selection(datastoreDescription)
				.fromBaseStore(RISK_BUCKET_STORE_NAME)
				.withAllFields()
				
				// Trade Store fields
				.usingReference(joinName(RISK_BUCKET_STORE_NAME, TRADES_STORE_NAME))
				.withAllFields().except(TRADES__SEQID)

				
				// Book Store fields
				.usingReference(joinName(RISK_BUCKET_STORE_NAME, TRADES_STORE_NAME) + "/" + joinName(TRADES_STORE_NAME, STATIC_BOOK_STORE_NAME))
				.withFields(STATIC_BOOK__BOOKNAME,
						STATIC_BOOK__REGION,
						STATIC_BOOK__STATUS,
						STATIC_BOOK__BOOKTYPE,
						STATIC_BOOK__DESCRIPTION,
						STATIC_BOOK__SEGMENT,
						STATIC_BOOK__DESK,
						STATIC_BOOK__SUBDESK						
						)
				
				// MD Store fields				
				.usingReference(joinName(RISK_BUCKET_STORE_NAME, MD_BUCKET_SOD_STORE_NAME))
				.withAllFields().except(MD_BUCKET__ID)
			
				.build();
	}

}
