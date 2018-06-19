package chosen_new.com.chosen.Util;

import okhttp3.MediaType;

public class UrlUtil {
      public static final String URL = "http://app.chosenenergy.co.th/";
      public static final String URLPAYMENT = "http://paypal.chosenenergy.co.th/";
//    public static final String URL = "http://maxosx.ddns.net:81/";
   // public static final String URL = "http://maxosx.thddns.net:5220/";
    public static final String URL_LOGIN = URL + "/Auth/Login";
    public static final String URL_HOME = URL + "/Home/map";
    public static final String URL_CARD = URL + "/Card";
    public static final String URL_POLE = URL + "/Pole";
    public static final String URL_REPORT = URL + "/Report/report";
    public static final String URL_INVOICE = URL + "/payment";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
