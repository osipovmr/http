import java.sql.*;

public class PostgresData {
    public static Connection conn;

    public static String ticket(String ticketNo) {
        //1. ��� ����������� ������ ������ (ticket_no) ������ ��� ���������, �����, ������, ����� ������.
        //= "'0005432000860'";
        String queryTicket = """
                select tickets.ticket_no,passenger_name, fare_conditions, status, city
                from bookings.tickets
                inner join bookings.ticket_flights on bookings.tickets.ticket_no = bookings.ticket_flights.ticket_no
                inner join bookings.flights on bookings.ticket_flights.flight_id = bookings.flights.flight_id
                inner join bookings.airports on bookings.flights.departure_airport = bookings.airports.airport_code
                where tickets.ticket_no = ?
                """;
        ResultSet resultTicket;
        ContainerRedis container = new ContainerRedis();
        try {
            TicketInfo ticketInfo = new TicketInfo();
            PreparedStatement preparedStatement = conn.prepareStatement(queryTicket);
            preparedStatement.setString(1, ticketNo);
            resultTicket = preparedStatement.executeQuery();
            if (resultTicket.next()) {
                ticketInfo.SourseCheck(resultTicket.getString("ticket_no"),
                        resultTicket.getString("passenger_name"),
                        resultTicket.getString("fare_conditions"),
                        resultTicket.getString("status"),
                        resultTicket.getString("city"));
                String key = resultTicket.getString("ticket_no");
                String value = ticketInfo.toString();
                container.setJedis(key, value);
                return ticketInfo.toString();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String airport(String code) {
        //2. ��� ����������� ���� ��������� (airport_code) ������ ���������� ������� �� ����� ���������,
        // ������� ��������� �������� �� ����� ���������, ���������� ����������, ���������� �� ���������
        String queryCode = """
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
                where status = 'Departed' and
                a.airport_code = ?
                group by f.flight_id, a.airport_code
                order by f.flight_id ) as qwe
                group by qwe.airport_code
                order by airport_code
                """;
        try {
            ResultSet resultAirport;
            ContainerRedis container = new ContainerRedis();
            PreparedStatement preparedStatement = conn.prepareStatement(queryCode);
            preparedStatement.setString(1, code);
            AirportInfo airportInfo = new AirportInfo();
            resultAirport = preparedStatement.executeQuery();
            if (resultAirport.next()) {
                airportInfo.SourseCheck(resultAirport.getString("airport_code"),
                        resultAirport.getString("���-�� ���������� ������"),
                        resultAirport.getString("���-�� ���������� ����������"),
                        resultAirport.getString("������� ��������� ������"));
                String key = resultAirport.getString("airport_code");
                String value = airportInfo.toString();
                container.setJedis(key, value);
                return airportInfo.toString();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
