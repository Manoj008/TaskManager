package com.manoj.taskmanagertodoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.List;

/**
 * Created by MANOJ on 03-Jun-18.
 */

public class MyFinAdapter extends RecyclerView.Adapter<MyFinAdapter.ViewHolder> {

    Context context;
    List<Details> details ;
    int lastPosition=-1;
    boolean isClickable=true;
    public MyFinAdapter(Context context, List details) {
        this.context=context;
        this.details=details;
    }

    @Override

    public MyFinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finishedlayout,parent,false);
        return new MyFinAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Details detail=details.get(position);
        holder.name.setText(detail.getTask());
        holder.time.setText(detail.getDate());
        holder.place.setText(detail.getPlace());

        setAnimation(holder.itemView,position);
    }


    private void setAnimation(View itemView, int position) {
        if(position>lastPosition){
//            ScaleAnimation animation= new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f
//                    , Animation.RELATIVE_TO_SELF,0.5f);
            TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0
                    ,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,1.0f
                    ,Animation.RELATIVE_TO_PARENT,0.0f);
            animation.setDuration(500);
            itemView.startAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView time;
        TextView place;
        Button delete;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.taskName);
            time = itemView.findViewById(R.id.taskTime);
            place = itemView.findViewById(R.id.taskPlace);
            delete=itemView.findViewById(R.id.deleteFinished);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickable)
                        return;
                    isClickable=false;
                    final int position=getAdapterPosition();
                    DatabaseTask db=new DatabaseTask(context);
                    db.deleteFinished(details.get(getAdapterPosition()).getId());

                    TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0
                            ,Animation.RELATIVE_TO_PARENT,1,Animation.RELATIVE_TO_SELF,0
                            ,Animation.RELATIVE_TO_PARENT,0);
                    animation.setDuration(600);
                    itemView.startAnimation(animation);

                    new android.os.Handler().postDelayed(new Runnable() {
                       @Override
                        public void run() {
                            lastPosition=position-1;
                            details.remove(position);
                            notifyItemRemoved(position);
                        }
                    },400);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isClickable=true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                }
            });

        }

        @Override
        public void onClick(final View v) {

        }
    }
}
