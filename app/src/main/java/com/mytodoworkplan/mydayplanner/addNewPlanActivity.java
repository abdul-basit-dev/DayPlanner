package com.mytodoworkplan.mydayplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class addNewPlanActivity extends AppCompatActivity {
    String date, finalTime, finalDate, message, title,dateToalarm;
    int notificationId = 1;

    SimpleDateFormat simpleDateFormat, dateFormat;
    Date selectedTime, selectedDate;

    private mySQLiteDBHelper dbHandler;
    private SQLiteDatabase sqLiteDatabase;

    ImageView imageViewBack, imageViewDone;
    EditText editText_wrokName, editText_Content;
    Spinner spinnerWorkType;
    ArrayList<String> mArrayWorkType;
    ArrayAdapter<String> mAdapterWorkType;
    String selectedItem, am_pm, dailySelectedTime;
    int hour, minute, mYear, mMonth, mDay;
    Calendar calendar;
    AlarmManager alarmManager;


    LinearLayout layout_daily_time, layout_daily_time_picker, layout_oneTime_day_a, layout_oneTime_time_b, layout_oneTime_timePicker;
    LinearLayout layout_ontTime_datePicker, layout_week_day_a, layout_week_time_b, layout_week_timePicker, layout_week_days;
    TimePicker timePicker_daily, timePicker_oneTIme, timePicker_week;
    DatePicker datePicker_oneTime;
    TextView tv_daily_time, tv_oneTime_time, tv_oneTime_day, tv_week_day, tv_week_time;
    Button btnMonday, btnTuesday, btnWednesday, btnthursday, btnFriday, btnSaturday, btnSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plan);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_work_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //creating Database.
        try {
            dbHandler = new mySQLiteDBHelper(this, "dbDayPlanner", null, 6);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS dailyWorkTwo(id INTEGER PRIMARY KEY AUTOINCREMENT,workTitle TEXT,workContent TEXT,workTime TEXT,workDate TEXT,workDay TEXT,workType TEXT)");

        } catch (Exception e) {
            Toast.makeText(this, "DataBase Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        //initilize toolbar components
        imageViewBack = toolbar.findViewById(R.id.ivBack);
        imageViewDone = toolbar.findViewById(R.id.ivDone);


        //initilize controls
        editText_Content = findViewById(R.id.editText_Content);
        editText_wrokName = findViewById(R.id.editText_workName);
        spinnerWorkType = findViewById(R.id.spinnerWork);

        layout_daily_time = findViewById(R.id.layout_daily_time);
        layout_daily_time_picker = findViewById(R.id.layout_daily_time_picker);
        layout_oneTime_day_a = findViewById(R.id.layout_oneTime_day_a);
        layout_oneTime_time_b = findViewById(R.id.layout_oneTime_time_b);
        layout_oneTime_timePicker = findViewById(R.id.layout_oneTime_timepicker);
        layout_ontTime_datePicker = findViewById(R.id.layout_oneTime_datePicker);
        tv_daily_time = findViewById(R.id.tv_daily_time);
        tv_oneTime_day = findViewById(R.id.tv_oneTime_day);
        tv_oneTime_time = findViewById(R.id.tv_oneTime_time);
        timePicker_daily = findViewById(R.id.timePicker_daily);
        timePicker_oneTIme = findViewById(R.id.timePicker_oneTime);
        datePicker_oneTime = findViewById(R.id.datePicker_oneTime);

        //WEEK//
        layout_week_day_a = findViewById(R.id.layout_week_day_a);
        layout_week_time_b = findViewById(R.id.layout_week_time_b);
        layout_week_days = findViewById(R.id.layout_week_days);
//        layout_week_datePicker= findViewById(R.id.layout_week_datePicker);
        layout_week_timePicker = findViewById(R.id.layout_week_timepicker);
        tv_week_day = findViewById(R.id.tv_week_day);
        tv_week_time = findViewById(R.id.tv_week_time);
        timePicker_week = findViewById(R.id.timePicker_week);
//        datePicker_week = findViewById(R.id.datePicker_week);

        //DAYS OF WEEK-BUTTONS
        btnMonday = findViewById(R.id.btnMonday);
        btnTuesday = findViewById(R.id.btnTuesday);
        btnWednesday = findViewById(R.id.btnWednesday);
        btnthursday = findViewById(R.id.btnThursday);
        btnFriday = findViewById(R.id.btnFriday);
        btnSaturday = findViewById(R.id.btnSaturday);
        btnSunday = findViewById(R.id.btnSunday);


        //initilize array for spinner
        mArrayWorkType = new ArrayList<>();
        mArrayWorkType.add("Daily");
        mArrayWorkType.add("Weekly");
        mArrayWorkType.add("One Time");
        mAdapterWorkType = new ArrayAdapter<>(addNewPlanActivity.this, R.layout.spinnerlayout, mArrayWorkType);
        spinnerWorkType.setAdapter(mAdapterWorkType);
        spinnerWorkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                //  Toast.makeText(addNewPlanActivity.this, "" + selectedItem, Toast.LENGTH_SHORT).show();
                if (selectedItem.equalsIgnoreCase("Daily")) {
                    if (layout_daily_time.getVisibility() == View.GONE) {
                        layout_daily_time.setVisibility(View.VISIBLE);

                        layout_oneTime_day_a.setVisibility(View.GONE);
                        layout_oneTime_time_b.setVisibility(View.GONE);

                        layout_oneTime_timePicker.setVisibility(View.GONE);
                        layout_ontTime_datePicker.setVisibility(View.GONE);

                        layout_week_day_a.setVisibility(View.GONE);
                        layout_week_time_b.setVisibility(View.GONE);

                        layout_week_timePicker.setVisibility(View.GONE);
                        layout_week_days.setVisibility(View.GONE);
//                        layout_week_datePicker.setVisibility(View.GONE);

                    }
                } else if (selectedItem.equalsIgnoreCase("One Time")) {

                    if (layout_oneTime_day_a.getVisibility() == View.GONE && layout_oneTime_time_b.getVisibility() == View.GONE) {
                        layout_oneTime_day_a.setVisibility(View.VISIBLE);
                        layout_oneTime_time_b.setVisibility(View.VISIBLE);

                        layout_daily_time.setVisibility(View.GONE);
                        layout_daily_time_picker.setVisibility(View.GONE);

                        layout_week_day_a.setVisibility(View.GONE);
                        layout_week_time_b.setVisibility(View.GONE);

                        layout_week_timePicker.setVisibility(View.GONE);
                        layout_week_days.setVisibility(View.GONE);
                        // layout_week_datePicker.setVisibility(View.GONE);

                    }


                } else if (selectedItem.equalsIgnoreCase("Weekly")) {
                    //Toast.makeText(addNewPlanActivity.this, ""+selectedItem, Toast.LENGTH_SHORT).show();
                    if (layout_week_day_a.getVisibility() == View.GONE && layout_week_time_b.getVisibility() == View.GONE) {
                        layout_week_day_a.setVisibility(View.VISIBLE);
                        layout_week_time_b.setVisibility(View.VISIBLE);

                        layout_oneTime_day_a.setVisibility(View.GONE);
                        layout_oneTime_time_b.setVisibility(View.GONE);

                        layout_oneTime_timePicker.setVisibility(View.GONE);
                        layout_ontTime_datePicker.setVisibility(View.GONE);

                        layout_daily_time.setVisibility(View.GONE);
                        layout_daily_time_picker.setVisibility(View.GONE);

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/////////////////////             Daily              /////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        timePicker_daily.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker_daily.getHour();
            minute = timePicker_daily.getMinute();
        } else {
            hour = timePicker_daily.getCurrentHour();
            minute = timePicker_daily.getCurrentMinute();
        }
//        if (hour > 12) {
//            am_pm = "PM";
//            hour = hour - 12;
//        } else {
//            am_pm = "AM";
//        }


        // tv_daily_time.setText("At: " + hour + " : " + minute + "  " + am_pm);
//        tv_daily_time.setText(String.format("%02d:%02d", hour, minute));
        dailySelectedTime = String.format("%02d", hour) + ":" + minute;

        //dailySelectedTime="%02d:%02d"+ hour+minute;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Setting time for Daily Work.
//Formatting Time Instance
        try {
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            selectedTime = simpleDateFormat.parse(dailySelectedTime);
            finalTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedTime);
            tv_daily_time.setText(dailySelectedTime);
            //   Toast.makeText(this, "Time:" + dailySelectedTime, Toast.LENGTH_SHORT).show();


        } catch (ParseException e) {
            e.printStackTrace();
        }
//Click listener On Daily Time View.(TEXTVIEW)
        tv_daily_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup timePickerDaily = (ViewGroup) findViewById(R.id.timePicker_daily);
                Animation bottomUp = AnimationUtils.loadAnimation(addNewPlanActivity.this, R.anim.bottom_up);
                if (layout_daily_time_picker.getVisibility() == View.GONE) {
                    layout_daily_time_picker.setVisibility(View.VISIBLE);
                    layout_oneTime_timePicker.setVisibility(View.GONE);
                    layout_ontTime_datePicker.setVisibility(View.GONE);
                    timePickerDaily.startAnimation(bottomUp);
                }


            }
        });

