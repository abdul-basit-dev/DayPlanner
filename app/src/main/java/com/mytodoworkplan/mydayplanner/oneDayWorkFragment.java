package com.mytodoworkplan.mydayplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


public class oneDayWorkFragment extends Fragment implements adapterForOneTimeWork_rv.OnItemClickListener, RecyclerItemTouchHelperOT.RecyclerItemTouchHelperListener {
    private DatePicker mDatePicker;
    int year, month, day;
    String date;


    private ArrayList<modelClass> mList;
    private RecyclerView recyclerView;
    private adapterForOneTimeWork_rv myAdapter;

    private mySQLiteDBHelper dbHandler;
    private SQLiteDatabase sqLiteDatabase;
    String workTitle, workType, workContent, workTime, oneTimewrokDate, workDate, workDay;
    int id;
    private AdView mAdView;


    public oneDayWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // readData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one_day_work, container, false);


        //Initilizing add.
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//Initilizing add ends here./////


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperOT(0, ItemTouchHelper.LEFT, oneDayWorkFragment.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);




        mDatePicker = rootView.findViewById(R.id.mDatePicker);
        year = mDatePicker.getYear();
        month = mDatePicker.getMonth() + 1;
        //mMonth =calendar.get(Calendar.MONTH)+1;
        day = mDatePicker.getDayOfMonth();
        date = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date = Integer.toString(year) + Integer.toString((monthOfYear + 1)) + Integer.toString(dayOfMonth);
                }
            });
        }


        //creating Database.
        try {
            dbHandler = new mySQLiteDBHelper(getActivity(), "dbDayPlanner", null, 6);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS dailyWorkTwo(id INTEGER PRIMARY KEY AUTOINCREMENT,workTitle TEXT,workContent TEXT,workTime TEXT,workDate TEXT,workDay TEXT,workType TEXT)");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "DataBase Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mList = new ArrayList<>();
        myAdapter = new adapterForOneTimeWork_rv(getActivity(), mList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(oneDayWorkFragment.this);

        //Click listeners for add.
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // Toast.makeText(getActivity(), "The Add have been loaded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
              //  Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        readData();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    private void readData() {

        String query = "Select * from dailyWorkTwo where worktype='One Time' AND workDate >=" + date;

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            mList.clear();
            if (cursor.moveToFirst()) {
                do {
                    workTitle = cursor.getString(cursor.getColumnIndex("workTitle"));
                    workContent = cursor.getString(cursor.getColumnIndex("workContent"));
                    workTime = cursor.getString(cursor.getColumnIndex("workTime"));
                    workDate = cursor.getString(cursor.getColumnIndex("workDate"));
                    workDay = cursor.getString(cursor.getColumnIndex("workDay"));
                    workType = cursor.getString(cursor.getColumnIndex("workType"));
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    // Toast.makeText(getActivity(), "id: "+workDate, Toast.LENGTH_SHORT).show();
                    String formattedDate;
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMd");
                    Date date1 = formatter2.parse(workDate);
                    formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date1);
//                    mList.add(new modelClass(workTitle,formattedDate,workContent,workTime));
                    mList.add(new modelClass(id, workTitle, workType, workContent, workTime, formattedDate, workDay));
                } while (cursor.moveToNext());
            }
            cursor.close();
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Read Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(int position) {

    }

//    @Override
//    public void onDeleteClick(int position) {
//
//
//
//        String myId = String.valueOf(mList.get(position).getId());
//        String where = "id=?";
//        sqLiteDatabase.delete("dailyWorkTwo", where, new String[]{myId});
//
//        mList.remove(position);
//        myAdapter.notifyDataSetChanged();
//
//
//
//    }

    @Override
    public void onEditClick(int position) {


        Intent intent = new Intent(getActivity(), editActivity.class);
        intent.putExtra("wid",mList.get(position).getId());
        intent.putExtra("wtitle",mList.get(position).getWorkTitle());
        intent.putExtra("wcontent",mList.get(position).getWorkContent());
        intent.putExtra("wtype",mList.get(position).getWorkType());
        intent.putExtra("wtime",mList.get(position).getWorkTime());
        intent.putExtra("wdate",mList.get(position).getOneTimeDate());
        intent.putExtra("wday",mList.get(position).getWeekDay());

        startActivity(intent);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof adapterForOneTimeWork_rv.ViewHolder) {

            final String myId = String.valueOf(mList.get(position).getId());
            final String workTitle = mList.get(position).getWorkTitle();
            String where = "id=?";
            sqLiteDatabase.delete("dailyWorkTwo", where, new String[]{myId});
            mList.remove(position);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), MyAlarmServices.class);
            intent.putExtra("title", "title");
            intent.putExtra("message", "message");

            final PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }


            myAdapter.notifyDataSetChanged();

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), workTitle + " Permanently removed from planning!",Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
//                mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
