import java.sql.*;

public class LogIn {
    public static String url = "jdbc:postgresql://c-c9qkc196nr3nge59qclr.rw.mdb.yandexcloud.net:6432/students-project";
    public static String login = "students-project";
    public static String password = "kd5HdsI2fcidKsqC";
    public static Statement statement = null;
    public static ResultSet result;
    public static Query query = new Query();
    public static Airports airports = new Airports();

    public static String connection() {
        ResultSet result;
        Statement statement = null;
        try (Connection conn = DriverManager.getConnection(url, login, password)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
                result = statement.executeQuery(query.query());
                statement = conn.createStatement();
                while (result.next()) {
                    System.out.println(result.getString("airport_code") + "\t " + result.getString("airport_name")
                            + "\t" + result.getString("city")
                            + "\t" + result.getString("longitude")
                            + "\t" + result.getString("latitude")
                            + "\t" + result.getString("timezone"));
                    airports.add(result.getString("airport_code") + "\t " + result.getString("airport_name")
                            + "\t" + result.getString("city")
                            + "\t" + result.getString("longitude")
                            + "\t" + result.getString("latitude")
                            + "\t" + result.getString("timezone")
                            + "<br>");
                }
                return "Connected to the database!"
                        + "<br>"
                        + url;
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed to make connection!";
    }
}

