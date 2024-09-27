package com.digitalapp.fathersdayphotoframeimageeditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digitalapp.fathersdayphotoframeimageeditor.R;

import java.util.ArrayList;
import java.util.List;

public class PortraitAdapter extends RecyclerView.Adapter<PortraitViewHolder> {

    private Context context;
    private List<String> imageList;

    public PortraitAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList != null ? imageList : new ArrayList<>();
    }

    @NonNull
    @Override
    public PortraitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_items_frame, parent, false);
        return new PortraitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortraitViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position)).placeholder(R.drawable.placeholder).into(holder.frameId);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}

