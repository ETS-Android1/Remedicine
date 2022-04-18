package com.iti.mad42.remedicine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class MedicationPojo implements Serializable {
    String name;
    int formIndex;
    String strength;
    int strengthUnitIndex;
    String reason;
    String instructions;
    int recurrencePerDayIndex;
    List<MedicineDose> medDoseReminders;
    int  recurrencePerWeekIndex;
    long startDate;
    long endDate;
    List<String> medDays;
    int medQty;
    int reminderMedQtyLeft;
    long refillReminderTimeInMilliSec;
    boolean isActive;
    List<MedState> medState;

    public MedicationPojo() {
    }

    public MedicationPojo(String name, int formIndex, String strength, int strengthUnitIndex, String reason, String instructions, int recurrencePerDayIndex, List<MedicineDose> medDoseReminders, int recurrencePerWeekIndex, long startDate, long endDate, List<String> medDays, int medQty, int reminderMedQtyLeft, long refillReminderTimeInMilliSec, boolean isActive, List<MedState> medState) {
        this.name = name;
        this.formIndex = formIndex;
        this.strength = strength;
        this.strengthUnitIndex = strengthUnitIndex;
        this.reason = reason;
        this.instructions = instructions;
        this.recurrencePerDayIndex = recurrencePerDayIndex;
        this.medDoseReminders = medDoseReminders;
        this.recurrencePerWeekIndex = recurrencePerWeekIndex;
        this.startDate = startDate;
        this.endDate = endDate;
        this.medDays = medDays;
        this.medQty = medQty;
        this.reminderMedQtyLeft = reminderMedQtyLeft;
        this.refillReminderTimeInMilliSec = refillReminderTimeInMilliSec;
        this.isActive = isActive;
        this.medState = medState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFormIndex() {
        return formIndex;
    }

    public void setFormIndex(int formIndex) {
        this.formIndex = formIndex;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getStrengthUnitIndex() {
        return strengthUnitIndex;
    }

    public void setStrengthUnitIndex(int strengthUnitIndex) {
        this.strengthUnitIndex = strengthUnitIndex;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getRecurrencePerDayIndex() {
        return recurrencePerDayIndex;
    }

    public void setRecurrencePerDayIndex(int recurrencePerDayIndex) {
        this.recurrencePerDayIndex = recurrencePerDayIndex;
    }

    public List<MedicineDose> getMedDoseReminders() {
        return medDoseReminders;
    }

    public void setMedDoseReminders(List<MedicineDose> medDoseReminders) {
        this.medDoseReminders = medDoseReminders;
    }

    public int getRecurrencePerWeekIndex() {
        return recurrencePerWeekIndex;
    }

    public void setRecurrencePerWeekIndex(int recurrencePerWeekIndex) {
        this.recurrencePerWeekIndex = recurrencePerWeekIndex;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public List<String> getMedDays() {
        return medDays;
    }

    public void setMedDays(List<String> medDays) {
        this.medDays = medDays;
    }

    public int getMedQty() {
        return medQty;
    }

    public void setMedQty(int medQty) {
        this.medQty = medQty;
    }

    public int getReminderMedQtyLeft() {
        return reminderMedQtyLeft;
    }

    public void setReminderMedQtyLeft(int reminderMedQtyLeft) {
        this.reminderMedQtyLeft = reminderMedQtyLeft;
    }

    public long getRefillReminderTimeInMilliSec() {
        return refillReminderTimeInMilliSec;
    }

    public void setRefillReminderTimeInMilliSec(long refillReminderTimeInMilliSec) {
        this.refillReminderTimeInMilliSec = refillReminderTimeInMilliSec;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<MedState> getMedState() {
        return medState;
    }

    public void setMedState(List<MedState> medState) {
        this.medState = medState;
    }

    @Override
    public String toString() {
        return "MedicationPojo{" +
                "name='" + name + '\'' +
                ", formIndex=" + formIndex +
                ", strength='" + strength + '\'' +
                ", strengthUnitIndex=" + strengthUnitIndex +
                ", reason='" + reason + '\'' +
                ", instructions='" + instructions + '\'' +
                ", recurrencePerDayIndex=" + recurrencePerDayIndex +
                ", medDoseReminders=" + medDoseReminders +
                ", recurrencePerWeekIndex=" + recurrencePerWeekIndex +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", medDays=" + medDays +
                ", medQty=" + medQty +
                ", reminderMedQtyLeft=" + reminderMedQtyLeft +
                ", refillReminderTimeInMilliSec=" + refillReminderTimeInMilliSec +
                ", isActive=" + isActive +
                ", medState=" + medState +
                '}';
    }
//    protected MedicationPojo(Parcel in) {
//        name = in.readString();
//        formIndex = in.readInt();
//        strength = in.readString();
//        strengthUnitIndex=in.readInt();
//        reason =in.readString();
//        instructions=in.readString();
//        recurrencePerDayIndex=in.readInt();
//        medDoseReminders=in.createTypedArrayList(new Creator<MedicineDose>() {
//            @Override
//            public MedicineDose createFromParcel(Parcel parcel) {
//                return new MedicineDose(parcel);
//            }
//
//            @Override
//            public MedicineDose[] newArray(int i) {
//                return new MedicineDose[i];
//            }
//        });
//        recurrencePerWeekIndex = in.readInt();
//        startDate = in.readLong();
//        endDate = in.readLong();
//        medDays = in.createStringArrayList();
//        medQty = in.readInt();
//        reminderMedQtyLeft = in.readInt();
//        refillReminderTimeInMilliSec = in.readLong();
//        isActive = in.readByte() != 0;
//        medState = in.createTypedArrayList(new Creator<MedState>() {
//            @Override
//            public MedState createFromParcel(Parcel parcel) {
//                return new MedState(in);
//            }
//
//            @Override
//            public MedState[] newArray(int i) {
//                return new MedState[i];
//            }
//        });
//    }



}