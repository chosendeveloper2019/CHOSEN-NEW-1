package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.ProfileModel;
import okhttp3.ResponseBody;

public interface CallbackProfileListener {

    public void onResponse(List<ProfileModel> res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
