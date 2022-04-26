package com.iti.mad42.remedicine.medicineDetails.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.data.pojo.MedicineDose;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class RemindersRecyclerAdapter extends RecyclerView.Adapter<RemindersRecyclerAdapter.ViewHolder> {
private final Context context;
private List<MedicineDose> myMeds;


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

    public RemindersRecyclerAdapter(Context _context, List<MedicineDose> myMeds){
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
        holder.medTime.setText(millisToTimeAsString(myMeds.get(position).getDoseTimeInMilliSec()));
        holder.medDose.setText("Take "+myMeds.get(position).getMedDose()+" "+ myMeds.get(position).getMedForm() +"(s)");

       // holder.medDose.setText(myMeds.get(position).getMedStrength()+" "+ myMeds.get(position).getMedUnit()+" Take 1 Pill(s)"+myMeds.get(position).getMedInstructions());
    }
    public String millisToTimeAsString(long timeInMillis){
        int minutes = (int) ((timeInMillis / (1000*60)) % 60);
        int hours   = (int) ((timeInMillis / (1000*60*60)) % 24);
        return (hours+":"+minutes);
    }

    @Override
    public int getItemCount() {
        if (myMeds== null)
             return 0;

        return myMeds.size();
    }
}
