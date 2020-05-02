package com.trinetraapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.trinetraapp.R;
import com.trinetraapp.databinding.ActivityTestHistoryBinding;

public class Test_History_Activity extends AppCompatActivity {
    ActivityTestHistoryBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_test__history_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_test__history_);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
