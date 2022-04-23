package com.iti.mad42.remedicine.homeRecyclerView.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class HomeChildItemAdapter extends RecyclerView
        .Adapter<HomeChildItemAdapter.ChildViewHolder> {

    private List<MedicationPojo> ChildItemList;
    private OnNodeListener onNodeListener;
    Context context;

    // Constructor
    HomeChildItemAdapter(Context context, List<MedicationPojo> childItemList, OnNodeListener onNodeListener)
    {
        this.ChildItemList = childItemList;
        this.context = context;
        this.onNodeListener = onNodeListener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {

        // Here we inflate the corresponding
        // layout of the child item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_child_recyclerview_item, viewGroup, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChildViewHolder childViewHolder, int position) {


        MedicationPojo childItem = ChildItemList.get(position);
        childViewHolder.tvChildItemMedName.setText(childItem.getName());
        childViewHolder.tvChildItemMedSub.setText(childItem.getStrength()+"g, Take "+childItem.getMedDoseReminders().get(0).getMedDose()+" Pill(s)");
        childViewHolder.cellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNodeListener.getChosenMedicine(childItem);
            }
        });
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
