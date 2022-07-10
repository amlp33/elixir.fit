package com.example.yoga_;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    Context context;
    ArrayList<Video> videoArrayList;
    private OnVideoItemListenener mOnVideoItemListener;
    public VideoAdapter(Context context, ArrayList<Video> videoArrayList, OnVideoItemListenener onVideoItemListenener ) {
        this.mOnVideoItemListener = onVideoItemListenener;
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.video_card_item,parent,false);

        return new MyViewHolder(v,mOnVideoItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.MyViewHolder holder, int position) {
        Video videoInfo = videoArrayList.get(position);
        String type = videoInfo.type;

        //String exerciseName = videoInfo.exerciseName;

        if(videoInfo.type.contains("yoga")) {

            holder.exerciseNameAndType.setText(videoInfo.exerciseName +"\n"+ "Yoga");
           holder.videoListImage.setImageResource(R.drawable.yoga);
        }
        else if(videoInfo.type.contains("calorieBurn")){

            holder.exerciseNameAndType.setText(videoInfo.exerciseName +"\n"+ "Calorie Burn");
           holder.videoListImage.setImageResource(R.drawable.ic_baseline_local_fire_department_24);
        }

    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView exerciseNameAndType;
    ImageView videoListImage;
    OnVideoItemListenener onVideoItemListenener;

        public MyViewHolder(@NonNull View itemView,OnVideoItemListenener onVideoItemListenener ) {
            super(itemView);
            exerciseNameAndType = itemView.findViewById(R.id.exerciseNameAndType);
            videoListImage = itemView.findViewById(R.id.videoListImage);
            this.onVideoItemListenener = onVideoItemListenener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onVideoItemListenener.onVideoClick(getAdapterPosition());
        }
    }

   public interface OnVideoItemListenener{
        void onVideoClick(int position);
   }
}
