package com.iti.mad42.remedicine.homeRecyclerView.view;

public class HomeChildItem {

    private String medicineName;
    private String medicineSubtitle;

    public HomeChildItem() {
    }

    public HomeChildItem(String medicineName, String medicineSubtitle) {
        this.medicineName = medicineName;
        this.medicineSubtitle = medicineSubtitle;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineSubtitle() {
        return medicineSubtitle;
    }

    public void setMedicineSubtitle(String medicineSubtitle) {
        this.medicineSubtitle = medicineSubtitle;
    }
}
