import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
    private static final MongoClient client = MongoClients.create("mongodb+srv://<usuario>:<pass>@<cluster>.mongodb.net/?retryWrites=true&w=majority");

    public static MongoDatabase getDatabase() {
        return client.getDatabase("mensajeria");
    }
}