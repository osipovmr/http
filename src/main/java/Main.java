import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static spark.Spark.*;

public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static ContainerRedis container = new ContainerRedis();

    public static void main(String[] args) throws SQLException {
        logger.info("Логгер работает!)");


        get("/hello", (req, res) -> DataSourse.getConnection());
        get("/ticket", (request, response) -> {
            String ticketNo;
            String param = request.queryParams("no");
            ticketNo = param;
            if (container.getJedis(param) != null) {
                logger.info("Данные из redis");
                return container.getJedis(param);
            } else {
                logger.info("Данные внешней базы");
                return PostgresData.ticket(ticketNo);
            }
        });

        get("/airport", (request, response) -> {
            String code;
            String param = request.queryParams("code");
            code = param;
            if (container.getJedis(param) != null) {
                System.out.println("Данные из redis");
                return container.getJedis(param);
            } else {
                System.out.println("Данные из внешней базы");
                return PostgresData.airport(code);
            }
        });
    }
}

