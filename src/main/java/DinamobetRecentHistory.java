import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class DinamobetRecentHistory implements Runnable {

    private final ReentrantLock lockLightning = new ReentrantLock();

    private final String DB_DOMAIN = "jdbc:postgresql://localhost:5432/postgres";
    private final String DB_PASSWORD = "admin";

    @Override
    public void run() {
        try {
            Connection connection = DriverManager.getConnection(DB_DOMAIN, "postgres", DB_PASSWORD);
            while (true) {
                lockLightning.lock();
                update(connection, "live_lightning");
                update(connection, "live_turk_lightning");
                update(connection, "live_xxxtreme_lightning");
                update(connection, "live_immersive");
                lockLightning.unlock();
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.err.println("Bağlantı hatası 1: " + e.getMessage());
        }
    }

    public void update(Connection connection, String table) {
        try {
            List<Integer> recentNumber = getRecentNumber(connection, table);
            String lastRecentNumber = getLastRecentNumber(connection, table + "_recent_number");
            if (lastRecentNumber == null || !lastRecentNumber.equals(recentNumber.stream().map(Objects::toString).collect(Collectors.joining(", ")))) {
                saveLastRecentNumber(connection, table + "_recent_number", recentNumber.stream().map(Objects::toString).collect(Collectors.joining(", ")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean saveLastRecentNumber(Connection connection, String table, String lastRecentNumber) {
        try {
            String sql = "INSERT INTO dinamobet." + table + " (d_number, d_date) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, lastRecentNumber);
                preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Bağlantı hatası 2: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String getLastRecentNumber(Connection connection, String table) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT d_number FROM dinamobet." + table + " ORDER BY d_date DESC LIMIT 1");

            while (resultSet.next()) {
                String sqlNumber = resultSet.getString("d_number");
                return sqlNumber;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Bağlantı hatası 3: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public List<Integer> getRecentNumber(Connection connection, String table) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT d_number FROM dinamobet." + table);

            List<Integer> numbers = new ArrayList<>();
            while (resultSet.next()) {
                int sqlNumber = resultSet.getInt("d_number");
                numbers.add(sqlNumber);
            }

            resultSet.close();
            statement.close();
            return numbers.subList(0, 13);
        } catch (SQLException e) {
            System.err.println("Bağlantı hatası 4: " + e.getMessage());
        }
        return new ArrayList<>();
    }

}
