package com.digitalapp.fathersdayphotoframeimageeditor.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import com.digitalapp.fathersdayphotoframeimageeditor.Activity.EditorActivity;
import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.FrameData;
import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.LandsViewHolder;

import com.digitalapp.fathersdayphotoframeimageeditor.R;



import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LandscapeFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<String> imageList = new ArrayList<>();
    LandscapeAdapter landscapeAdapter ;
    private StaggeredGridLayoutManager GridLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_landscape, container, false);

        FirebaseApp.initializeApp(getContext());


        recyclerView =view. findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);

        GridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(GridLayoutManager);

        landscapeAdapter = new LandscapeAdapter(getContext(), imageList);
        recyclerView.setAdapter(landscapeAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("PhotoFrame/" + FrameData.Landscape + "/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    imageList.add(dataSnapshot.getValue(String.class));
                }
                Collections.shuffle(imageList);
                landscapeAdapter.notifyDataSetChanged();
                databaseReference.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

return view;
    }



    public class LandscapeAdapter extends RecyclerView.Adapter<LandsViewHolder> {

        private Context context;
        private List<String> imageList;



        public LandscapeAdapter (Context context, List<String> imageList) {
            this.context = context;
            this.imageList = imageList;

        }

        @NonNull
        @Override
        public LandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.landscape_item_view, parent, false);
            return new LandsViewHolder(view);
        }




        @Override
        public void onBindViewHolder(@NonNull LandsViewHolder holder, @SuppressLint("RecyclerView") int position) {


            Glide.with(context).load(imageList.get(position)).placeholder(R.drawable.placeholder).into(holder.frameId);




            holder.frameId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        String imageUrl = imageList.get(position);
                        Intent intent = new Intent(context, EditorActivity.class);
                        intent.putExtra("imageUrl", imageUrl);
                        intent.putStringArrayListExtra("imageList", (ArrayList<String>) imageList);
                        context.startActivity(intent);

                    // All frames will be sent to EditorActivity as soon as they are loaded



                    //Toast.makeText(context, "এখানে থেকে ইডিটর পেজ নিয়ে গিয়ে ফ্রেম সেট এবং বাকি কাজ গুলো করতে হবে ", Toast.LENGTH_SHORT).show();

                    }

            });


        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

    }

}
