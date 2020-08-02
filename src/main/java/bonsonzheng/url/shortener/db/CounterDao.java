package bonsonzheng.url.shortener.db;

import bonsonzheng.url.shortener.service.UrlShortenService;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class CounterDao {


    public static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    public static final DynamoDB dynamoDB = new DynamoDB(client);
    public static final String tableName = "Counter";

    public long incrementAndGet(long chunkSize) throws Exception{

        Table table = dynamoDB.getTable(tableName);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", 0)
                .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set nextVal = nextVal + :inc")
                .withValueMap(new ValueMap().withNumber(":inc", chunkSize));

        UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

        return outcome.getItem().getLong("nextVal");


    }
}
