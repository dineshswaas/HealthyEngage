package models;

public class InterventionElements {

    float value;
    boolean isHad;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isCompleted() {
        return isHad;
    }

    public void setCompleted(boolean had) {
        isHad = had;
    }
}
