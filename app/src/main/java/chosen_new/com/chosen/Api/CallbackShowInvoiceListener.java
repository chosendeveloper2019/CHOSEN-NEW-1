package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.PaymentInvoiceModel;
import chosen_new.com.chosen.Model.PaymentModel;
import okhttp3.ResponseBody;

public interface CallbackShowInvoiceListener {

    public void onResponse(List<PaymentInvoiceModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
