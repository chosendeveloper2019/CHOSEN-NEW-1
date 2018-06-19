package chosen_new.com.chosen.Model.ResPaypal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {
    @Expose
    private String subtotal;
    @SerializedName("tax")
    @Expose
    private String tax;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
