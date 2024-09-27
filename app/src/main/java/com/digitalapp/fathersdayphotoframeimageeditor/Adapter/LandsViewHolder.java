package com.digitalapp.fathersdayphotoframeimageeditor.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalapp.fathersdayphotoframeimageeditor.R;


public class LandsViewHolder extends RecyclerView.ViewHolder {

    public ImageView frameId;


    public LandsViewHolder(@NonNull View itemView) {
        super(itemView);

        frameId = itemView.findViewById(R.id.frameId);


    }
}
