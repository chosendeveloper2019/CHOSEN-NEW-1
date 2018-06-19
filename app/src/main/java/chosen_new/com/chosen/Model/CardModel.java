package chosen_new.com.chosen.Model;

import java.io.Serializable;

public class CardModel implements Serializable{
    private String no;
    private String card_id;

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_id() {
        return card_id;
    }
}
