package models;

import java.util.List;

public class PatientMessageAPIModel<T> {
    int totalCount;
    List<T>PatientMessages;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPatientMessages() {
        return PatientMessages;
    }

    public void setPatientMessages(List<T> patientMessages) {
        PatientMessages = patientMessages;
    }
}
