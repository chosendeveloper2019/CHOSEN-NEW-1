package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCardModel {

    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("state")
    @Expose
    private String state;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
