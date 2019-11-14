package models;

import java.util.List;

public class APIResponseModels<T> {




    private List<T> careplan;
    private  Error error;
    String day,mobileNo,country_code,mobile_no,token,careplanId,patientId,delegateId,lastSyncDate;
    boolean sync,status,is_hipaa_signed;
    Delegates delegates;
    String first_name,last_name,patient_id,delegate_id,relationship_category_id,name,type,content;
    boolean is_active;

    public boolean isIs_hipaa_signed() {
        return is_hipaa_signed;
    }

    public void setIs_hipaa_signed(boolean is_hipaa_signed) {
        this.is_hipaa_signed = is_hipaa_signed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDelegate_id() {
        return delegate_id;
    }

    public void setDelegate_id(String delegate_id) {
        this.delegate_id = delegate_id;
    }

    public String getRelationship_category_id() {
        return relationship_category_id;
    }

    public void setRelationship_category_id(String relationship_category_id) {
        this.relationship_category_id = relationship_category_id;
    }

    public Delegates getDelegates() {
        return delegates;
    }

    public void setDelegates(Delegates delegates) {
        this.delegates = delegates;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    AccessToken<T> accessToken;



    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
    int statusCode;
    String name,message;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getCareplanId() {
        return careplanId;
    }

    public void setCareplanId(String careplanId) {
        this.careplanId = careplanId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(String delegateId) {
        this.delegateId = delegateId;
    }

    public AccessToken<T> getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken<T> accessToken) {
        this.accessToken = accessToken;
    }

    public class AccessToken <T>{

        String id,created,userId;
        int ttl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }


    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }




    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public List<T> getCareplan() {
        return careplan;
    }

    public void setCareplan(List<T> careplan) {
        this.careplan = careplan;
    }

}
