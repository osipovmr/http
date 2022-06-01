import java.util.ArrayList;

public class Tickets {
    ArrayList<String> list = new ArrayList<>();
    public void add(String text) {
        list.add(text);
    }
    public ArrayList<String> getTicket() {
        return list;
    }
}