//Click listner for time change(TIME PICKER)


        timePicker_daily.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (Build.VERSION.SDK_INT >= 23) {
                    hourOfDay = timePicker_daily.getHour();
                    minute = timePicker_daily.getMinute();
                } else {
                    hourOfDay = timePicker_daily.getCurrentHour();
                    minute = timePicker_daily.getCurrentMinute();
                }
//                if (hourOfDay > 12) {
//                    am_pm = "PM";
//                    hourOfDay = hourOfDay - 12;
//                } else {
//                    am_pm = "AM";
//                }
                dailySelectedTime = String.format("%02d", hourOfDay) + ":" + minute;
                try {
                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                    selectedTime = simpleDateFormat.parse(dailySelectedTime);
                    finalTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedTime);
                    tv_daily_time.setText(dailySelectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////// End Daily ////////////////////////


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////// Start One Time ////////////////////////

        timePicker_oneTIme.setIs24HourView(true);
        tv_oneTime_time.setText(dailySelectedTime); //setting current time to TextView-oneTime Work


        //Setting up click listener to open Time Picker for One Time Work
        tv_oneTime_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(addNewPlanActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                ViewGroup timePickerOneTime = (ViewGroup) findViewById(R.id.timePicker_oneTime);
                Animation bottomUp = AnimationUtils.loadAnimation(addNewPlanActivity.this, R.anim.bottom_up);

                if (layout_oneTime_timePicker.getVisibility() == View.GONE) {
                    layout_oneTime_timePicker.setVisibility(View.VISIBLE);

                    layout_ontTime_datePicker.setVisibility(View.GONE);
                    layout_daily_time_picker.setVisibility(View.GONE);
                    layout_week_timePicker.setVisibility(View.GONE);
                    layout_week_days.setVisibility(View.GONE);
                    ///layout_week_datePicker.setVisibility(View.GONE);
                    timePickerOneTime.startAnimation(bottomUp);
                }
            }
        });
        //Set up a click listener on One Time Work Time Picker
        timePicker_oneTIme.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (Build.VERSION.SDK_INT >= 23) {
                    hourOfDay = timePicker_oneTIme.getHour();
                    minute = timePicker_oneTIme.getMinute();
                } else {
                    hourOfDay = timePicker_oneTIme.getCurrentHour();
                    minute = timePicker_oneTIme.getCurrentMinute();
                }
