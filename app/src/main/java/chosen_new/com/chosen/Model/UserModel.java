package chosen_new.com.chosen.Model;

import java.io.Serializable;

public class UserModel implements Serializable{
    private int user_id;
    private String user_fullname;
    private String user_name;
    private String user_email;
    private String user_pw;
    private String password_salt;
    private String user_permit;
    private String user_createdate;
    private String pole_owner_id;
    private String card_holder_id;
    private String street;
    private String streetnumber;
    private String postalcode;
    private String city;
    private String company;
    private String mobile;
    private String fax;
    private String BIC;
    private String IBAN;
    private String reference;
    private String Roles;

    public UserModel(){ }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public String getPassword_salt() {
        return password_salt;
    }

    public void setUser_permit(String user_permit) {
        this.user_permit = user_permit;
    }

    public String getUser_permit() {
        return user_permit;
    }

    public void setUser_createdate(String user_createdate) {
        this.user_createdate = user_createdate;
    }

    public String getUser_createdate() {
        return user_createdate;
    }

    public void setPole_owner_id(String pole_owner_id) {
        this.pole_owner_id = pole_owner_id;
    }

    public String getPole_owner_id() {
        return pole_owner_id;
    }

    public void setCard_holder_id(String card_holder_id) {
        this.card_holder_id = card_holder_id;
    }

    public String getCard_holder_id() {
        return card_holder_id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public String getBIC() {
        return BIC;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setRoles(String roles) {
        Roles = roles;
    }

    public String getRoles() {
        return Roles;
    }
}
