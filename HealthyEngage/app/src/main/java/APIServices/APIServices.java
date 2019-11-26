package APIServices;

import models.APIResponseModels;
import models.CarePlanModels;
import models.ConnectAPIModel;
import models.Delegates;
import models.PatientMessageAPIModel;
import models.PatientMessageModels;
import models.UserModel;
import models.UserVerifyModel;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PATCH;
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
                                                         @Path("id") String id,
                                                         @Body ConnectAPIModel apiModel);




    @POST("api/Users/patient/verifyMobile")
    Call<UserVerifyModel> patientVerifyMobile(@Body UserVerifyModel userVerifyModel);

    @POST("api/Users/patient/verify")
    Call<APIResponseModels<CarePlanModels>> checkPatientVerify(@Body UserVerifyModel userVerifyModel);



    @POST("api/PatientInterventions")
    Call<APIResponseModels> updateCarePlanIntervention(@Header("Authorization") String value,
                                                       @Body CarePlanModels.CarePlanIntervention.InterventionFrequency carePlanModels);

    @POST("api/PatientAssessments")
    Call<APIResponseModels> updateCarePlanAssessment(@Header("Authorization") String value,
                                                       @Body CarePlanModels.CarePlanAssessment patientAssessment);

    @GET("api/RelationshipCategories")
    Call<ConnectAPIModel<ConnectAPIModel>> getRelationDetails(@Header("Authorization") String value);

    @POST("api/Delegates")
    Call<APIResponseModels> addDelegate(@Header("Authorization") String value,
                                                                 @Body Delegates delegates);

    @POST("api/Delegates/update")
    Call<APIResponseModels> updateDelegate(@Header("Authorization") String value,
                                        @Body Delegates delegates);

    @GET("api/StaticContents/hipaa")
    Call<APIResponseModels> acknowledgement(@Header("Authorization") String value);


    @POST("api/Delegates/signHipaa")
    Call<APIResponseModels> submitHippa(@Header("Authorization") String value,
                                        @Body Delegates delegates);

    @GET("api/Users/{id}")
    Call<UserModel> getUserDetails(@Header("Authorization") String value,
                                   @Path("id") String id);

    @PATCH("api/Users/{id}")
    Call<UserModel> updateUserDetails(@Header("Authorization") String value,
                                      @Path("id") String id,
                                      @Body UserModel userModel);



    @POST("api/Patients/joinVideoCall")
    Call<APIResponseModels> joinVideoCall(@Header("Authorization") String value,
                                        @Body APIResponseModels apiResponseModels);


}
