import java.util.ArrayList;

public class AirportInfo {
    public String airportCode;
    public String airportName;
    public String city;
    public String longitude;
    public String latitude;
    public String timezone;
    ArrayList<String> list = new ArrayList<>();

    public void AirportsInfo(String airportCode, String airportName, String city, String longitude, String latitude, String timezone) {
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "AirportInfo{" +
                "airportCode = '" + airportCode + '\'' +
                ", airportName = '" + airportName + '\'' +
                ", city = '" + city + '\'' +
                ", longitude = '" + longitude + '\'' +
                ", latitude = '" + latitude + '\'' +
                ", timezone = '" + timezone + '\'' + '}' + "<br>";

    }
}
