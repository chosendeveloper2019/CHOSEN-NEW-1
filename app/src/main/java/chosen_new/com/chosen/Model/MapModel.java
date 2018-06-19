package chosen_new.com.chosen.Model;

import java.io.Serializable;

public class MapModel implements Serializable{

    private double lat;
    private double lon;
    private String pole_id;
    private String user_fullname;
    private int user_id;
    private int color;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon){
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor(){ return color; }

    public void setPole_id(String pole_id) {
        this.pole_id = pole_id;
    }

    public String getPole_id() {
        return pole_id;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }
}
