package com.activeviam.training.cfg.source;

import com.activeviam.training.cfg.datastore.DatastoreDescriptionConfig;
import com.google.api.client.util.Preconditions;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ServerStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.storage.v1beta1.*;
import com.google.cloud.bigquery.storage.v1beta1.Storage.ReadSession;
import com.qfs.desc.IFieldDescription;
import com.qfs.desc.IStoreDescription;
import com.qfs.store.transaction.ITransactionManager;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static java.lang.Math.toIntExact;

public class BigQueryProject {

    private String credentialsPath;
    private String projectId;
    private String datasetId;
    private BigQueryStorageClient client;
    private HashMap<String, List<NameAndType>> storeIndices = new HashMap<>();
    private HashMap<String,String> storeAndTableNames = new HashMap<>();


    public BigQueryProject(String credentialsPath, String projectId, String datasetId) {
        this.credentialsPath = credentialsPath;
        this.projectId = projectId;
        this.datasetId = datasetId;
    }

    public void startConnection() throws IOException {
        GoogleCredentials credentials;
        File credentialsFile = new File(credentialsPath);
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }

        BigQueryStorageSettings bigQueryStorageSettings =
                BigQueryStorageSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        client = BigQueryStorageClient.create(bigQueryStorageSettings);
    }

    public void closeConnection() {
        client.close();
    }

    public void readTables(String[] storeNames, String[] tableNames, ITransactionManager transactionManager) throws IOException {
        getFieldTypes(storeNames);
        for (int i = 0; i < storeNames.length; i++) {
            storeAndTableNames.put(storeNames[i], tableNames[i]);
        }
        for (String storeName: storeIndices.keySet()) {
            ReadSession session = getReadSession(client, storeAndTableNames.get(storeName));
            SimpleRowReader reader = new SimpleRowReader(
                    new Schema.Parser().parse(session.getAvroSchema().getSchema()));

            Preconditions.checkState(session.getStreamsCount() > 0);
            Storage.StreamPosition readPosition = Storage.StreamPosition.newBuilder().setStream(session.getStreams(0)).build();
            Storage.ReadRowsRequest request = Storage.ReadRowsRequest.newBuilder()
                    .setReadPosition(readPosition)
                    .build();

            ServerStream<Storage.ReadRowsResponse> stream = client.readRowsCallable().call(request);
            long startTime = System.currentTimeMillis();
            int streamLength = 0;
            List<Object[]> newEntry = new ArrayList<>();
            for (Storage.ReadRowsResponse response : stream) {
                //System.out.println("Time 0:" + (System.currentTimeMillis() - startTime));
                Preconditions.checkState(response.hasAvroRows());
                //System.out.println("Time 1:" + (System.currentTimeMillis() - startTime));
                newEntry.addAll(reader.processRows(response.getAvroRows(), storeName));
                if (streamLength % 10 == 0) {
                    transactionManager.addAll(storeName, newEntry);
                    newEntry.clear();
                }
                //System.out.println("Time 2:" + (System.currentTimeMillis() - startTime));
                streamLength += 1;
            }
            transactionManager.addAll(storeName, newEntry);

            System.out.println("stream length:"+streamLength);
            //System.out.println("Time 3:" + (System.currentTimeMillis() - startTime));
        }
    }

    private ReadSession getReadSession(BigQueryStorageClient bigQueryStorageClient, String tableName) {
        TableReferenceProto.TableReference tableReference = TableReferenceProto.TableReference.newBuilder().setDatasetId(datasetId).setTableId(tableName).setProjectId(projectId).build();
        String parent = "projects/" + projectId;
        Storage.CreateReadSessionRequest.Builder builder = Storage.CreateReadSessionRequest.newBuilder()
                .setParent(parent)
                .setTableReference(tableReference)
                .setFormat(Storage.DataFormat.AVRO);

        return bigQueryStorageClient.createReadSession(builder.build());
    }

    private class SimpleRowReader {

        private final DatumReader<GenericRecord> datumReader;

        private BinaryDecoder decoder = null;

        private GenericRecord row = null;

        public SimpleRowReader(Schema schema) {
            Preconditions.checkNotNull(schema);
            datumReader = new GenericDatumReader<>(schema);
        }

        public List<Object[]> processRows(AvroProto.AvroRows avroRows, String storeName) throws IOException {
            decoder = DecoderFactory.get()
                    .binaryDecoder(avroRows.getSerializedBinaryRows().toByteArray(), decoder);
            List<Object[]> newEntry = new ArrayList<>();

            //System.out.println(storeName);
            while (!decoder.isEnd()) {
                row = datumReader.read(row, decoder);

                Map<String, Object> myMap = new HashMap<>();
                for (int i = 0; i < storeIndices.get(storeName).size(); i++) {
                    String fieldName = storeIndices.get(storeName).get(i).getName();
                    String fieldType = storeIndices.get(storeName).get(i).getType();
                    Object entry = row.get(i);
                    entry = castEntry(entry, fieldType);
                    myMap.put(fieldName, entry);
                }

                Object[] newRow = new Object[storeIndices.get(storeName).size()];

                for (String fieldName: myMap.keySet()) {
                    Object entry = myMap.get(fieldName);
                    if (entry != null) {
                        newRow[findIndex(storeName, fieldName)] = entry;
                    }
                }
                newEntry.add(newRow);
            }
            return newEntry;
        }
    }

    private int findIndex(String storeName, String fieldName) {
        int i = 0;
        for (NameAndType nameAndType: storeIndices.get(storeName)) {
            if (nameAndType.getName().equals(fieldName)) {
                return i;
            }
            i += 1;
        }
        return 0;
    }

    private void getFieldTypes(String[] storeNames) {
        Collection<IStoreDescription> stores = (Collection<IStoreDescription>) new DatastoreDescriptionConfig().schemaDescription().getStoreDescriptions();
        for (IStoreDescription store: stores) {
            String storeName = store.getName();
            if (Arrays.asList(storeNames).contains(storeName)) {
                List namesAndTypes = new ArrayList();
                for (IFieldDescription field : store.getFields()) {
                    namesAndTypes.add(new NameAndType(field.getName(), field.getDataType()));
                }
                storeIndices.put(storeName, namesAndTypes);
            }
        }
    }

    private class NameAndType {
        private final String name;
        private final String type;

        public NameAndType(final String name, final String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    private Object castEntry(Object entry, String fieldType) {
        if (entry == null) {
            return null;
        }
        switch (fieldType) {
            case "int" : return toIntExact((long) entry);
            case "string" : return String.valueOf(entry);
            //case "double" : return
        }

        if (fieldType.startsWith("localDate")) {
            return Date.from(Instant.ofEpochSecond((int) entry * 24 * 60 * 60));
        }

        return entry;
    }

}
