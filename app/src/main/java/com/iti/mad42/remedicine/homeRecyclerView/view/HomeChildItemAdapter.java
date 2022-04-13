package com.iti.mad42.remedicine.homeRecyclerView.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mad42.remedicine.R;

import java.util.List;

public class HomeChildItemAdapter extends RecyclerView
        .Adapter<HomeChildItemAdapter.ChildViewHolder> {

    private List<HomeChildItem> ChildItemList;
    Context context;

    // Constructor
    HomeChildItemAdapter(Context context, List<HomeChildItem> childItemList)
    {
        this.ChildItemList = childItemList;
        this.context = context;
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

        // Create an instance of the ChildItem
        // class for the given position
        HomeChildItem childItem = ChildItemList.get(position);

        // For the created instance, set title.
        // No need to set the image for
        // the ImageViews because we have
        // provided the source for the images
        // in the layout file itself
        childViewHolder.tvChildItemMedName.setText(childItem.getMedicineName());
        childViewHolder.tvChildItemMedSub.setText(childItem.getMedicineSubtitle());
    }

    @Override
    public int getItemCount()
    {
        return ChildItemList.size();
    }

    // This class is to initialize
    // the Views present
    // in the child RecyclerView
    class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView tvChildItemMedName, tvChildItemMedSub;
        ImageView imgViewPillImage;
        ConstraintLayout constraintLayout;

        ChildViewHolder(View itemView)
        {
            super(itemView);
            tvChildItemMedName = itemView.findViewById(R.id.txtViewHomeMedName);
            tvChildItemMedSub = itemView.findViewById(R.id.txtViewHomeMedSubtitle);
            imgViewPillImage = itemView.findViewById(R.id.imgViewPill);
            constraintLayout = itemView.findViewById(R.id.constrainLayoutHomeChildItem);
        }
    }
}
