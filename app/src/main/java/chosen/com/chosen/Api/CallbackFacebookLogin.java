package chosen.com.chosen.Api;

import java.util.List;

import chosen.com.chosen.Model.FbLoginModel;

import okhttp3.ResponseBody;

public interface CallbackFacebookLogin {

    public void onResponse(List<FbLoginModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