//                if (hourOfDay > 12) {
//                    am_pm = "PM";
//                    hourOfDay = hourOfDay - 12;
//                } else {
//                    am_pm = "AM";
//                }

                dailySelectedTime = String.format("%02d", hourOfDay) + ":" + minute;
                try {
                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                    selectedTime = simpleDateFormat.parse(dailySelectedTime);
                    finalTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedTime);
                    tv_oneTime_time.setText(dailySelectedTime);
                    // Toast.makeText(addNewPlanActivity.this, "Time:" + finalTime, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        //DATE PICKER//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////DATE PICKER STARTS HERE///////////////////////////////////////////////////////
/////////////////////FIRST WE GET INSTANCE OF DAILY'S DATE, THEN WE HAVE AN DATE CAHANGE LISTENER IN ONE TIME WORK TO SELECT FUTURE DATE.
        mYear = datePicker_oneTime.getYear();
        mMonth = datePicker_oneTime.getMonth() + 1;
        mDay = datePicker_oneTime.getDayOfMonth();

        date = Integer.toString(datePicker_oneTime.getYear()) + Integer.toString(mMonth) + Integer.toString(datePicker_oneTime.getDayOfMonth());
        try {
            dateFormat = new SimpleDateFormat("yyyyMdd");
            selectedDate = dateFormat.parse(date);
            finalDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate);
            dateToalarm = DateFormat.getDateInstance(DateFormat.SHORT).format(selectedDate);
            tv_oneTime_day.setText(finalDate);
             // Toast.makeText(addNewPlanActivity.this, "Date:" + dateToalarm, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Setting up Day for One Day(OD)
        tv_oneTime_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup dateOneDay = (ViewGroup) findViewById(R.id.datePicker_oneTime);
                Animation bottomUp = AnimationUtils.loadAnimation(addNewPlanActivity.this, R.anim.bottom_up);

                if (layout_ontTime_datePicker.getVisibility() == View.GONE) {
                    layout_ontTime_datePicker.setVisibility(View.VISIBLE);
                    layout_oneTime_timePicker.setVisibility(View.GONE);
                    layout_daily_time_picker.setVisibility(View.GONE);
                    layout_week_days.setVisibility(View.GONE);
                    // layout_week_datePicker.setVisibility(View.GONE);
                    layout_week_timePicker.setVisibility(View.GONE);
                    dateOneDay.startAnimation(bottomUp);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker_oneTime.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // tv_oneTime_day.setText("Day: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    date = Integer.toString(year) + Integer.toString((monthOfYear + 1)) + Integer.toString(dayOfMonth);

                    try {
                        dateFormat = new SimpleDateFormat("yyyyMdd");
                        selectedDate = dateFormat.parse(date);
                        finalDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate);
                        dateToalarm = DateFormat.getDateInstance(DateFormat.SHORT).format(selectedDate);
                        tv_oneTime_day.setText(finalDate);
                        //  Toast.makeText(addNewPlanActivity.this, "DATE:" + finalDate, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //ENDS ONE TIME WORK
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////new ///

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////// Start WEEK WORK ////////////////////////

        timePicker_week.setIs24HourView(true);
        tv_week_time.setText(dailySelectedTime); //setting current time to TextView-oneTime Work


        //Setting up click listener to open Time Picker for One Time Work
        tv_week_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(addNewPlanActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                ViewGroup timePickerOneTime = (ViewGroup) findViewById(R.id.timePicker_week);
                Animation bottomUp = AnimationUtils.loadAnimation(addNewPlanActivity.this, R.anim.bottom_up);

                if (layout_week_timePicker.getVisibility() == View.GONE) {
                    layout_week_timePicker.setVisibility(View.VISIBLE);

                    layout_oneTime_timePicker.setVisibility(View.GONE);
                    layout_week_days.setVisibility(View.GONE);
                    // layout_week_datePicker.setVisibility(View.GONE);
                    layout_ontTime_datePicker.setVisibility(View.GONE);
                    layout_daily_time_picker.setVisibility(View.GONE);
                    timePickerOneTime.startAnimation(bottomUp);
                }
            }
        });
        //Set up a click listener on One Time Work Time Picker

        timePicker_week.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (Build.VERSION.SDK_INT >= 23) {
                    hourOfDay = timePicker_week.getHour();
                    minute = timePicker_week.getMinute();
                } else {
                    hourOfDay = timePicker_week.getCurrentHour();
                    minute = timePicker_week.getCurrentMinute();
                }
