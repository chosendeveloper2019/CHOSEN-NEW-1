package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCardModel {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("message")
    @Expose
    private String message;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
