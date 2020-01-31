package devcom.marcos.triangularpos;


public class Beacon {
    private final String macAddress;
    private final String name;
    private float lat, lon, dist;

    public Beacon(String add, String name){
        macAddress = add;
        this.name = name;
    }

    //Getters
    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public float getDist() {
        return dist;
    }

    //Setters
    public void setLatLon(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setDist(float dist) {
        this.dist = dist;
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
