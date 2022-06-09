import static spark.Spark.*;

import java.sql.*;

public class HelloWorld {
    public static Statement statement = null;
    public static Airports airports = new Airports();
    public static Tickets tickets = new Tickets();
    public static Codes codes = new Codes();
    public static String check = null;
    static ResultSet resultAirport;
    static ResultSet resultTicket;
    static ResultSet resultCode;
    public static String queryAirport = "select * from bookings.airports";
    //1. ��� ����������� ������ ������ (ticket_no) ������ ��� ���������, �����, ������, ����� ������.
    public static String queryTicket = "select tickets.ticket_no, "
            + "passenger_name, "
            + "fare_conditions, "
            + "status, "
            + "city"
            + "\n"
            + "from bookings.tickets"
            + "\n" +
            "inner join bookings.ticket_flights on bookings.tickets.ticket_no = bookings.ticket_flights.ticket_no"
            + "\n" +
            " inner join bookings.flights on bookings.ticket_flights.flight_id = bookings.flights.flight_id"
            + "\n" +
            "inner join bookings.airports on bookings.flights.departure_airport = bookings.airports.airport_code"
            + "\n" +
            "limit 100";
    //2. ��� ����������� ���� ��������� (airport_code) ������ ���������� ������� �� ����� ���������,
    // ������� ��������� �������� �� ����� ���������, ���������� ����������, ���������� �� ���������
    public static String queryCode = "select airport_code, " +
            "count (\"���������� ����\") as \"���-�� ���������� ������\"," +
            "sum (\"���-�� ���������� � �����\") as \"���-�� ���������� ����������\"," +
            "sum(\"������� ��������� ������\")/count (\"���������� ����\") as \"������� ��������� ������\"\n" +
            "from (\n" +
            "select a.airport_code, departure_airport, f.flight_id as \"���������� ����\", count (t.ticket_no) as \"���-�� ���������� � �����\",  round (avg (amount), 2) as \"������� ��������� ������\"\n" +
            "from bookings.airports a \n" +
            "inner join bookings.flights f on a.airport_code = f.departure_airport \n" +
            "inner join bookings.ticket_flights tf on f.flight_id = tf.flight_id \n" +
            "inner join bookings.tickets t on tf.ticket_no =t.ticket_no \n" +
            "where status = 'Departed'\n" +
            "group by f.flight_id, a.airport_code \n" +
            "order by f.flight_id ) as qwe\n" +
            "group by qwe.airport_code\n" +
            "order by airport_code";

    public static String connection() {
        try {
            Connection conn = DataSourse.getConnection();
            if (conn != null) {
                statement = conn.createStatement();
                check = "Connected to the database!";
            } else {
                check = "Failed to make connection!";
            }
        } catch (SQLException e) {
            check = String.valueOf(System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            check = "������ SQL!";
        }
        return check;
    }
    public static void airport() {
        try {
            resultAirport = statement.executeQuery(queryAirport);
            while (resultAirport.next()) {
                airports.add(resultAirport.getString("airport_code")
                        + "\t " + resultAirport.getString("airport_name")
                        + "\t" + resultAirport.getString("city")
                        + "\t" + resultAirport.getString("longitude")
                        + "\t" + resultAirport.getString("latitude")
                        + "\t" + resultAirport.getString("timezone")
                        + "<br>");
            }
            System.out.println("���� airport �������!");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ticket() {
        try {
            resultTicket = statement.executeQuery(queryTicket);
            while (resultTicket.next()) {
                tickets.add(resultTicket.getString("ticket_no")
                        + "\t " + resultTicket.getString("passenger_name")
                        + "\t" + resultTicket.getString("fare_conditions")
                        + "\t" + resultTicket.getString("status")
                        + "\t" + resultTicket.getString("city")
                        + "<br>");
            }
            System.out.println("���� ticket �������!");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void code() {
        try {
            resultCode = statement.executeQuery(queryCode);
            while (resultCode.next()) {
                codes.add( resultCode.getString("airport_code")
                        + "\t " +  resultCode.getString("���-�� ���������� ������")
                        + "\t" +  resultCode.getString("���-�� ���������� ����������")
                        + "\t" +  resultCode.getString("������� ��������� ������")
                        + "<br>");
            }
            System.out.println("���� code �������!");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(connection());
        airport();
        ticket();
        code();

        for (int i = 0; i < airports.list.size(); i++) {
            System.out.println(airports.list.get(i));
        }
        for (int i = 0; i < tickets.list.size(); i++) {
            System.out.println(tickets.list.get(i));
        }
        for (int i = 0; i < codes.list.size(); i++) {
            System.out.println(codes.list.get(i));
        }
        get("/hello", (req, res) -> check);
        get("/airport", (req, res) -> airports.getAirports());
        get("/ticket", (req, res) -> tickets.getTicket());
        get("/code", (req, res) -> codes.getCode());
    }

}
