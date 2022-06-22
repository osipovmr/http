public class TicketInfo {

    public String ticketNo;
    public String passangerName;
    public String fareConditions;
    public String status;
    public String city;


    public void TicketInfo(String ticketNo, String passangerName, String fareConditions, String status, String city) {
        this.ticketNo = ticketNo;
        this.passangerName = passangerName;
        this.fareConditions = fareConditions;
        this.status = status;
        this.city = city;
    }

    @Override
    public String toString() {
        return "TicketInfo{" +
                "ticketNo = '" + ticketNo + '\'' +
                ", airportName = '" + passangerName + '\'' +
                ", fareConditions = '" + fareConditions + '\'' +
                ", status = '" + status + '\'' +
                ", city = '" + city + '\'' + '}' + "<br>";

    }
}