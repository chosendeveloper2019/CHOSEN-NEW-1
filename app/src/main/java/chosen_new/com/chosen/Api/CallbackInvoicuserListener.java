package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.InvoiceCardModel;
import chosen_new.com.chosen.Model.UserInvoiceModel;
import okhttp3.ResponseBody;

public interface CallbackInvoicuserListener {

    public void onResponse(List<UserInvoiceModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
