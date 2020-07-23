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
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class homeFragment extends Fragment implements MyAdapter.OnItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    String date;
    String finalDate;
    TextView mText;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<modelClass> mList;


    SimpleDateFormat dateFormat;
    private Date selectedDate;

    private mySQLiteDBHelper dbHandler;
    private SQLiteDatabase sqLiteDatabase;
    private DatePicker mDatePicker;
    private int day, month, year, id;

    private String workTitle, workType, workContent, workTime, workDate, workDay, dayToCompare;
    Calendar calendar;

    private AdView mAdView;
    private ConstraintLayout container;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
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

        mDatePicker = rootView.findViewById(R.id.mDatePicker);
        mText = rootView.findViewById(R.id.mText);
        container = rootView.findViewById(R.id.container);

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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, homeFragment.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        year = mDatePicker.getYear();
        month = mDatePicker.getMonth() + 1;
        //mMonth =calendar.get(Calendar.MONTH)+1;
        day = mDatePicker.getDayOfMonth();
        date = Integer.toString(mDatePicker.getYear()) + Integer.toString(month) + Integer.toString(mDatePicker.getDayOfMonth());

        try {
            dateFormat = new SimpleDateFormat("yyyyMdd");
            selectedDate = dateFormat.parse(date);
            finalDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate);
            mText.setText("Today's date : " + finalDate);
            //Toast.makeText(getActivity(), "Date:"+finalDate, Toast.LENGTH_SHORT).show();


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date = Integer.toString(year) + Integer.toString((monthOfYear + 1)) + Integer.toString(dayOfMonth);
                    try {
                        dateFormat = new SimpleDateFormat("yyyyMdd");
                        selectedDate = dateFormat.parse(date);
                        finalDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //creating Database.
        //creating Database.
        try {
            dbHandler = new mySQLiteDBHelper(getActivity(), "dbDayPlanner", null, 6);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS dailyWorkTwo(id INTEGER PRIMARY KEY AUTOINCREMENT,workTitle TEXT,workContent TEXT,workTime TEXT,workDate TEXT,workDay TEXT,workType TEXT)");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "DataBase Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        //list = new ArrayList<>();
        mList = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(), mList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(homeFragment.this);

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
          // Toast.makeText(getActivity(), "Please Connect to Internet"+errorCode, Toast.LENGTH_SHORT).show();
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
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                // Current day is Monday
                dayToCompare = "MONDAY";

                break;
            case Calendar.TUESDAY:
                dayToCompare = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                dayToCompare = "WEDNESDAY";
                break;

            case Calendar.THURSDAY:
                dayToCompare = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                dayToCompare = "FRIDAY";

                break;
            case Calendar.SATURDAY:
                dayToCompare = "SATURDAY";

                break;
            case Calendar.SUNDAY:
                dayToCompare = "SUNDAY";

                break;

        }
//        Toast.makeText(getActivity(), "::"+dayToCompare, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        // readData();
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
//        String query = "Select * from dailyWorkTwo where workDate =" + date + " AND  workDay='" + dayToCompare + "'";
        String query = "Select * from dailyWorkTwo where worktype='Daily'";

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

                    String formattedDate;

                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMd");
                    Date date1 = formatter2.parse(workDate);


                    formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date1);

                    //Toast.makeText(getActivity(), "day: " + workDay, Toast.LENGTH_SHORT).show();
                    mList.add(new modelClass(id, workTitle, workType, workContent, workTime, formattedDate, workDay));
                } while (cursor.moveToNext());
            }
            cursor.close();
            myAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Read Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(int position) {
        String myId = String.valueOf(mList.get(position).getId());
        //  Toast.makeText(getActivity(), "Click at : " + position+mList.get(position).getWorkType(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), "Date: "+mList.get(position).getOneTimeDate(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDeleteClick(int position) {
        //  Toast.makeText(getActivity(), "Delete at : " + position, Toast.LENGTH_SHORT).show();
//        String myId = String.valueOf(mList.get(position).getId());
//        String where = "id=?";
//        sqLiteDatabase.delete("dailyWorkTwo", where, new String[]{myId});
//        mList.remove(position);
//        myAdapter.notifyDataSetChanged();
//
    }

    @Override
    public void onEditClick(int position) {
        // Toast.makeText(getActivity(), "Edit at : " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), editActivity.class);
        intent.putExtra("wid", mList.get(position).getId());
        intent.putExtra("wtitle", mList.get(position).getWorkTitle());
        intent.putExtra("wcontent", mList.get(position).getWorkContent());
        intent.putExtra("wtype", mList.get(position).getWorkType());
        intent.putExtra("wtime", mList.get(position).getWorkTime());
        intent.putExtra("wdate", mList.get(position).getOneTimeDate());
        intent.putExtra("wday", mList.get(position).getWeekDay());
        startActivity(intent);

    }
//Swipe to delete function....
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof MyAdapter.ViewHolder) {

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
