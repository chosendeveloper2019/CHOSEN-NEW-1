package chosen_new.com.chosen.Model.ResPaypal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedirectUrls {
    @SerializedName("return_url")
    @Expose
    private String returnUrl;
    @SerializedName("cancel_url")
    @Expose
    private String cancelUrl;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
