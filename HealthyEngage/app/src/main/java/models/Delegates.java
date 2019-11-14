package models;

public class Delegates {

    String id,user_id;
    User user;
    String first_name,last_name,patient_id,delegate_id,relationship_category_id,mobile_no,country_code;
    boolean is_deleted,is_patient,is_hipaa_signed;


    public boolean isIs_hipaa_signed() {
        return is_hipaa_signed;
    }

    public void setIs_hipaa_signed(boolean is_hipaa_signed) {
        this.is_hipaa_signed = is_hipaa_signed;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean isIs_patient() {
        return is_patient;
    }

    public void setIs_patient(boolean is_patient) {
        this.is_patient = is_patient;
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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User{
        String id,email,mobile_no,gender,country_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }
    }
}
