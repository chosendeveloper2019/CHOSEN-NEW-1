package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.LoginModel;
import okhttp3.ResponseBody;

public interface CallbackLoginListener {

    public void onResponse(List<LoginModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
