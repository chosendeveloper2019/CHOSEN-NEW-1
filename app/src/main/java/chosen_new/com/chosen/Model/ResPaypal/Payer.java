package chosen_new.com.chosen.Model.ResPaypal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payer {
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
