package Repositories;

import android.content.Context;

import java.util.List;

import APIServices.CarePlanServices;
import APIServices.RetrofitAPIBuilder;
import models.APIResponseModels;
import models.CarePlanModels;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class CarePlanRepository {

    private Context mContext;
    private GetCarePlanModelDetails getCarePlanModelDetails;

  public CarePlanRepository(Context context){
    this.mContext = context;
  }

public void getCarePlanDetails(CarePlanModels carePlanModels){
    if(NetworkUtils.isNetworkAvailable(mContext)){
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        CarePlanServices carePlanServices =retrofit.create(CarePlanServices.class);

        Call call =carePlanServices.getCarePlanDetails(PreferenceUtils.getAuthorizationKey(mContext),
                carePlanModels.getCarePlanId(),carePlanModels.getPatientId(),carePlanModels);
        call.enqueue(new Callback<APIResponseModels<CarePlanModels>>() {
            @Override
            public void onResponse(Response<APIResponseModels<CarePlanModels>> response, Retrofit retrofit) {
            APIResponseModels apiResponseModels =response.body();
            if(apiResponseModels != null){
                if(apiResponseModels.getCareplan() != null){
                    getCarePlanModelDetails.getCarePlanSuccess(apiResponseModels.getCareplan());
                }
            }
            }

            @Override
            public void onFailure(Throwable t) {
                getCarePlanModelDetails.getCarePlanFailure(t.getMessage());
            }
        });

    }
}


    public void setGetCarePlanDetails(GetCarePlanModelDetails getCarePlanDetails){
      this.getCarePlanModelDetails = getCarePlanDetails;

    }


public interface GetCarePlanModelDetails{

      void getCarePlanSuccess(List<CarePlanModels> carePlanModels);
      void getCarePlanFailure(String s);

}



}
