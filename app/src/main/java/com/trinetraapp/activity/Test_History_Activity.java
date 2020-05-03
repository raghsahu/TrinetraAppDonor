package com.trinetraapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.trinetraapp.MainActivity;
import com.trinetraapp.R;
import com.trinetraapp.adapter.TestHistoryAdapter;
import com.trinetraapp.databinding.ActivityTestHistoryBinding;
import com.trinetraapp.model.test_history.Test_History_Data;
import com.trinetraapp.model.test_history.Test_History_Model;
import com.trinetraapp.model.test_name.TestNameData;
import com.trinetraapp.model.test_name.TestNameModel;
import com.trinetraapp.utils.Api_Call;
import com.trinetraapp.utils.Connectivity;
import com.trinetraapp.utils.RxApiClient;
import com.trinetraapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.trinetraapp.utils.Base_Url.BaseUrl;

public class Test_History_Activity extends AppCompatActivity {
    ActivityTestHistoryBinding binding;
    List<TestNameData> testNameData = new ArrayList<>();
    List<Test_History_Data> testHistoryData = new ArrayList<>();
    List<String> TestName = new ArrayList<>();
    String TestId, user_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_test__history_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_test__history_);
        sessionManager=new SessionManager(this);
        user_id=sessionManager.getUser_Id();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

            if (Connectivity.isConnected(this)){
                getTest();
            }else {
                Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
            }


        binding.spinTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (testNameData != null && !testNameData.isEmpty()) {
                    TestId = testNameData.get(i).getTestId();
                    if (testHistoryData != null && !testHistoryData.isEmpty()) {
                        testHistoryData.clear();
                    }

                    getHistoryTest(TestId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


    }

    @SuppressLint("CheckResult")
    private void getHistoryTest(String testId) {
        final ProgressDialog progressDialog = new ProgressDialog(Test_History_Activity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.TestHistoryApi(testId,user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Test_History_Model>() {
                    @Override
                    public void onNext(Test_History_Model response) {
                        //Handle logic
                        try {
                            testHistoryData.clear();
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResponse()) {
                                testHistoryData = response.getData();

                                TestHistoryAdapter friendsAdapter = new TestHistoryAdapter(testHistoryData,Test_History_Activity.this);
                                binding.setMyAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {
                                binding.setMyAdapter(null);
                                  Toast.makeText(Test_History_Activity.this, response.getError(), Toast.LENGTH_SHORT).show();
//                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
//                                        response.getMsg(), getString(R.string.ok), true);
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


    @SuppressLint("CheckResult")
    private void getTest() {
        final ProgressDialog progressDialog = new ProgressDialog(Test_History_Activity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.TestNameApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TestNameModel>() {
                    @Override
                    public void onNext(TestNameModel response) {
                        //Handle logic
                        try {
                            testNameData.clear();
                            TestName.clear();
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResponse()) {
                                testNameData = response.getData();
                                for (int i = 0; i < response.getData().size(); i++) {
                                    TestName.add(response.getData().get(i).getTestName());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(Test_History_Activity.this, android.R.layout.simple_spinner_dropdown_item, TestName);
                                binding.spinTest.setAdapter(adp);
                            } else {
                                //  Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
//                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
//                                        response.getMsg(), getString(R.string.ok), true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
