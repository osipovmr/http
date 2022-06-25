import java.util.ArrayList;

public class CodeInfo {

    public String airportCode;
    public String flightsOut;
    public String passengersOut;
    public String averagePrice;
    ArrayList<String> list = new ArrayList<>();

    public void CodeInfo(String airportCode, String flightsOut, String passengersOut, String averagePrice) {
        this.airportCode = airportCode;
        this.flightsOut = flightsOut;
        this.passengersOut = passengersOut;
        this.averagePrice = averagePrice;

    }

    @Override
    public String toString() {
        return "CodeInfo{" +
                "airportCode = '" + airportCode + '\'' +
                ", flightsOut = '" + flightsOut + '\'' +
                ", passengersOut = '" + passengersOut + '\'' +
                ", averagePrice = '" + averagePrice + '\'' + '}' + "<br>";

    }
}
