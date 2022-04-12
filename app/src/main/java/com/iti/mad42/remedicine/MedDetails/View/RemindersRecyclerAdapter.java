package com.iti.mad42.remedicine.MedDetails.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.Model.Medication;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.ShowMedications.View.ActiveMedicationsAdapter;

import java.util.List;

public class RemindersRecyclerAdapter extends RecyclerView.Adapter<RemindersRecyclerAdapter.ViewHolder> {
private final Context context;
private List<Medication> myMeds;


public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView medTime, medDose;

    public View layout;

    public ViewHolder( View layout) {
        super(layout);
        this.layout = layout;
        medTime = layout.findViewById(R.id.med_time_in_hrs);
        medDose = layout.findViewById(R.id.med_dose_per_time);

    }
}
    public RemindersRecyclerAdapter(Context _context, List<Medication> myMeds){
        this.context = _context;
        this.myMeds = myMeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.med_time_recyclerview_item, parent,false);
        ViewHolder myViewHolder = new ViewHolder(layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.medTime.setText(myMeds.get(position).getMedName());
        holder.medTime.setText("9:00");
        holder.medDose.setText(" Take 1 Pill(s)");

       // holder.medDose.setText(myMeds.get(position).getMedStrength()+" "+ myMeds.get(position).getMedUnit()+" Take 1 Pill(s)"+myMeds.get(position).getMedInstructions());
    }

    @Override
    public int getItemCount() {
        if (myMeds== null)
             return 0;

        return myMeds.size();
    }
}
