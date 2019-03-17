package sample.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseService {

    private String userName = "root";
    private String password = "root";
    private String connectionURL = "jdbc:mysql://localhost:3306/EY_Task_One?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public void setupDatabase() {
        createDatabase();
        createTables();
    }

    private void createDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", userName, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS EY_Task_One");
            System.out.println("// Successfully created database.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("// Error in method createDatabase().");
        }
    }

    private void createTables() {

        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `ey_task_one`.`random_lines` (`id` INT NOT NULL AUTO_INCREMENT," +
                    "  `random_date` VARCHAR(45) NOT NULL," +
                    "  `eng_chars` VARCHAR(45) NOT NULL," +
                    "  `rus_chars` VARCHAR(45) NOT NULL," +
                    "  `even_num` INT NOT NULL," +
                    "  `double_num` DECIMAL(10,8) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addToTable(String rnd_date, String eng, String rus, int even, double doub) {
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into random_lines (random_date, eng_chars, rus_chars, even_num, double_num) values("
                    + "'" + rnd_date + "'" + ", "
                    + "'" + eng + "'" + ", "
                    + "'" + rus + "'" + ", "
                    + "'" + even + "'" + ", "
                    + "'" + doub + "'" + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}