import java.sql.*;

import static spark.Spark.*;

public class HelloWorld {


    public static void main(String[] args) {
        LogIn logIn = new LogIn();
        Airports airports = new Airports();
        Query query = new Query();
        get("/hello", (req, res) -> logIn.connection()
                + "<br>"
                + query.query()
                + "<br>"
                + airports.getAirports());

        /*
           Airports airports = new Airports();
        Statement statement = null;
        String query = "select * from bookings.airports";
        ResultSet result;
        try (Connection conn = DriverManager.getConnection(url, login, password)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
                statement = conn.createStatement();
                result = statement.executeQuery(query);
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
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        get("/hello", (req, res) -> "�����, ������!"
                + "<br>"
                + "� ��� � �������!"
                + "<br>"
                + airports.getAirports()); */


    }
}
