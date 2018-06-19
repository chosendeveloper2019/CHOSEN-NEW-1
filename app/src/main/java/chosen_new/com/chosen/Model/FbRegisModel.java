package chosen_new.com.chosen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FbRegisModel {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_fullname")
    @Expose
    private String userFullname;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_pw")
    @Expose
    private String userPw;
    @SerializedName("password_salt")
    @Expose
    private String passwordSalt;
    @SerializedName("user_permit")
    @Expose
    private String userPermit;
    @SerializedName("user_createdate")
    @Expose
    private String userCreatedate;
    @SerializedName("pole_owner_id")
    @Expose
    private String poleOwnerId;
    @SerializedName("card_holder_id")
    @Expose
    private String cardHolderId;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("streetnumber")
    @Expose
    private String streetnumber;
    @SerializedName("postalcode")
    @Expose
    private String postalcode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("BIC")
    @Expose
    private String bIC;
    @SerializedName("IBAN")
    @Expose
    private String iBAN;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("Roles")
    @Expose
    private String roles;
    @SerializedName("remember_token")
    @Expose
    private Object rememberToken;
    @SerializedName("emailConfirm")
    @Expose
    private Object emailConfirm;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getUserPermit() {
        return userPermit;
    }

    public void setUserPermit(String userPermit) {
        this.userPermit = userPermit;
    }

    public String getUserCreatedate() {
        return userCreatedate;
    }

    public void setUserCreatedate(String userCreatedate) {
        this.userCreatedate = userCreatedate;
    }

    public String getPoleOwnerId() {
        return poleOwnerId;
    }

    public void setPoleOwnerId(String poleOwnerId) {
        this.poleOwnerId = poleOwnerId;
    }

    public String getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(String cardHolderId) {
        this.cardHolderId = cardHolderId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBIC() {
        return bIC;
    }

    public void setBIC(String bIC) {
        this.bIC = bIC;
    }

    public String getIBAN() {
        return iBAN;
    }

    public void setIBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Object getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(Object rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Object getEmailConfirm() {
        return emailConfirm;
    }

    public void setEmailConfirm(Object emailConfirm) {
        this.emailConfirm = emailConfirm;
    }
}
