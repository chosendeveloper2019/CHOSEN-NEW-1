package chosen_new.com.chosen.Api;

import chosen_new.com.chosen.Model.ResultPaymentModel;
import okhttp3.ResponseBody;

public interface CallbackInvoiceListener {

    public void onResponse(ResultPaymentModel res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
