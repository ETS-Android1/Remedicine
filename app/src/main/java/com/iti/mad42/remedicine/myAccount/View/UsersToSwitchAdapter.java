package com.iti.mad42.remedicine.myAccount.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.data.pojo.User;
import com.iti.mad42.remedicine.R;

import java.util.List;

public class UsersToSwitchAdapter extends RecyclerView.Adapter<UsersToSwitchAdapter.ViewHolder> {
    private List<User> users;
    private Context context;
    private OnRowClickListenerInterface listener;

    public UsersToSwitchAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userNameTxt;
        public ConstraintLayout constraintLayout;
        public View layout;
        public ViewHolder(@NonNull View layout) {
            super(layout);
            this.layout = layout;
            userNameTxt = layout.findViewById(R.id.userNameTxt);
            constraintLayout = layout.findViewById(R.id.userToSwitchConstraint);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_accounts_to_switch_row, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userNameTxt.setText(users.get(position).getEmail().split("[@.]")[0]);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRowItem(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnRowClickListener(OnRowClickListenerInterface listener){
        this.listener = listener;
    }

    public void setUsersList(List<User> users){
        this.users = users;
    }

}