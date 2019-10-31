package APIServices;

import models.APIResponseModels;
import models.CarePlanModels;
import models.PatientMessageAPIModel;
import models.PatientMessageModels;
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

}
