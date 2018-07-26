package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInvoiceModel {

    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("payments_id")
    @Expose
    private String paymentsId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("pole_id")
    @Expose
    private String poleId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("net_price")
    @Expose
    private String netPrice;
    @SerializedName("vat")
    @Expose
    private String vat;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("charge_time")
    @Expose
    private String chargeTime;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("kwh")
    @Expose
    private String kwh;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentsId() {
        return paymentsId;
    }

    public void setPaymentsId(String paymentsId) {
        this.paymentsId = paymentsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getKwh() {
        return kwh;
    }

    public void setKwh(String kwh) {
        this.kwh = kwh;
    }
}
