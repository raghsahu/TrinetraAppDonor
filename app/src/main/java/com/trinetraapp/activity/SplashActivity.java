package com.trinetraapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.trinetraapp.R;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            } else {
//
//                /* New Handler to start the Menu-Activity
//                 * and close this Splash-Screen after some seconds.*/


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  if (sessionManager.isLoggedIn()){
//                    Intent mainIntent = new Intent(Splash_Screen.this, MainActivity.class);
//                    startActivity(mainIntent);
//                    finish();
                        //  }else {
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();
                        //  }


                    }
                }, 3000);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


    }



        @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        System.out.println(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[1] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[2] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[3] == PackageManager.PERMISSION_GRANTED);



        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 7 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED


            ) {
                /* New Handler to start the Menu-Activity
                 * and close this Splash-Screen after some seconds.*/

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  if (sessionManager.isLoggedIn()){
//                    Intent mainIntent = new Intent(Splash_Screen.this, MainActivity.class);
//                    startActivity(mainIntent);
//                    finish();
                        //  }else {
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();
                        //  }


                    }
                }, 3000);

            } else {
                Toast.makeText(SplashActivity.this, "Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
