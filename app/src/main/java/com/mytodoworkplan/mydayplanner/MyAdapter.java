package com.mytodoworkplan.mydayplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<modelClass> uploads;
    modelClass upload;
    private OnItemClickListener mListener;
    private SQLiteDatabase sqLiteDatabase;

    private mySQLiteDBHelper dbHandler;


    public MyAdapter(Context context, List<modelClass> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        upload = uploads.get(position);

        holder.workTitle.setText(uploads.get(position).getWorkTitle());
        holder.workContent.setText(uploads.get(position).getWorkContent());
        holder.workTime.setText("At "+uploads.get(position).getWorkTime());
        holder.day.setText(uploads.get(position).getWeekDay());
        holder.date.setText(uploads.get(position).getOneTimeDate());
//        holder.myCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "at: "+position, Toast.LENGTH_SHORT).show();
//                String myId = String.valueOf(uploads.get(position).getId());
//                String where = "id=?";
//                sqLiteDatabase.delete("dailyWorkTwo", where, new String[]{myId});
//
//                uploads.remove(position);
//               // myAdapter.notifyDataSetChanged();
//                notifyItemRemoved(position);
//                MyAdapter.this.notifyDataSetChanged();
//
//            }
//        });



    }

    @Override
    final public int getItemCount() {
        return uploads.size();
    }

//    public void setOnItemClickListener(ValueEventListener Listener) {
////        mListener = (OnItemClickListener) Listener;
//        l = Listener;
//    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
            , MenuItem.OnMenuItemClickListener {


        public TextView workTitle, workContent, workTime,date,day;
        public CheckBox myCheckBox;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            workTitle = itemView.findViewById(R.id.tv_list_workTitle);
            workContent = itemView.findViewById(R.id.list_tv_content);
            workTime = itemView.findViewById(R.id.list_time);
            date = itemView.findViewById(R.id.list_date);
            day = itemView.findViewById(R.id.list_day);
            myCheckBox = itemView.findViewById(R.id.test_checkBox);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);



            //creating Database.
            //creating Database.
            try {
                dbHandler = new mySQLiteDBHelper(context, "dbDayPlanner", null, 6);
                sqLiteDatabase = dbHandler.getWritableDatabase();
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS dailyWorkTwo(id INTEGER PRIMARY KEY AUTOINCREMENT,workTitle TEXT,workContent TEXT,workTime TEXT,workDate TEXT,workDay TEXT,workType TEXT)");

            } catch (Exception e) {
                Toast.makeText(context, "DataBase Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


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
//            MenuItem delete = menu.add(Menu.NONE,2,2,"Supprimer");
            edit.setOnMenuItemClickListener(this);
//            delete.setOnMenuItemClickListener(this);
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

    public void removeItem(int position) {
        uploads.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(modelClass item, int position) {
        uploads.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
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
