import static spark.Spark.*;

import java.sql.*;


public class HelloWorld {
    public static String url = "jdbc:postgresql://c-c9qkc196nr3nge59qclr.rw.mdb.yandexcloud.net:6432/students-project";
    public static String login = "students-project";
    public static String password = "kd5HdsI2fcidKsqC";
    public static Statement statement = null;
    public static Query query = new Query();
    public static Airports airports = new Airports();
    public static String check = null;
    static ResultSet result;

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(url, login, password);
            if (conn != null) {
                statement = conn.createStatement();
                result = statement.executeQuery(query.query());
                check = "Connected to the database!";
                while (result.next()) {
                    airports.add(result.getString("airport_code") + "\t " + result.getString("airport_name")
                            + "\t" + result.getString("city")
                            + "\t" + result.getString("longitude")
                            + "\t" + result.getString("latitude")
                            + "\t" + result.getString("timezone")
                            + "<br>");
                }
            } else {
                check = "Failed to make connection!";
            }

        } catch (SQLException e) {
            check = String.valueOf(System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            check = "Ошибка SQL!";
        }
        System.out.println(check);
        for (int i = 0; i < airports.list.size(); i++) {
            System.out.println(airports.list.get(i));
        }
        get("/hello", (req, res) -> airports.getAirports());
    }

}

