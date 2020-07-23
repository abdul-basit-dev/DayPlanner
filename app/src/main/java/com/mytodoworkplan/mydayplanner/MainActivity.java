package com.mytodoworkplan.mydayplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    ImageView imageViewAdd, imageViewSettings;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Floating action button.
        fab = (FloatingActionButton) findViewById(R.id.fButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, addNewPlanActivity.class);
                startActivity(intent);
            }
        });

        //Initilize Navigation Menu
        imageViewAdd = toolbar.findViewById(R.id.ivAdd);
        imageViewSettings = toolbar.findViewById(R.id.ivSettings);
        bottomNavigation = toolbar.findViewById(R.id.my_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //Display a fragment in MainActivity
        replaceFragment(R.id.navigation_home);


        //Setting up click listener for adding new plane
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPlaneIntent = new Intent(MainActivity.this, addNewPlanActivity.class);
                startActivity(newPlaneIntent);

            }
        });

        //  Setting up click listener for settings
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add settings
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);


            }
        });


    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            // launch settings activity
//            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //function to swap fragments...
    public void replaceFragment(int id) {
        Fragment fragment = null;
        if (id == R.id.navigation_home) {
            fragment = new homeFragment();
        } else if (id == R.id.navigation_daily) {
            fragment = new dailyPlanFragment();
        } else if (id == R.id.navigation_weekly) {
            fragment = new weeklyFragment();
        } else if (id == R.id.navigation_one_day) {
            fragment = new oneDayWorkFragment();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    //Menu Navigation..
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:

                            replaceFragment(R.id.navigation_home);
                            return true;
                        case R.id.navigation_daily:

                            replaceFragment(R.id.navigation_daily);
                            return true;
                        case R.id.navigation_weekly:

                            replaceFragment(R.id.navigation_weekly);
                            return true;
                        case R.id.navigation_one_day:

                            replaceFragment(R.id.navigation_one_day);
                            return true;
                    }
                    return false;
                }
            };


}




