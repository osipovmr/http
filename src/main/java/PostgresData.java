import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PostgresData {
    static TicketInfo ticketInfo = new TicketInfo();
    public static AirportInfo airportInfo = new AirportInfo();
    public static String check = null;
    static Statement statement;
    static ContainerRedis container = new ContainerRedis();

    static ResultSet resultAirport;
    static ResultSet resultTicket;
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



    public static String ticket() {
        try {
            resultTicket = statement.executeQuery(queryTicket);
            if (resultTicket.next()) {
            ticketInfo.SourseCheck(resultTicket.getString("ticket_no"),
                    resultTicket.getString("passenger_name"),
                    resultTicket.getString("fare_conditions"),
                    resultTicket.getString("status"),
                    resultTicket.getString("city"));
            String key = resultTicket.getString("ticket_no");
            String value = ticketInfo.toString();
            container.setJedis(key, value);
            return ticketInfo.toString();}
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String airport() {
        try {
            resultAirport = statement.executeQuery(queryCode);
            if (resultAirport.next()) {
            airportInfo.SourseCheck(resultAirport.getString("airport_code"),
                    resultAirport.getString("���-�� ���������� ������"),
                    resultAirport.getString("���-�� ���������� ����������"),
                    resultAirport.getString("������� ��������� ������"));
            String key = resultAirport.getString("airport_code");
            String value = airportInfo.toString();
            container.setJedis(key, value);
            return airportInfo.toString();}
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
