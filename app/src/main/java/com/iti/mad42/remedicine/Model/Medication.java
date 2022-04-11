package com.iti.mad42.remedicine.Model;

public class Medication {
    String medName;
    String medStrength;
    String medUnit;
    String medInstructions;
    String pillAmount;

    public Medication(String medName, String medStrength, String medUnit, String medInstructions, String pillAmount) {
        this.medName = medName;
        this.medStrength = medStrength;
        this.medUnit = medUnit;
        this.medInstructions = medInstructions;
        this.pillAmount = pillAmount;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedStrength() {
        return medStrength;
    }

    public void setMedStrength(String medStrength) {
        this.medStrength = medStrength;
    }

    public String getMedUnit() {
        return medUnit;
    }

    public void setMedUnit(String medUnit) {
        this.medUnit = medUnit;
    }

    public String getMedInstructions() {
        return medInstructions;
    }

    public void setMedInstructions(String medInstructions) {
        this.medInstructions = medInstructions;
    }

    public String getPillAmount() {
        return pillAmount;
    }

    public void setPillAmount(String pillAmount) {
        this.pillAmount = pillAmount;
    }
}
