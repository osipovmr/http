import java.util.ArrayList;

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

    @Override
    public String toString() {
        return "AirportInfo{" +
                "airportCode = '" + airportCode + '\'' +
                ", flightsOut = '" + flightsOut + '\'' +
                ", passengersOut = '" + passengersOut + '\'' +
                ", averagePrice = '" + averagePrice + '\'' + '}';

    }
}
