import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public class TransactionThread extends Thread {
    private final Connection connection;
    private final int quantityOfTransactions;
    private final String transactionsType;
    private final CountDownLatch initLatch;
    private final int levelOfIsolation;

    public TransactionThread(Connection connection, int quantityOfTransactions,
                             String transactionsType,CountDownLatch initLatch,
                             int levelOfIsolation) {
        this.connection = connection;
        this.quantityOfTransactions = quantityOfTransactions;
        this.transactionsType = transactionsType;
        this.initLatch = initLatch;
        this.levelOfIsolation = levelOfIsolation;
    }

    public void run() {
        initLatch.countDown();
        try {
            initLatch.await();
            switch (transactionsType) {
                case "SELECT" -> Transactions.selectMeals(connection, quantityOfTransactions, levelOfIsolation);
                case "INSERT" -> Transactions.insertMeals(connection, quantityOfTransactions, levelOfIsolation);
                case "UPDATE" -> Transactions.updateMeals(connection, quantityOfTransactions, levelOfIsolation);
            }
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        } finally {
            Utils.close(connection);
        }
    }
}






