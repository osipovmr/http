import static spark.Spark.*;

import java.sql.*;

public class HelloWorld {
    //закидываем все классы
    public static Statement statement = null;
    public static Container container = new Container();
    public static AirportInfo airportInfo = new AirportInfo();
    public static TicketInfo ticketInfo = new TicketInfo();
    public static CodeInfo codeInfo = new CodeInfo();
    public static String check = null;
    static ResultSet resultAirport;
    static ResultSet resultTicket;
    static ResultSet resultCode;
    public static String queryAirport = "select * from bookings.airports"; // вызов всей таблицы
    //1. для конкретного номера билета (ticket_no) узнать имя пассажира, тариф, статус, город вылета.
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
    //2. для конкретного кода аэропорта (airport_code) узнать количество вылетов из этого аэропорта,
    // среднюю стоимость перелета из этого аэропорта, количество пассажиров, вылетевших из аэропорта
    public static String queryCode = "select airport_code, " +
            "count (\"Вылетевший рейс\") as \"Кол-во вылетевших рейсов\"," +
            "sum (\"Кол-во пассажиров в рейсе\") as \"Кол-во вылетевших пассажиров\"," +
            "sum(\"Средняя стоимость билета\")/count (\"Вылетевший рейс\") as \"Средняя стоимость вылета\"\n" +
            "from (\n" +
            "select a.airport_code, departure_airport, f.flight_id as \"Вылетевший рейс\", count (t.ticket_no) as \"Кол-во пассажиров в рейсе\",  round (avg (amount), 2) as \"Средняя стоимость билета\"\n" +
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
            check = "Ошибка SQL!";
        }
        return check;
    }

    public static void airport() {
        try {
            resultAirport = statement.executeQuery(queryAirport);
            while (resultAirport.next()) {
                airportInfo.AirportsInfo(resultAirport.getString("airport_code"),
                        resultAirport.getString("airport_name"),
                        resultAirport.getString("city"),
                        resultAirport.getString("longitude"),
                        resultAirport.getString("latitude"),
                        resultAirport.getString("timezone")
                        );
                airportInfo.add(airportInfo.toString());
                String key = resultAirport.getString("airport_code");
                String value = airportInfo.toString();
                container.setJedis(key, value);
            }
            System.out.println("База airport собрана!");
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
                ticketInfo.TicketInfo(resultTicket.getString("ticket_no"),
                        resultTicket.getString("passenger_name"),
                        resultTicket.getString("fare_conditions"),
                        resultTicket.getString("status"),
                        resultTicket.getString("city"));
                ticketInfo.add(ticketInfo.toString());
                String key = resultTicket.getString("ticket_no");
                String value = ticketInfo.toString();
                container.setJedis(key,value);
            }
            System.out.println("База ticket собрана!");
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
                codeInfo.CodeInfo(resultCode.getString("airport_code"),
                        resultCode.getString("Кол-во вылетевших рейсов"),
                        resultCode.getString("Кол-во вылетевших пассажиров"),
                        resultCode.getString("Средняя стоимость вылета"));
                codeInfo.add(codeInfo.toString());
                String key = resultCode.getString("airport_code");
                String value = codeInfo.toString();
                container.setJedis(key,value);
            }
            System.out.println("База code собрана!");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(connection());
        System.out.println(container.redisTester());
        container.setJedis("Privet", "Hello"); //проверка
        System.out.println(container.getJedis("Privet")); //проверка
        System.out.println(container.getJedis("NOZ"));
        System.out.println(container.getJedis("0005432000883"));
        System.out.println(container.getJedis("TOF"));
        airport();
        ticket();
        code();

        for (int i = 0; i < airportInfo.list.size(); i++) {
            System.out.println(airportInfo.list.get(i));
        }
        for (int i = 0; i < ticketInfo.list.size(); i++) {
            System.out.println(ticketInfo.list.get(i));
        }
        for (int i = 0; i < codeInfo.list.size(); i++) {
            System.out.println(codeInfo.list.get(i));
        }
        get("/hello", (req, res) -> check);
        get("/airport", (req, res) -> airportInfo.getAirports());
        get("/ticket", (req, res) -> ticketInfo.getTicket());
        get("/code", (req, res) -> codeInfo.getCode());
    }

}
