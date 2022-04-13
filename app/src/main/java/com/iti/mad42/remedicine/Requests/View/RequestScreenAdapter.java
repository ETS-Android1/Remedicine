package com.iti.mad42.remedicine.Requests.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.Model.Medication;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class RequestScreenAdapter extends RecyclerView.Adapter<RequestScreenAdapter.ViewHolder> {

    private final Context context;
    private List<Medication> myMeds;

    public RequestScreenAdapter(Context _context, List<Medication> myMeds){
        this.context = _context;
        this.myMeds = myMeds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public Button acceptBtn, rejectBtn;
        public View layout;

        public ViewHolder(View layout) {
            super(layout);
            this.layout = layout;
            userName = layout.findViewById(R.id.requestUserName);
            acceptBtn = layout.findViewById(R.id.acceptBtn);
            rejectBtn = layout.findViewById(R.id.rejectBtn);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.requests_custom_cell,parent, false);
        ViewHolder requestViewHolder = new ViewHolder(layout);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(myMeds.get(position).getMedName());
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Accept Button", Toast.LENGTH_SHORT).show();
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Reject Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myMeds.size();
    }


}
