package chosen_new.com.chosen.Api;

import java.util.List;

import chosen_new.com.chosen.Model.ProfileModel;
import chosen_new.com.chosen.Model.UploadImageModel;
import okhttp3.ResponseBody;

public interface CallbackuploadpicListenner {

    public void onResponse(UploadImageModel res);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
