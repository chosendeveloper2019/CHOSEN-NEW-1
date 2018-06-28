package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceCardModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("payments_id")
    @Expose
    private String paymentsId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("pole_id")
    @Expose
    private String poleId;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("net_price")
    @Expose
    private Double netPrice;
    @SerializedName("vat")
    @Expose
    private Double vat;
    @SerializedName("total_price")
    @Expose
    private Double totalPrice;
    @SerializedName("payment_status")
    @Expose
    private Boolean paymentStatus;
    @SerializedName("payment_detail")
    @Expose
    private String paymentDetail;
    @SerializedName("charge_time")
    @Expose
    private Integer chargeTime;
    @SerializedName("transactionid")
    @Expose
    private String transactionid;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPoleId() {
        return poleId;
    }

    public void setPoleId(String poleId) {
        this.poleId = poleId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(Double netPrice) {
        this.netPrice = netPrice;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public Integer getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Integer chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}