//                if (hourOfDay > 12) {
//                    am_pm = "PM";
//                    hourOfDay = hourOfDay - 12;
//                } else {
//                    am_pm = "AM";
//                }

                dailySelectedTime = String.format("%02d", hourOfDay) + ":" + minute;
                try {
                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                    selectedTime = simpleDateFormat.parse(dailySelectedTime);
                    finalTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedTime);
                    tv_week_time.setText(dailySelectedTime);
                    // Toast.makeText(addNewPlanActivity.this, "Time:" + dailySelectedTime, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        //DATE PICKER//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////DATE PICKER STARTS HERE///////////////////////////////////////////////////////
/////////////////////FIRST WE GET INSTANCE OF DAILY'S DATE, THEN WE HAVE AN DATE CAHANGE LISTENER IN ONE TIME WORK TO SELECT FUTURE DATE.
//        mYear = datePicker_week.getYear();
//        mMonth = datePicker_week.getMonth() + 1;
//        mDay = datePicker_oneTime.getDayOfMonth();

        // date = Integer.toString(datePicker_oneTime.getYear()) + Integer.toString(mMonth) + Integer.toString(datePicker_oneTime.getDayOfMonth());
        try {
            dateFormat = new SimpleDateFormat("yyyyMdd");
            selectedDate = dateFormat.parse(date);
            finalDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate);
            // tv_week_day.setText(finalDate);
            //  Toast.makeText(addNewPlanActivity.this, "Date:" + finalDate, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Setting up Day for One Day(OD)
        tv_week_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup dateOneDay = (ViewGroup) findViewById(R.id.layout_week_days);
                Animation rightTOleft = AnimationUtils.loadAnimation(addNewPlanActivity.this, R.anim.left_to_right);

                if (layout_week_days.getVisibility() == View.GONE) {
                    layout_week_days.setVisibility(View.VISIBLE);
                    layout_week_timePicker.setVisibility(View.GONE);

                    layout_oneTime_timePicker.setVisibility(View.GONE);
                    layout_ontTime_datePicker.setVisibility(View.GONE);
                    layout_daily_time_picker.setVisibility(View.GONE);
                    dateOneDay.startAnimation(rightTOleft);
                }
            }
        });


        //ENDS ONE TIME WORK
        calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                // Current day is Monday
                tv_week_day.setText("MONDAY");
                btnMonday.setAlpha((float) 0.5);
                break;
            case Calendar.TUESDAY:
                tv_week_day.setText("TUESDAY");
                btnTuesday.setAlpha((float) 0.5);
                break;
            case Calendar.WEDNESDAY:
                tv_week_day.setText("WEDNESDAY");
                btnWednesday.setAlpha((float) 0.5);
                break;

            case Calendar.THURSDAY:
                tv_week_day.setText("THURSDAY");
                btnthursday.setAlpha((float) 0.5);
                break;
            case Calendar.FRIDAY:
                tv_week_day.setText("FRIDAY");
                btnFriday.setAlpha((float) 0.5);

                break;
            case Calendar.SATURDAY:
                tv_week_day.setText("SATURDAY");
                btnSaturday.setAlpha((float) 0.5);
                break;
            case Calendar.SUNDAY:
                tv_week_day.setText("SUNDAY");
                btnSunday.setAlpha((float) 0.5);
                break;

        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Setting Up click listener to go back..
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Setting up click listner to add Work(Done)


        //setting up listener to change keyboard focus for : Work Name
        editText_wrokName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        //setting up listener to change keyboard focus for : Content
        editText_Content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


