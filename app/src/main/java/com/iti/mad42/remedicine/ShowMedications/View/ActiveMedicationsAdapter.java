package com.iti.mad42.remedicine.ShowMedications.View;

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
import java.util.List;

public class ActiveMedicationsAdapter extends RecyclerView.Adapter<ActiveMedicationsAdapter.ViewHolder> {
    private final Context context;
    private List<Medication> myMeds;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView medName, medSubtitle;
        public ConstraintLayout constraintLayout;
        public View layout;

        public ViewHolder( View layout) {
            super(layout);
            this.layout = layout;
            medName = layout.findViewById(R.id.txtViewHomeMedName);
            medSubtitle = layout.findViewById(R.id.txtViewHomeMedSubtitle);
            constraintLayout = layout.findViewById(R.id.constrainLayoutHomeChildItem);
        }

    }

    public ActiveMedicationsAdapter(Context _context, List<Medication> myMeds){
        this.context = _context;
        this.myMeds = myMeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.med_fragment_custom_cell, parent,false);
        ViewHolder myViewHolder = new ViewHolder(layout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.medName.setText(myMeds.get(position).getMedName());
        holder.medSubtitle.setText(myMeds.get(position).getMedStrength()+" "+ myMeds.get(position).getMedUnit()+" Take 1 Pill(s)"+myMeds.get(position).getMedInstructions());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,myMeds.get(position).getMedName()+" Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myMeds.size();
    }
}
