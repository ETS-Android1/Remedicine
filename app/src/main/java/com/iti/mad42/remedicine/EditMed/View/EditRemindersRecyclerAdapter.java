package com.iti.mad42.remedicine.EditMed.View;

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

import com.iti.mad42.remedicine.MedDetails.View.RemindersRecyclerAdapter;
import com.iti.mad42.remedicine.Model.MedicineDose;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class EditRemindersRecyclerAdapter extends RecyclerView.Adapter<EditRemindersRecyclerAdapter.ViewHolder>{
    private final Context context;
    private List<MedicineDose> myMeds;

    public EditRemindersRecyclerAdapter(Context _context, List<MedicineDose> myMeds){
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
        holder.medTime.setText(myMeds.get(position).getHour()+":"+myMeds.get(position).getMinute());
        holder.medDose.setText("Take "+myMeds.get(position).getDose()+" "+myMeds.get(position).getName()+"(s)");
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,myMeds.get(position).getName()+" Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (myMeds== null)
            return 0;

        return myMeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView medTime, medDose;
        public ConstraintLayout constraintLayout;
        public View layout;

        public ViewHolder( View layout) {
            super(layout);
            this.layout = layout;
            constraintLayout= layout.findViewById(R.id.med_reminders_item_constraint_layout);
            medTime = layout.findViewById(R.id.med_time_in_hrs);
            medDose = layout.findViewById(R.id.med_dose_per_time);

        }
    }
}
