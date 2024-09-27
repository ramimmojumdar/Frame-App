package com.digitalapp.fathersdayphotoframeimageeditor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digitalapp.fathersdayphotoframeimageeditor.Activity.EditorActivity;
import com.digitalapp.fathersdayphotoframeimageeditor.R;

import java.util.ArrayList;
import java.util.List;

public class LandscapeAdapter extends RecyclerView.Adapter<LandsViewHolder> {

    private Context context;
    private List<String> imageList;

    public LandscapeAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList != null ? imageList : new ArrayList<>();
    }

    @NonNull
    @Override
    public LandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_items_frame, parent, false);
        return new LandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(imageList.get(position)).placeholder(R.drawable.placeholder).into(holder.frameId);

        // Set onClickListener to show Toast message
        holder.frameId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUrl = imageList.get(position);

                // Update the ImageView in the EditorActivity without starting a new Activity
                if (context instanceof EditorActivity) {
                    ((EditorActivity) context).updateFrameImage(imageUrl);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
