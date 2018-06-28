package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChargeModel {


    @SerializedName("charging")
    @Expose
    private String charging;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("pole_id")
    @Expose
    private String poleId;
    @SerializedName("startcharge")
    @Expose
    private String startcharge;
    @SerializedName("endcharge")
    @Expose
    private String endcharge;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("khw")
    @Expose
    private String khw;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("invoice_code")
    @Expose
    private String invoiceCode;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("chargetime")
    @Expose
    private String chargetime;

    public String getCharging() {
        return charging;
    }

    public void setCharging(String charging) {
        this.charging = charging;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPoleId() {
        return poleId;
    }

    public void setPoleId(String poleId) {
        this.poleId = poleId;
    }

    public String getStartcharge() {
        return startcharge;
    }

    public void setStartcharge(String startcharge) {
        this.startcharge = startcharge;
    }

    public String getEndcharge() {
        return endcharge;
    }

    public void setEndcharge(String endcharge) {
        this.endcharge = endcharge;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getKhw() {
        return khw;
    }

    public void setKhw(String khw) {
        this.khw = khw;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChargetime() {
        return chargetime;
    }

    public void setChargetime(String chargetime) {
        this.chargetime = chargetime;
    }
}
