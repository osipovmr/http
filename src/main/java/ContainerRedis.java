import redis.clients.jedis.Jedis;

public class ContainerRedis {
    static Jedis jedis = new Jedis("127.0.0.1");

    public void setJedis(String x, String y) {
       jedis.set(x,y);
    }

    public String getJedis(String x) {
        return jedis.get(x);
    }

    public String redisTester() {
        String a = "Client is running " + jedis.ping();
        return a;
    }

}
