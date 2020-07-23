package com.mytodoworkplan.mydayplanner;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class adapterForWeek_rv extends RecyclerView.Adapter<adapterForWeek_rv.ViewHolder> {

    private Context context;
    private List<modelClass> uploads;
    modelClass upload;
    private OnItemClickListener mListener;


    public adapterForWeek_rv(Context context, List<modelClass> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_week, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        upload = uploads.get(position);
        holder.workTitle_week.setText(uploads.get(position).getWorkTitle());
        holder.workContent_week.setText(uploads.get(position).getWorkContent());
        holder.workTime_week.setText("At "+uploads.get(position).getWorkTime());
       holder.workDay_week.setText(uploads.get(position).getWeekDay());






    }

    @Override
    final public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
            , MenuItem.OnMenuItemClickListener {


        public TextView workTitle_week,workContent_week,workTime_week,workDay_week;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
             workTitle_week = itemView.findViewById(R.id.tv_list_workTitle_week);
             workContent_week = itemView.findViewById(R.id.list_tv_content_week);
             workTime_week = itemView.findViewById(R.id.list_week_time);
            workDay_week = itemView.findViewById(R.id.list_week_day);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    mListener.onItemClick(position);

                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose action");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
          //  MenuItem delete = menu.add(Menu.NONE,2,2,"Supprimer");
            edit.setOnMenuItemClickListener(this);
          //  delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {

                        case 1:
                            mListener.onEditClick(position);
                            return true;
//                        case 2:
//                            mListener.onDeleteClick(position);
//                            return true;
                    }

                }
            }
            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);

        void onEditClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
