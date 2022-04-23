package com.iti.mad42.remedicine.MedDetails.View;

import static com.iti.mad42.remedicine.Model.pojo.Utility.timeToMillis;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.facebook.CallbackManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.mad42.remedicine.AddDose.View.AddDose;
import com.iti.mad42.remedicine.EditMed.View.EditMedActivity;
import com.iti.mad42.remedicine.MedDetails.Presenter.MedDetailsPresenter;
import com.iti.mad42.remedicine.MedDetails.Presenter.MedDetailsPresenterInterface;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MedDetails extends AppCompatActivity implements MedDetailsInterface {

    private ImageView back;
    private ImageView delete;
    private ImageView edit;
    private TextView title;
    private ImageView medImg;
    private TextView medicationNameLabel;
    private TextView medicationStrengthLabel;
    private View seperator;
    private TextView medDurationLabel;
    private MaterialButton suspendBtn;
    private RecyclerView medTimeRecyclerview;
    private TextView medReasonLabel;
    private TextView pillsLeftLabel;
    private TextView prescriptionRefillLabel;
    private MaterialButton refillBtn;
    private TextView howToUseLabel;
    private Button add;
    private TextView lastTimeTakenLabel;
    MedDetailsPresenterInterface presenter;
    Dialog refillDialog;

    RemindersRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);
        initView();
        refillDialog = new Dialog(this);
        presenter = new MedDetailsPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        })));
        setListeners();
        presenter.setData();

    }

    public MedicationPojo getMedObject() {
        return (MedicationPojo) getIntent().getSerializableExtra("fromActiveToDetails");
    }

    public void setMedTimeRecyclerview(List<MedicineDose> medDose) {
        medTimeRecyclerview.setHasFixedSize(true);
        adapter = new RemindersRecyclerAdapter(this, medDose);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        medTimeRecyclerview.setLayoutManager(layoutManager);
        medTimeRecyclerview.setAdapter(adapter);
    }

    public void setMedicationNameLabel(String medName) {
        medicationNameLabel.setText(medName);
    }

    public void setPrescriptionRefillLabel(String prescriptionRefill) {
        prescriptionRefillLabel.setText(prescriptionRefill);
    }

    public void setHowToUseLabel(String howToUse) {
        howToUseLabel.setText(howToUse);
    }

    public void setPillsLeftLabel(String pillsLeft) {
        pillsLeftLabel.setText(pillsLeft);
    }

    public void setMedReasonLabel(String medReason) {
        medReasonLabel.setText(medReason);
    }

    public void setMedDurationLabel(String medDuration) {
        medDurationLabel.setText(medDuration);
    }

    public void setMedicationStrengthLabel(String medicationStrength) {
        medicationStrengthLabel.setText(medicationStrength);
    }

    public void setSuspendBtnText(String state){
        suspendBtn.setText(state);
    }

    void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MedDetails.this, AddDose.class);
//                startActivity(intent);
                presenter.addDose();
                Toast.makeText(getApplicationContext(), "Additional Dose Taken", Toast.LENGTH_SHORT).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedDetails.this, EditMedActivity.class);
                intent.putExtra("fromDetailsToEdit",(Serializable) presenter.getMedication());
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteMed();
                setWorkTimer();
                finish();
            }
        });
        suspendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.suspendMed();
            }
        });
        refillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRefillDialog();
            }
        });
    }

    void openRefillDialog(){

        refillDialog.setContentView(R.layout.refill_med_dialog);
        refillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button refillBtn,cancelBtn;
        TextInputLayout refillEdt;
        ImageView closeDialog = refillDialog.findViewById(R.id.dialogCloseBtn);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refillDialog.dismiss();
            }
        });

        refillBtn = refillDialog.findViewById(R.id.refillBtn);
        cancelBtn = refillDialog.findViewById(R.id.cancelMedBtn);
        refillEdt = refillDialog.findViewById(R.id.refillEdit);

        refillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.setRefillAmount(Integer.parseInt(refillEdt.getEditText().getText().toString()));
               presenter.refillMed();
               setWorkTimer();
               refillDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refillDialog.dismiss();
            }
        });

        refillDialog.show();
    }

    public void setWorkTimerForRefillReminder() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void setWorkTimer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        delete = (ImageView) findViewById(R.id.delete);
        edit = (ImageView) findViewById(R.id.edit);
        title = (TextView) findViewById(R.id.title);
        medImg = (ImageView) findViewById(R.id.med_img);
        medicationNameLabel = (TextView) findViewById(R.id.medication_name_label);
        medicationStrengthLabel = (TextView) findViewById(R.id.medication_strength_label);
        seperator = (View) findViewById(R.id.seperator);
        medDurationLabel = (TextView) findViewById(R.id.med_duration_label);
        suspendBtn = (MaterialButton) findViewById(R.id.suspend_btn);
        medTimeRecyclerview = (RecyclerView) findViewById(R.id.med_time_recyclerview);
        medReasonLabel = (TextView) findViewById(R.id.med_reason_label);
        pillsLeftLabel = (TextView) findViewById(R.id.pills_left_label);
        prescriptionRefillLabel = (TextView) findViewById(R.id.prescription_refill_label);
        refillBtn = (MaterialButton) findViewById(R.id.refill_btn);
        howToUseLabel = (TextView) findViewById(R.id.how_to_use_label);
        add = (Button) findViewById(R.id.add);
        lastTimeTakenLabel = (TextView) findViewById(R.id.last_time_taken_label);
    }
}