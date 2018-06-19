package chosen_new.com.chosen.Model;

import android.widget.Button;

import java.io.Serializable;

public class InvoiceModel implements Serializable{
    private String no;
    private String card_id;
    private String total_price;
    private String invoice;
    public Button btnView;

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public void  setInvoice(String invoice){ this.invoice = invoice; }

    public void setTotal(String total_price) { this.total_price = total_price;}

    public String getCard_id() {
        return card_id;
    }

    public  String getInvoice() { return  invoice; }

    public  String getTotal(){ return total_price; }
}
