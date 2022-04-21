package com.iti.mad42.remedicine.Requests.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.R;

import java.util.List;
import java.util.stream.Stream;

public class RequestScreenAdapter extends RecyclerView.Adapter<RequestScreenAdapter.ViewHolder> {

    private final Context context;
    private List<RequestPojo> requests;
    private OnClickListenerInterface listener;

    public RequestScreenAdapter(Context _context, List<RequestPojo> requests, OnClickListenerInterface listener){
        this.context = _context;
        this.requests = requests;
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.userName.setText(requests.get(position).getSenderEmail().split("[@.]")[0]);
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Accept Button", Toast.LENGTH_SHORT).show();
                listener.onClickAcceptBtn(requests.get(position));
                setList(requests);
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Reject Button", Toast.LENGTH_SHORT).show();
                listener.onClickRejectBtn(requests.get(position));
                setList(requests);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("sandra" , "requests list size : "+ requests.size());
        return requests.size();
    }

    public void setList(List<RequestPojo> requests){
        this.requests = requests;
    }
}
