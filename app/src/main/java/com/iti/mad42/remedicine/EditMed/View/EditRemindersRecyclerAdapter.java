package com.iti.mad42.remedicine.EditMed.View;

import static com.iti.mad42.remedicine.Model.pojo.Utility.millisToTimeAsString;
import static com.iti.mad42.remedicine.Model.pojo.Utility.timeToMillis;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class EditRemindersRecyclerAdapter extends RecyclerView.Adapter<EditRemindersRecyclerAdapter.ViewHolder>{
    private final Context context;

    public List<MedicineDose> getMyMeds() {
        return myMeds;
    }

    private List<MedicineDose> myMeds;
    Dialog timeDoseDialog;

    public EditRemindersRecyclerAdapter(Context _context, List<MedicineDose> myMeds){
        this.context = _context;
        this.myMeds = myMeds;
        timeDoseDialog = new Dialog(_context);
        Log.e("mando", "EditRemindersRecyclerAdapter: " +myMeds.size() );
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

        holder.medTime.setText(millisToTimeAsString(myMeds.get(position).getDoseTimeInMilliSec()));
        holder.medDose.setText("Take "+myMeds.get(position).getMedDose()+" "+ myMeds.get(position).getMedForm() +"(s)");
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeDoseDialog(holder,position);
            }
        });
    }


    public void openTimeDoseDialog(ViewHolder holder , int pos){
        timeDoseDialog.setContentView(R.layout.time_dose_dialouge);
        timeDoseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeDialog = timeDoseDialog.findViewById(R.id.timeDoseDialogCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDoseDialog.dismiss();
            }
        });

        FloatingActionButton increaseBtn, decreaseBtn;
        Button saveBtn;
        TextView doseAmountText;
        TimePicker timePicker;

        increaseBtn = timeDoseDialog.findViewById(R.id.increaseBtn);
        decreaseBtn = timeDoseDialog.findViewById(R.id.decreaseBtn);
        saveBtn = timeDoseDialog.findViewById(R.id.save_dose_time_btn);
        timePicker = timeDoseDialog.findViewById(R.id.doseTimePicker);
        doseAmountText = timeDoseDialog.findViewById(R.id.doseAmount);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.counter++;
                doseAmountText.setText(holder.counter+"");
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.counter>1){
                    holder.counter--;
                    doseAmountText.setText(holder.counter+"");
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag =false;
                switch (myMeds.size()){
                    case 1 :
                        myMeds.set(pos,new MedicineDose(myMeds.get(pos).getMedForm(),Integer.parseInt(doseAmountText.getText().toString()),timeToMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute())));
                        holder.medTime.setText(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
                        holder.medDose.setText("Take "+doseAmountText.getText().toString()+" "+myMeds.get(pos).getMedForm()+"(s)");
                        timeDoseDialog.dismiss();
                        break;
                    case 2:
                        for (int i = 0; i < 2; i++) {
                            if(timeToMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute())==myMeds.get(i).getDoseTimeInMilliSec()){
                                flag = true;
                                break;
                            }
                        }
                        if (flag){
                            Toast.makeText(context,"Cannot Enter The Same Time Twice",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            myMeds.set(pos,new MedicineDose(myMeds.get(pos).getMedForm(),Integer.parseInt(doseAmountText.getText().toString()),timeToMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute())));
                            holder.medTime.setText(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
                            holder.medDose.setText("Take "+doseAmountText.getText().toString()+" "+myMeds.get(pos).getMedForm()+"(s)");
                            timeDoseDialog.dismiss();
                        }
                        break;
                    case 3:
                        for (int i = 0; i < 3; i++) {
                            if(timeToMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute())==myMeds.get(i).getDoseTimeInMilliSec()){
                                flag = true;
                                break;
                            }
                        }
                        if (flag){
                            Toast.makeText(context,"Cannot Enter The Same Time Twice",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            myMeds.set(pos,new MedicineDose(myMeds.get(pos).getMedForm(),Integer.parseInt(doseAmountText.getText().toString()),timeToMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute())));
                            holder.medTime.setText(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
                            holder.medDose.setText("Take "+doseAmountText.getText().toString()+" "+myMeds.get(pos).getMedForm()+"(s)");
                            timeDoseDialog.dismiss();
                        }
                        break;
                }
            }
        });

        timeDoseDialog.show();
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
        int counter;

        public ViewHolder( View layout) {
            super(layout);
            this.layout = layout;
            constraintLayout= layout.findViewById(R.id.med_reminders_item_constraint_layout);
            medTime = layout.findViewById(R.id.med_time_in_hrs);
            medDose = layout.findViewById(R.id.med_dose_per_time);
            counter = 1;
        }
    }
}
