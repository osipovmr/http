import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

public class TicketInfo {

    public String ticketNo;
    public String passangerName;
    public String fareConditions;
    public String status;
    public String city;

    public void SourseCheck(String ticketNo, String passangerName, String fareConditions, String status, String city) {
        this.ticketNo = ticketNo;
        this.passangerName = passangerName;
        this.fareConditions = fareConditions;
        this.status = status;
        this.city = city;
    }
    public String writeJson() throws IOException {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("ticketNo", ticketNo);
        rootObject.addProperty("passangerName", passangerName);
        rootObject.addProperty("fareConditions", fareConditions);
        rootObject.addProperty("status", status);
        rootObject.addProperty("city", city);
        Gson gson = new Gson();
        String json = gson.toJson(rootObject);
        return json;
    }


    /*
    @Override
    public String toString() {
        return "TicketInfo{" +
                "ticketNo = '" + ticketNo + '\'' +
                ", airportName = '" + passangerName + '\'' +
                ", fareConditions = '" + fareConditions + '\'' +
                ", status = '" + status + '\'' +
                ", city = '" + city + '\'' + '}';

    }

     */
}
