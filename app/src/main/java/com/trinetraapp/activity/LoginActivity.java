package com.trinetraapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.trinetraapp.MainActivity;
import com.trinetraapp.R;
import com.trinetraapp.databinding.ActivityLoginBinding;
import com.trinetraapp.model.RegistrationModel;
import com.trinetraapp.model.login_model.LoginModel;
import com.trinetraapp.utils.Api_Call;
import com.trinetraapp.utils.Connectivity;
import com.trinetraapp.utils.RxApiClient;
import com.trinetraapp.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.trinetraapp.utils.Base_Url.BaseUrl;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SessionManager sessionManager;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sessionManager = new SessionManager(this);

        binding.tvDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                intent.putExtra("user_type", "Donor");
                startActivity(intent);
            }
        });

        binding.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                intent.putExtra("user_type", "User");
                startActivity(intent);
            }
        });


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_mobile = binding.etMobile.getText().toString();
                if (!et_mobile.isEmpty()) {

                    if (et_mobile.length() < 9) {
                        Toast.makeText(LoginActivity.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Connectivity.isConnected(LoginActivity.this)) {
                            LoginApi(et_mobile);
                        } else {
                            Toast.makeText(LoginActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressLint("CheckResult")
    private void LoginApi(String et_mobile) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_phone", et_mobile);

        apiInterface.LoginApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginModel>() {
                    @Override
                    public void onNext(LoginModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());

                            if (response.getResponse()) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                sessionManager.setLoginData(response.getData());

                                for (int i=0; i<response.getData().size(); i++){
                                    user_id=response.getData().get(i).getUserId();
                                }

                                sessionManager.setUser_Id(user_id);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            } else {
                                Toast.makeText(LoginActivity.this, "" + response.getError(), Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("Categ_product_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }
}
