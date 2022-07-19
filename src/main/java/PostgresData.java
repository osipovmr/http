import com.google.gson.Gson;

import java.io.IOException;
import java.sql.*;

public class PostgresData {
    static Statement statement;
    static Connection conn;

    static {
        try {
            conn = DataSourse.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String connection() {
        String check;
        try {
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
            check = "Ошибка SQL!";
        }
        return check;
    }


    public static String ticket(String ticketNo) {
        String queryTicket = """
                select tickets.ticket_no,passenger_name, fare_conditions, status, city
                from bookings.tickets
                inner join bookings.ticket_flights on bookings.tickets.ticket_no = bookings.ticket_flights.ticket_no
                inner join bookings.flights on bookings.ticket_flights.flight_id = bookings.flights.flight_id
                inner join bookings.airports on bookings.flights.departure_airport = bookings.airports.airport_code
                where tickets.ticket_no = ?""";
        ResultSet resultTicket;
        ContainerRedis container = new ContainerRedis();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryTicket);
            preparedStatement.setString(1, ticketNo);
            resultTicket = preparedStatement.executeQuery();
            if (resultTicket.next()) {
                TicketInfo ticketInfo = new TicketInfo(resultTicket.getString("ticket_no"),
                        resultTicket.getString("passenger_name"),
                        resultTicket.getString("fare_conditions"),
                        resultTicket.getString("status"),
                        resultTicket.getString("city"));
                Gson gson = new Gson();
                String ticketInfoJson = gson.toJson(ticketInfo);
                String key = resultTicket.getString("ticket_no");
                String value = ticketInfoJson;
                container.setJedis(key, value);
                return ticketInfoJson;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String airport(String code) {
        ResultSet resultAirport;
        ContainerRedis container = new ContainerRedis();
        String queryCode = """
                select airport_code,
                count (\"Вылетевший рейс\") as \"Кол-во вылетевших рейсов\",
                sum (\"Кол-во пассажиров в рейсе\") as \"Кол-во вылетевших пассажиров\",
                sum(\"Средняя стоимость билета\")/count (\"Вылетевший рейс\") as \"Средняя стоимость вылета\"
                from (
                select a.airport_code, departure_airport, f.flight_id as \"Вылетевший рейс\", count (t.ticket_no) as \"Кол-во пассажиров в рейсе\",  round (avg (amount), 2) as \"Средняя стоимость билета\"
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
            PreparedStatement preparedStatement = conn.prepareStatement(queryCode);
            preparedStatement.setString(1, code);
            resultAirport = preparedStatement.executeQuery();
            if (resultAirport.next()) {
                AirportInfo airportInfo = new AirportInfo(resultAirport.getString("airport_code"),
                        resultAirport.getString("Кол-во вылетевших рейсов"),
                        resultAirport.getString("Кол-во вылетевших пассажиров"),
                        resultAirport.getString("Средняя стоимость вылета"));
                Gson gson = new Gson();
                String airportInfoJson = gson.toJson(airportInfo);
                String key = resultAirport.getString("airport_code");
                String value = airportInfoJson;
                container.setJedis(key, value);
                return airportInfoJson;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
