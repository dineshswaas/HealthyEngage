package APIServices;

import models.APIResponseModels;
import models.CarePlanModels;
import models.ConnectAPIModel;
import models.PatientMessageAPIModel;
import models.PatientMessageModels;
import models.UserVerifyModel;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface APIServices {

    @POST("api/CarePlans/{carePlanId}/patient/{patientId}/detail")
    Call<APIResponseModels<CarePlanModels>> getCarePlanDetails(@Header("Authorization") String value,
                                                               @Path("carePlanId") String carePlanId,
                                                               @Path("patientId") String patientId,
                                                               @Body CarePlanModels carePlanModels);


    @POST("api/PatientMessages/{patientId}")
    Call<PatientMessageAPIModel<PatientMessageModels>> getPatientMessages(@Header("Authorization") String value,
                                                                          @Path("patientId") String patientId,
                                                                          @Body PatientMessageModels models);



    @POST("api/Patients/{id}/careTakers")
    Call<ConnectAPIModel<ConnectAPIModel>> getCareTakers(@Header("Authorization") String value,
                                                         @Path("id") String id);



    @POST("api/PatientInterventions")
    Call<APIResponseModels> updateCarePlanIntervention(@Header("Authorization") String value,
                                                                       @Body CarePlanModels carePlanModels);

    @POST("api/Users/patient/verifyMobile")
    Call<UserVerifyModel> patientVerifyMobile(@Body UserVerifyModel userVerifyModel);

    @POST("api/Users/patient/verify")
    Call<APIResponseModels<CarePlanModels>> checkPatientVerify(@Body UserVerifyModel userVerifyModel);

}
