


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Transactions {
    public static ArrayList<Long> selectMeals(Connection connection, int quantityOfTransactions, int levelOfIsolation) {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < quantityOfTransactions; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SELECT id, food_name FROM meals where food_name like '%da%'");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001"))
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }

        Utils.writeTimeResultsToFile(levelOfIsolation + "-select.txt", result);
        return result;
    }

    public static ArrayList<Long> insertMeals(Connection connection, int quantityOfTransactions, int levelOfIsolation) throws SQLException {
        ArrayList<String> names = Utils.fileDataToArray("resources/meal_names.txt");
        Collections.shuffle(names);

        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < quantityOfTransactions; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(String.format("INSERT INTO Meals (food_name, calories, price)" +
                                    " VALUES ('%s','%s','%s')",
                            names.get(i % names.size()),
                                    (Utils.random.nextInt(300) + 100),
                                    (Utils.random.nextInt(300) + 100))
                    );
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001"))
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }

        Utils.writeTimeResultsToFile(levelOfIsolation + "-insert.txt", result);
        return result;
    }

    public static ArrayList<Long> updateMeals(Connection connection, int quantityOfTransactions, int levelOfIsolation) {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < quantityOfTransactions; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("UPDATE meals SET food_name = 'fadadq' where food_name like '%da%'");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001"))// serializable error
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }
        Utils.writeTimeResultsToFile(levelOfIsolation + "-update.txt", result);
        return result;
    }
}



