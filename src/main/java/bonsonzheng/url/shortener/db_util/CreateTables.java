package bonsonzheng.url.shortener.db_util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;

/**
 * Created by zhengbangsheng on 2020/7/27.
 */
public class CreateTables {

    public static void createUrlMap(){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

// Attribute definitions
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("LongUrl")
                .withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("ShortUrl")
                .withAttributeType("S"));

// Table key schema
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement()
                .withAttributeName("LongUrl")
                .withKeyType(KeyType.HASH));  //Partition key

// Global Secondary Index
        GlobalSecondaryIndex shortUrlIndex = new GlobalSecondaryIndex()
                .withIndexName("ShortUrl")
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 10)
                        .withWriteCapacityUnits((long) 1))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL)).withKeySchema(tableKeySchema);

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("UrlMap")
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 5)
                        .withWriteCapacityUnits((long) 1))
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(tableKeySchema)
                .withGlobalSecondaryIndexes(shortUrlIndex);

        Table table = dynamoDB.createTable(createTableRequest);
        System.out.println(table.getDescription());
    }

    public static void main(String[] args) {
        createUrlMap();
    }
}
