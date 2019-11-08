package models;

import java.util.List;

public class APIResponseModels<T> {




    private List<T> careplan;
    private List<T> error;
    String day,mobileNo,country_code,mobile_no,token,careplanId,patientId,delegateId,lastSyncDate;
    int status,sync;
    AccessToken<T> accessToken;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public List<T> getError() {
        return error;
    }

    public void setError(List<T> error) {
        this.error = error;
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
