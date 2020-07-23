package com.mytodoworkplan.mydayplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class adapterForAll extends ArrayAdapter {

    Context context;
    ArrayList<modelClass> list;
    public adapterForAll(Context context,ArrayList<modelClass> list)
    {
        super(context,R.layout.layout_home,list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_home,null);


        TextView workTitle = view.findViewById(R.id.tv_list_workTitle);
        TextView workContent = view.findViewById(R.id.list_tv_content);
        TextView workTime = view.findViewById(R.id.list_time);

        workTitle.setText(list.get(position).getWorkTitle());
        workContent.setText(list.get(position).getWorkContent());
        workTime.setText(list.get(position).getWorkTime());



        return view;

    }
}
