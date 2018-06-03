package chosen.com.chosen.Model;

public class ReportModel {
    private int transaction_id;
    private String contract_id;
    private String card_id;
    private String pole_id;
    private String meterstartdate;
    private String metereinddate;
    private String Total;
    private String metereind;
    private String rates;
    private String price;
    private String usages;
    private String sumary;
    private String startdate;
    private String enddate;
    private String name;

    public ReportModel(){ }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public void setPole_id(String pole_id) {
        this.pole_id = pole_id;
    }

    public void setMeterstartdate(String meterstartdate) {
        this.meterstartdate = meterstartdate;
    }

    public void setMetereinddate(String metereinddate) {
        this.metereinddate = metereinddate;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public void setMetereind(String metereind) {
        this.metereind = metereind;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUsages(String usages) {
        this.usages = usages;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public String getPole_id() {
        return pole_id;
    }

    public String getMeterstartdate() {
        return meterstartdate;
    }

    public String getMetereinddate() {
        return metereinddate;
    }

    public String getTotal() {
        return Total;
    }

    public String getMetereind() {
        return metereind;
    }

    public String getRates() {
        return rates;
    }

    public String getPrice() {
        return price;
    }

    public String getUsages() {
        return usages;
    }

    public String getSumary() {
        return sumary;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getName() {
        return name;
    }
}
