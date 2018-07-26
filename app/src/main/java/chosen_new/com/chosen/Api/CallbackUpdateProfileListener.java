package chosen_new.com.chosen.Api;

import chosen_new.com.chosen.Model.UpdateProfileModel;
import chosen_new.com.chosen.Model.UploadImageModel;
import okhttp3.ResponseBody;

public interface CallbackUpdateProfileListener {

    public void onResponse(UpdateProfileModel res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
