package models;

import java.io.Serializable;

public class ConnectAPIModel<T> implements Serializable {

    int is_patient;


    Author<T> author;
    Navigator<T> navigator;
    Patient<T> patient;
    Organisation<T> organisation;


    String id,first_name,last_name,email,mobile_no,gender,country_code,headerText;
    String time_zone,shift_start_time,shift_end_time,emergency_number;
    boolean isFromPatient;


    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public boolean isFromPatient() {
        return isFromPatient;
    }

    public void setFromPatient(boolean fromPatient) {
        isFromPatient = fromPatient;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getShift_start_time() {
        return shift_start_time;
    }

    public void setShift_start_time(String shift_start_time) {
        this.shift_start_time = shift_start_time;
    }

    public String getShift_end_time() {
        return shift_end_time;
    }

    public void setShift_end_time(String shift_end_time) {
        this.shift_end_time = shift_end_time;
    }

    public String getEmergency_number() {
        return emergency_number;
    }

    public void setEmergency_number(String emergency_number) {
        this.emergency_number = emergency_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public class Author<T>{

        String id,first_name,last_name,email,mobile_no,gender,country_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    public class Navigator<T>{

        String id,first_name,last_name,email,mobile_no,gender,country_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    public class Patient<T>{

        String id,first_name,last_name,email,mobile_no,gender,country_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    public class Organisation<T>{

        String id,time_zone,shift_start_time,shift_end_time,emergency_number;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime_zone() {
            return time_zone;
        }

        public void setTime_zone(String time_zone) {
            this.time_zone = time_zone;
        }

        public String getShift_start_time() {
            return shift_start_time;
        }

        public void setShift_start_time(String shift_start_time) {
            this.shift_start_time = shift_start_time;
        }

        public String getShift_end_time() {
            return shift_end_time;
        }

        public void setShift_end_time(String shift_end_time) {
            this.shift_end_time = shift_end_time;
        }

        public String getEmergency_number() {
            return emergency_number;
        }

        public void setEmergency_number(String emergency_number) {
            this.emergency_number = emergency_number;
        }
    }

    public int getIs_patient() {
        return is_patient;
    }

    public void setIs_patient(int is_patient) {
        this.is_patient = is_patient;
    }

    public Author<T> getAuthor() {
        return author;
    }

    public void setAuthor(Author<T> author) {
        this.author = author;
    }

    public Navigator<T> getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator<T> navigator) {
        this.navigator = navigator;
    }

    public Patient<T> getPatient() {
        return patient;
    }

    public void setPatient(Patient<T> patient) {
        this.patient = patient;
    }

    public Organisation<T> getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation<T> organisation) {
        this.organisation = organisation;
    }
}
