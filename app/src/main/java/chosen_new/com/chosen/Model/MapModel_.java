package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapModel_ {

    @SerializedName("pole_id")
    @Expose
    private String poleId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("user_fullname")
    @Expose
    private String userFullname;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("color")
    @Expose
    private Integer color;

    public String getPoleId() {
        return poleId;
    }

    public void setPoleId(String poleId) {
        this.poleId = poleId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
