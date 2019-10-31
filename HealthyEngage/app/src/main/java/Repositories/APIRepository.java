package Repositories;

import android.content.Context;

import java.util.List;

import APIServices.APIServices;
import APIServices.RetrofitAPIBuilder;
import models.APIResponseModels;
import models.CarePlanModels;
import models.PatientMessageAPIModel;
import models.PatientMessageModels;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class APIRepository {

    private Context mContext;
    private GetCarePlanModelDetails getCarePlanModelDetails;
    private GerPatientMessages gerPatientMessages;
  public APIRepository(Context context){
    this.mContext = context;
  }

public void getCarePlanDetails(CarePlanModels carePlanModels){
    if(NetworkUtils.isNetworkAvailable(mContext)){
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        APIServices carePlanServices =retrofit.create(APIServices.class);

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



    /* Patient Messages*/

    public void getPatientMessages(PatientMessageModels patientMessageModels){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.getPatientMessages(PreferenceUtils.getAuthorizationKey(mContext),
                    PreferenceUtils.getPatientId(mContext),patientMessageModels);
            call.enqueue(new Callback<PatientMessageAPIModel<CarePlanModels>>() {
                @Override
                public void onResponse(Response<PatientMessageAPIModel<CarePlanModels>> response, Retrofit retrofit) {
                    PatientMessageAPIModel apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels.getPatientMessages() != null){
                            gerPatientMessages.getPatientMessages(apiResponseModels.getPatientMessages());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    gerPatientMessages.getPatientMessagesFailure(t.getMessage());
                }
            });

        }
    }

    public void setGetPatientMessage(GerPatientMessages gerPatientMessages){
        this.gerPatientMessages = gerPatientMessages;
    }

    public interface GerPatientMessages{
        void getPatientMessages(List<PatientMessageModels> carePlanModels);
        void getPatientMessagesFailure(String s);
    }








}
