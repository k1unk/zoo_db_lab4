import java.sql.Connection;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {
        start(2);
        start(4);
        start(8);
    }

    public static void start(int levelOfIsolation) {
        int quantityOfTransactions = 1000;

        final CountDownLatch initLatch = new CountDownLatch(3);

        final Connection connectionSelect = Utils.getConnection(levelOfIsolation);
        final Connection connectionInsert = Utils.getConnection(levelOfIsolation);
        final Connection connectionUpdate = Utils.getConnection(levelOfIsolation);

        new TransactionThread(connectionSelect, quantityOfTransactions,
                "SELECT", initLatch, levelOfIsolation).start();
        new TransactionThread(connectionInsert, quantityOfTransactions,
                "INSERT", initLatch, levelOfIsolation).start();
        new TransactionThread(connectionUpdate, quantityOfTransactions,
                "UPDATE", initLatch, levelOfIsolation).start();

        try {
            initLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


