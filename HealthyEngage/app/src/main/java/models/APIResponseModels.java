package models;

import java.util.List;

public class APIResponseModels<T> {




    private List<T> careplan;
    private String lastSyncDate;

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
