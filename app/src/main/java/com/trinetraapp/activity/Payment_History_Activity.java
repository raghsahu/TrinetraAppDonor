package com.trinetraapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.trinetraapp.R;
import com.trinetraapp.databinding.ActivityPaymentHistoryBinding;
import com.trinetraapp.databinding.ActivityPaymentHistoryBindingImpl;

public class Payment_History_Activity extends AppCompatActivity {
    ActivityPaymentHistoryBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment__history_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment__history);

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
