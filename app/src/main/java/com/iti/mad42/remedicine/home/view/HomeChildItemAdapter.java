package com.iti.mad42.remedicine.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class HomeChildItemAdapter extends RecyclerView
        .Adapter<HomeChildItemAdapter.ChildViewHolder> {

    private List<MedicationPojo> ChildItemList;
    private OnNodeListener onNodeListener;
    private Long time;
    private Context context;
    private String date;
    private String dayStat;

    // Constructor
    HomeChildItemAdapter(Context context, List<MedicationPojo> childItemList, OnNodeListener onNodeListener, Long time, String date)
    {
        this.ChildItemList = childItemList;
        this.context = context;
        this.onNodeListener = onNodeListener;
        this.time = time;
        this.date = date;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {

        long today = Utility.dateToLong(Utility.getCurrentDay());
        long selectedDay = Utility.dateToLong(date);
        if (selectedDay == today) {
            dayStat = "today";
        }else if (selectedDay > today) {
            dayStat = "after";
        }else {
            dayStat = "before";
        }
        // Here we inflate the corresponding
        // layout of the child item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_child_recyclerview_item, viewGroup, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChildViewHolder childViewHolder, int position) {

        int medStatIndex = 0;
        MedicationPojo childItem = ChildItemList.get(position);
        childViewHolder.tvChildItemMedName.setText(childItem.getName());
        for (int x = 0; x < childItem.getMedState().size(); x++) {
            if (childItem.getMedState().get(x).getTime() == time) {
                medStatIndex = x;
            }
        }

        switch (dayStat) {

            case "today":
                if (childItem.getMedState().get(medStatIndex).getState().equals("taken")) {
                    childViewHolder.tvChildItemMedSub.setText("Taken");
                    childViewHolder.tvChildItemMedSub.setTextColor(context.getResources().getColor(R.color.main_app_color));
                    childViewHolder.cellBtn.setEnabled(false);
                }else {
                    childViewHolder.tvChildItemMedSub.setText(childItem.getStrength()+"g, Take "+childItem.getMedDoseReminders().get(0).getMedDose()+" Pill(s)");
                }
                break;
            case "after":
                childViewHolder.cellBtn.setEnabled(false);
                childViewHolder.tvChildItemMedSub.setText(childItem.getStrength()+"g, Take "+childItem.getMedDoseReminders().get(0).getMedDose()+" Pill(s)");
                break;
            case "before":
                childViewHolder.tvChildItemMedSub.setText("Taken");
                childViewHolder.tvChildItemMedSub.setTextColor(context.getResources().getColor(R.color.main_app_color));
                childViewHolder.cellBtn.setEnabled(false);
                break;
        }

        childViewHolder.cellBtn.setOnClickListener(view -> onNodeListener.getChosenMedicine(childItem, time));
    }

    @Override
    public int getItemCount()
    {
        return ChildItemList.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder{

        TextView tvChildItemMedName, tvChildItemMedSub;
        ImageView imgViewPillImage;
        ConstraintLayout constraintLayout;
        Button cellBtn;
        OnNodeListener onNodeListener;

        ChildViewHolder(View itemView)
        {
            super(itemView);
            tvChildItemMedName = itemView.findViewById(R.id.txtViewHomeMedName);
            tvChildItemMedSub = itemView.findViewById(R.id.txtViewHomeMedSubtitle);
            imgViewPillImage = itemView.findViewById(R.id.imgViewPill);
            constraintLayout = itemView.findViewById(R.id.constrainLayoutHomeChildItem);
            cellBtn = itemView.findViewById(R.id.btnCell);
        }
    }



}
