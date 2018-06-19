package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.FbRegisModel;

import okhttp3.ResponseBody;

public interface CallbackFacebookRegis {
    public void onResponse(List<FbRegisModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
