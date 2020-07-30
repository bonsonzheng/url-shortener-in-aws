package bonsonzheng.url.shortener.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.springframework.stereotype.Repository;

import java.io.IOException;


@Repository
public class CounterDao {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = "Counter";

//    public static void main(String[] args) throws IOException {
//
//        retrieveItem();
//
//        updateExistingAttributeConditionally();
//
//        retrieveItem();
//
//    }

    private static void createItems() {

        Table table = dynamoDB.getTable(tableName);
        try {

            Item item = new Item().withPrimaryKey("id", 0).withNumber("nextVal", 10);
            PutItemSpec putItemSpec = new PutItemSpec().withItem(item).withConditionExpression("attribute_not_exists(nextVal)");

            table.putItem(putItemSpec);

        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }

    public static void retrieveItem() {
        Table table = dynamoDB.getTable(tableName);

        try {

            Item item = table.getItem("id", 0, "nextVal", null);

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());

        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }

    }

    public long incrementAndGet(long chunkSize) {

        Table table = dynamoDB.getTable(tableName);

        try{
            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", 0)
                    .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set nextVal = nextVal + :inc")
                    .withValueMap(new ValueMap().withNumber(":inc", chunkSize));

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after conditional update to new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());

            return outcome.getItem().getLong("nextVal");

        } catch (Exception e) {
            System.err.println("Error updating item in " + tableName);
            System.err.println(e.getMessage());
        }

        return -1;
    }
}
