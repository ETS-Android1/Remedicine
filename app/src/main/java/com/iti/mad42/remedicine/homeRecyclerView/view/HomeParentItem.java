package com.iti.mad42.remedicine.homeRecyclerView.view;

import java.util.List;

public class HomeParentItem {

    private String doseTime;
    private List<HomeChildItem> childItemList;

    public HomeParentItem() {
    }

    public HomeParentItem(String doseTime, List<HomeChildItem> childItemList) {
        this.doseTime = doseTime;
        this.childItemList = childItemList;
    }

    public String getDoseTime() {
        return doseTime;
    }

    public void setDoseTime(String doseTime) {
        this.doseTime = doseTime;
    }

    public List<HomeChildItem> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<HomeChildItem> childItemList) {
        this.childItemList = childItemList;
    }

}
