package chosen.com.chosen.Api;

import java.util.List;

import chosen.com.chosen.Model.LoginModel;
import chosen.com.chosen.Model.PaymentModel;
import okhttp3.ResponseBody;

public interface CallbackPaymentListener {

    public void onResponse(List<PaymentModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
