package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.LoginModel;
import chosen_new.com.chosen.Model.MapModel_;
import okhttp3.ResponseBody;

public interface CallbackHomeListener  {

    public void onResponse(List<MapModel_> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
