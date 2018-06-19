package chosen_new.com.chosen.Model.ResPaypal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {

    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("invoice_number")
    @Expose
    private String invoiceNumber;
    @SerializedName("item_list")
    @Expose
    private ItemList itemList;
    @SerializedName("related_resources")
    @Expose
    private List<Object> relatedResources = null;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public List<Object> getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(List<Object> relatedResources) {
        this.relatedResources = relatedResources;
    }
}
