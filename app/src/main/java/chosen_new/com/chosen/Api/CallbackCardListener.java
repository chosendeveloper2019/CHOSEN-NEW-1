package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.GetCardModel;
import okhttp3.ResponseBody;

public interface CallbackCardListener {

    public void onResponse(List<GetCardModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
