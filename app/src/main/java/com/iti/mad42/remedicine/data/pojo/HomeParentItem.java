package com.iti.mad42.remedicine.data.pojo;

import java.util.List;

public class HomeParentItem {

    private Long doseTime;
    private List<MedicationPojo> childItemList;

    public HomeParentItem() {
    }

    public HomeParentItem(Long doseTime, List<MedicationPojo> childItemList) {
        this.doseTime = doseTime;
        this.childItemList = childItemList;
    }

    public Long getDoseTime() {
        return doseTime;
    }

    public void setDoseTime(Long doseTime) {
        this.doseTime = doseTime;
    }

    public List<MedicationPojo> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<MedicationPojo> childItemList) {
        this.childItemList = childItemList;
    }

}
