package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.CarModel;
import chosen_new.com.chosen.Model.SelectCarModel;
import okhttp3.ResponseBody;

public interface CallbackSelectCarListener {

    public void onResponse(SelectCarModel res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
