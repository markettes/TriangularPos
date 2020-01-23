package devcom.marcos.triangularpos;


public class Beacon {
    private final String macAddress;
    private final String name;
    private double lat, lon;

    public Beacon(String add, String name, double lat, double lon){
        macAddress = add;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    //Getters
    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    //Setters
    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    //Otros

    @Override
    public String toString() {
        return "Beacon{" +
                "macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

}
