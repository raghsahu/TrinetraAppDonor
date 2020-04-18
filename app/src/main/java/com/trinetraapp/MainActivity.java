package com.trinetraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.trinetraapp.activity.Donor_Registration;
import com.trinetraapp.activity.LoginActivity;
import com.trinetraapp.activity.Payment_History_Activity;
import com.trinetraapp.activity.Profile_Activity;
import com.trinetraapp.activity.SplashActivity;
import com.trinetraapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);


        SetGraph();

        binding.rlMenuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(MainActivity.this, binding.rlMenuMore);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    private Intent intent;

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_profile:
                                //handle menu1 click
                                intent=new Intent(MainActivity.this, Profile_Activity.class);
                                startActivity(intent);

                                return true;

                            case R.id.navigation_pay_history:
                                intent=new Intent(MainActivity.this, Payment_History_Activity.class);
                                startActivity(intent);
                                return true;

                            case R.id.logout:

                                intent=new Intent(MainActivity.this, SplashActivity.class);
                                startActivity(intent);

                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    private void SetGraph() {

        try {
            LineGraphSeries<DataPoint> series = new LineGraphSeries < > (new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(Integer.valueOf(10), Integer.valueOf(5)),
                    new DataPoint(Integer.valueOf(20), Integer.valueOf(10)),
                    new DataPoint(Integer.valueOf(30), Integer.valueOf(15)),
                    new DataPoint(Integer.valueOf(40), Integer.valueOf(5))
            });
            binding.graph.addSeries(series);
        } catch (IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        }

}
