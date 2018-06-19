package chosen_new.com.chosen.Model.ResPaypal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultPaymentModel {
    @SerializedName("intent")
    @Expose
    private String intent;
    @SerializedName("payer")
    @Expose
    private Payer payer;
    @SerializedName("redirect_urls")
    @Expose
    private RedirectUrls redirectUrls;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public RedirectUrls getRedirectUrls() {
        return redirectUrls;
    }

    public void setRedirectUrls(RedirectUrls redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