//        //set Alarm
//        //set type,milliseconds,intent

        imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set notificationID and text
                message = editText_Content.getText().toString().trim();
                title = editText_wrokName.getText().toString().trim();
                Intent intent = new Intent(addNewPlanActivity.this, MyAlarmServices.class);
                intent.putExtra("notificationID", notificationId);
                intent.putExtra("title", title);
                intent.putExtra("message", message);

                final PendingIntent pendingIntent = PendingIntent.getBroadcast(addNewPlanActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //Creating Time
                String[] partA = dailySelectedTime.split(":");
                String[] partB = dateToalarm.split("/");

              //  Toast.makeText(addNewPlanActivity.this, "Day"+Integer.parseInt(partB[0]), Toast.LENGTH_SHORT).show();
                Calendar startTime = Calendar.getInstance();
                startTime.setTimeInMillis(System.currentTimeMillis());
                startTime.set(Calendar.YEAR,Integer.parseInt(partB[2]));
                startTime.set(Calendar.MONTH,Integer.parseInt(partB[1])-1);
                startTime.set(Calendar.DAY_OF_MONTH,Integer.parseInt(partB[0]));

                startTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(partA[0]));
                startTime.set(Calendar.MINUTE,Integer.parseInt(partA[1]));
                startTime.set(Calendar.SECOND, 0);

               // Toast.makeText(addNewPlanActivity.this, ""+Integer.parseInt(partA[0])+"\t"+Integer.parseInt(partA[1]), Toast.LENGTH_SHORT).show();
                //startTime.set();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.clear();

               // startTime.set(2020, mMonth+1 , 8,Integer.parseInt(partA[0]) ,Integer.parseInt(partA[1]));
                final long alarmStartTime = startTime.getTimeInMillis();
