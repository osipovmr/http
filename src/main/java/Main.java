import static spark.Spark.*;

public class Main {
    //закидываем все классы
    public static ContainerRedis container = new ContainerRedis();

    public static void main(String[] args) {
        System.out.println(PostgresData.connection());
        System.out.println(container.redisTester());
        System.out.println(PostgresData.airport());
        System.out.println(PostgresData.ticket());

        get("/hello", (req, res) -> PostgresData.connection());
        get("/ticket", (req, res) -> PostgresData.ticket() );
        get("/airport", (req, res) -> PostgresData.airport());

    }

}
