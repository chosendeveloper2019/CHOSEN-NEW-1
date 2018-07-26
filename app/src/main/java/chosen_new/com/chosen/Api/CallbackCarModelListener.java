package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.CarModel;
import chosen_new.com.chosen.Model.CardModel;
import chosen_new.com.chosen.Model.ChargeModel;
import okhttp3.ResponseBody;

public interface CallbackCarModelListener {

    public void onResponse(List<CarModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    void onFailure(Throwable t);

}