//                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime, AlarmManager.INTERVAL_DAY, pendingIntent);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), TimeUnit.DAYS.toMillis(1), pendingIntent);
                insertIntoDB();
                if (getApplicationContext() != null) {
                    onBackPressed();
                }
            }
        });


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    //FUNCTION TO SEND DATA TO DATABASE
    public void insertIntoDB() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("workTitle", editText_wrokName.getText().toString());
        contentValues.put("workContent", editText_Content.getText().toString());
        contentValues.put("workTime", dailySelectedTime);
        contentValues.put("workDate", date);
        contentValues.put("workDay", tv_week_day.getText().toString());
        contentValues.put("workType", selectedItem);


        sqLiteDatabase.insert("dailyWorkTwo", null, contentValues);
        //sqLiteDatabase.close();
        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

    }


    public void buttonOnClick(View view) {

        switch (view.getId()) {
            case R.id.btnMonday:
                // Code for button 1 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                btnMonday.setAlpha((float) 0.5);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 1.0);

                tv_week_day.setText("MONDAY");
                break;

            case R.id.btnTuesday:
                // Code for button 2 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 0.5);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 1.0);
                tv_week_day.setText("TUESDAY");
                break;

            case R.id.btnWednesday:
                // Code for button 3 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 0.5);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 1.0);
                tv_week_day.setText("WEDNESDAY");
                break;
            case R.id.btnThursday:
                // Code for button 3 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 0.5);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 1.0);
                tv_week_day.setText("THURSDAY");
                break;
            case R.id.btnFriday:
                // Code for button 3 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 0.5);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 1.0);
                tv_week_day.setText("FRIDAY");
                break;
            case R.id.btnSaturday:
                // Code for button 3 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 0.5);
                btnSunday.setAlpha((float) 1.0);
                tv_week_day.setText("SATURDAY");
                break;
            case R.id.btnSunday:
                // Code for button 3 click
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                btnMonday.setAlpha((float) 1.0);
                btnTuesday.setAlpha((float) 1.0);
                btnWednesday.setAlpha((float) 1.0);
                btnthursday.setAlpha((float) 1.0);
                btnFriday.setAlpha((float) 1.0);
                btnSaturday.setAlpha((float) 1.0);
                btnSunday.setAlpha((float) 0.5);
                tv_week_day.setText("SUNDAY");
                break;
        }
    }
}
