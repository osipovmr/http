import java.sql.SQLException;

import static spark.Spark.*;

public class Main {

    public static ContainerRedis container = new ContainerRedis();

    public static void main(String[] args) throws SQLException {
        System.out.println(DataSourse.getConnection());
        System.out.println(PostgresData.connection());
        System.out.println(container.redisTester());

        get("/hello", (req, res) -> DataSourse.getConnection());
        get("/ticket", (request, response) -> {
            String ticketNo;
            String param = request.queryParams("no");
            ticketNo = param;
            return PostgresData.ticket(ticketNo);});

        get("/airport", (request, response) -> {
            String code;
            String param = request.queryParams("code");
            code = param;
            return PostgresData.airport(code);});
    }
}
