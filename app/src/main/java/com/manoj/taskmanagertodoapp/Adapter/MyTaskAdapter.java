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
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;

import java.util.List;

/**
 * Created by MANOJ on 03-Jun-18.
 */

public class MyTaskAdapter  extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {
    Context context;
    List<Details> details;
    int lastPosition = -1;

    public MyTaskAdapter(Context context, List details) {
        this.context = context;
        this.details = details;
    }

    @Override

    public MyTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytasklayout, parent, false);
        return new MyTaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Details detail = details.get(position);
        holder.name.setText(detail.getTask());
        holder.time.setText(detail.getDate().substring(0));

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
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView time;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.taskName);
            time = itemView.findViewById(R.id.taskTime);
        }

        @Override
        public void onClick(final View v) {
            int position=getAdapterPosition();
            Details detail=details.get(position);
            Intent intent=new Intent(context, AddEvent.class);
            intent.putExtra("id",detail.getId());
            intent.putExtra("fragment","bday");
            context.startActivity(intent);

        }
    }
}