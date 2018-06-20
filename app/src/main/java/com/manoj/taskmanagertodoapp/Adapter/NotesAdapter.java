package com.manoj.taskmanagertodoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.AddEvent;
import com.manoj.taskmanagertodoapp.Model.Diary;
import com.manoj.taskmanagertodoapp.R;

import java.util.List;

/**
 * Created by MANOJ on 04-Jun-18.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    Context context;
    List<Diary> list;
    int lastPosition=-1;

    public NotesAdapter(Context context, List<Diary> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteslayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate());
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPosition) {
//            ScaleAnimation animation= new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f
//                    , Animation.RELATIVE_TO_SELF,0.5f);
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0
                    , Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 1.0f
                    , Animation.RELATIVE_TO_PARENT, 0.0f);
            animation.setDuration(500);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,date;

        public ViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.noteName);
            date=itemView.findViewById(R.id.noteDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Diary detail=list.get(position);
            Intent intent=new Intent(context, AddEvent.class);
            intent.putExtra("fragment","note");
            intent.putExtra("id",detail.getId());
            context.startActivity(intent);
        }
    }
}
