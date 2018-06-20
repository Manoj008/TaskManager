package com.manoj.taskmanagertodoapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.AddEvent;
import com.manoj.taskmanagertodoapp.Main2Activity;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.OnEmptyListener;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.List;

/**
 * Created by MANOJ on 31-May-18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    Context context;
    List<Details> details ;
    int lastPosition=-1;
    public boolean isClickable=true;
    SharedPreferences sharedPreferences;

    public void setListener(OnEmptyListener listener){
        onEmptyListener=listener;
    }

    public MyAdapter(Context context, List details) {
        this.context=context;
        this.details=details;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        Details detail=details.get(position);
        holder.name.setText(detail.getTask());
        holder.time.setText(detail.getDate());
        holder.place.setText(detail.getPlace());
        holder.checkBox.setChecked(false);

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
        if(details.size()==0){
            doRefresh();
        }
        return details.size();
    }

    OnEmptyListener onEmptyListener=null;

    public void doRefresh(){
        if(onEmptyListener!=null){
            onEmptyListener.onEmpty();
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView time;
        TextView place;
        CheckBox checkBox;


        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.taskName);
            time = itemView.findViewById(R.id.taskTime);
            place = itemView.findViewById(R.id.taskPlace);
            checkBox = itemView.findViewById(R.id.checkbox);
            sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
            final boolean confirm=sharedPreferences.getBoolean("confirm",false);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if (confirm) {
                            AlertDialog alertDialog=new AlertDialog.Builder(context)
                                    .setMessage("have you finished this task?")
                                    .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            checkBox.setChecked(false);
                                        }
                                    })
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            call();
                                        }
                                    }).create();
                            alertDialog.show();

                        }
                        else
                            call();
                    }
                }
            });

        }

        private void call() {
            if (!isClickable)
                return;
            isClickable = false;
            final int position = getAdapterPosition();


            DatabaseTask db = new DatabaseTask(context);
            if (position >= 0)
                db.deleteEvent(details.get(position).getId());

            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0
                    , Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_SELF, 0
                    , Animation.RELATIVE_TO_PARENT, 0);
            animation.setDuration(600);
            itemView.startAnimation(animation);

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lastPosition = position - 1;
                    details.remove(position);
                    checkBox.setChecked(false);
                    notifyItemRemoved(position);
                    Main2Activity.updateStatus(context);
                }
            }, 400);


            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    isClickable = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        public void onClick(final View v) {
            if(!isClickable)
                return;

            int position=getAdapterPosition();
            Details detail=details.get(position);
            Intent intent=new Intent(context, AddEvent.class);
            intent.putExtra("id",detail.getId());
            intent.putExtra("fragment","task");
            context.startActivity(intent);
        }
    }
}
