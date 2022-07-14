import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.IOException;

public class AirportInfo {

    public String airportCode;
    public String flightsOut;
    public String passengersOut;
    public String averagePrice;


    public void SourseCheck(String airportCode, String flightsOut, String passengersOut, String averagePrice) {
        this.airportCode = airportCode;
        this.flightsOut = flightsOut;
        this.passengersOut = passengersOut;
        this.averagePrice = averagePrice;
    }

    public String writeJson() throws IOException {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("airportCode", airportCode);
        rootObject.addProperty("flightsOut", flightsOut);
        rootObject.addProperty("passengersOut", passengersOut);
        rootObject.addProperty("averagePrice", averagePrice);
        Gson gson = new Gson();
        String json = gson.toJson(rootObject);
        return json;
    }
/*
    @Override
    public String toString() {
        return "AirportInfo{" +
                "airportCode = '" + airportCode + '\'' +
                ", flightsOut = '" + flightsOut + '\'' +
                ", passengersOut = '" + passengersOut + '\'' +
                ", averagePrice = '" + averagePrice + '\'' + '}';

    }

 */
}
