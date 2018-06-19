package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInvoiceModel {

    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("pole_id")
    @Expose
    private String poleId;
    @SerializedName("start_charge")
    @Expose
    private String startCharge;
    @SerializedName("end_charge")
    @Expose
    private String endCharge;
    @SerializedName("total_charge")
    @Expose
    private String totalCharge;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("vat")
    @Expose
    private String vat;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dates")
    @Expose
    private String dates;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPoleId() {
        return poleId;
    }

    public void setPoleId(String poleId) {
        this.poleId = poleId;
    }

    public String getStartCharge() {
        return startCharge;
    }

    public void setStartCharge(String startCharge) {
        this.startCharge = startCharge;
    }

    public String getEndCharge() {
        return endCharge;
    }

    public void setEndCharge(String endCharge) {
        this.endCharge = endCharge;
    }

    public String getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(String totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
}
