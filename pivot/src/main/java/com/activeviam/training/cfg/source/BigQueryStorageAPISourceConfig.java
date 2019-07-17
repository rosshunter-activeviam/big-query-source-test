package com.activeviam.training.cfg.source;

import com.activeviam.training.cfg.pivot.profiles.StandardConfig;
import com.qfs.msg.csv.ICSVParserConfiguration;
import com.qfs.msg.csv.ICSVTopic;
import com.qfs.store.transaction.ITransactionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Path;

import static com.activeviam.training.constants.StoreAndFieldConstants.*;
import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET_STORE_NAME;

@Profile(BigQuerySourceConfig.SPRING_PROFILE)
@Configuration
public class BigQuerySourceConfig extends ASourceConfig<Path> {

    public static final String SPRING_PROFILE = "bigquery";

    @Override
    protected ICSVTopic<Path> createTopic(String topic, String file, ICSVParserConfiguration parserConfig) {
        return null;
    }

    @Override
    protected ICSVTopic<Path> createDirectoryTopic(String topic, String filePattern, ICSVParserConfiguration parserConfig) {
        return null;
    }

    @Override
    void fetchData(ITransactionManager transactionManager) {
        System.out.println("FETCHING BQ DATA");
        try {
            fetchBQData(transactionManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fetchBQData(ITransactionManager transactionManager) throws IOException {
        BigQueryProject bigQueryProject = new BigQueryProject(
                "C:\\Users\\ActiveViam\\Downloads\\aptest-240212-5112f809b5fb.json",
                "bq2ap-239312",
                "performance");
        bigQueryProject.startConnection();
        String[] storeNames = {TRADES_STORE_NAME, STATIC_BOOK_STORE_NAME, MD_BUCKET_SOD_STORE_NAME, FX_STORE_NAME, BUCKETS_STORE_NAME};
        String[] tableNames = {"Trades", "Static_Book", "MD_Bucket_SoD", "FX", "Buckets"};
        bigQueryProject.readTables(storeNames, tableNames, transactionManager);
        bigQueryProject.closeConnection();
    }
}
