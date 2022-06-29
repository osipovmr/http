import static spark.Spark.*;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    //���������� ��� ������
    public static Statement statement = null;
    public static Container container = new Container();
    public static TicketInfo ticketInfo = new TicketInfo();
    public static CodeInfo codeInfo = new CodeInfo();
    public static String check = null;
    static ResultSet resultAirport;
    static ResultSet resultTicket;
    static ResultSet resultCode;
    //1. ��� ����������� ������ ������ (ticket_no) ������ ��� ���������, �����, ������, ����� ������.
    public static String queryTicket = """
            select tickets.ticket_no,passenger_name, fare_conditions, status, city
            from bookings.tickets
            inner join bookings.ticket_flights on bookings.tickets.ticket_no = bookings.ticket_flights.ticket_no
            inner join bookings.flights on bookings.ticket_flights.flight_id = bookings.flights.flight_id
            inner join bookings.airports on bookings.flights.departure_airport = bookings.airports.airport_code
            limit 100
            """;
    //2. ��� ����������� ���� ��������� (airport_code) ������ ���������� ������� �� ����� ���������,
    // ������� ��������� �������� �� ����� ���������, ���������� ����������, ���������� �� ���������
    public static String queryCode = """
            select airport_code,
            count (\"���������� ����\") as \"���-�� ���������� ������\",
            sum (\"���-�� ���������� � �����\") as \"���-�� ���������� ����������\",
            sum(\"������� ��������� ������\")/count (\"���������� ����\") as \"������� ��������� ������\"
            from (
            select a.airport_code, departure_airport, f.flight_id as \"���������� ����\", count (t.ticket_no) as \"���-�� ���������� � �����\",  round (avg (amount), 2) as \"������� ��������� ������\"
            from bookings.airports a
            inner join bookings.flights f on a.airport_code = f.departure_airport
            inner join bookings.ticket_flights tf on f.flight_id = tf.flight_id
            inner join bookings.tickets t on tf.ticket_no =t.ticket_no
            where status = 'Departed'
            group by f.flight_id, a.airport_code
            order by f.flight_id ) as qwe
            group by qwe.airport_code
            order by airport_code
            """;

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

    public static ArrayList ticket() {
        try {
            ArrayList<String> a = new ArrayList<>();
            resultTicket = statement.executeQuery(queryTicket);
            while (resultTicket.next()) {
                ticketInfo.TicketInfo(resultTicket.getString("ticket_no"),
                        resultTicket.getString("passenger_name"),
                        resultTicket.getString("fare_conditions"),
                        resultTicket.getString("status"),
                        resultTicket.getString("city"));
                a.add(ticketInfo.toString());
                String key = resultTicket.getString("ticket_no");
                String value = ticketInfo.toString();
                container.setJedis(key,value);
            }
            System.out.println("���� ticket �������!");
            return a;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList code() {
        try {
            ArrayList<String> a = new ArrayList<>();
            resultCode = statement.executeQuery(queryCode);
            while (resultCode.next()) {
                codeInfo.CodeInfo(resultCode.getString("airport_code"),
                        resultCode.getString("���-�� ���������� ������"),
                        resultCode.getString("���-�� ���������� ����������"),
                        resultCode.getString("������� ��������� ������"));
                a.add(codeInfo.toString());
                String key = resultCode.getString("airport_code");
                String value = codeInfo.toString();
                container.setJedis(key,value);
            }
            System.out.println("���� code �������!");
            return a;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(connection());
        System.out.println(container.redisTester());
        ticket();
        code();

        get("/hello", (req, res) -> check);
        get("/ticket", (req, res) -> ticket());
        get("/code", (req, res) -> code());
    }

}
