package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.ChargeModel;
import okhttp3.ResponseBody;

public interface CallbackStatusChageListener {

    public void onResponse(List<ChargeModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
