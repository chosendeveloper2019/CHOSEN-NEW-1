package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.FbRegisModel;
import chosen_new.com.chosen.Model.RegisterModel;
import okhttp3.ResponseBody;

public interface CallbackRegisListener {

    public void onResponse(RegisterModel res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
