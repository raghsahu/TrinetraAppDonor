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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.trinetraapp.MainActivity;
import com.trinetraapp.R;
import com.trinetraapp.databinding.ActivityUserRegistrationBinding;
import com.trinetraapp.databinding.ActivityUserRegistrationBindingImpl;
import com.trinetraapp.model.RegistrationModel;
import com.trinetraapp.model.StateData;
import com.trinetraapp.model.StateModel;
import com.trinetraapp.model.login_model.LoginModel;
import com.trinetraapp.utils.Api_Call;
import com.trinetraapp.utils.Connectivity;
import com.trinetraapp.utils.RxApiClient;
import com.trinetraapp.utils.SessionManager;
import com.trinetraapp.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.trinetraapp.utils.Base_Url.BaseUrl;

public class Registration extends AppCompatActivity {
    ActivityUserRegistrationBinding binding;
    String user_type;
    List<StateData> stateModelList=new ArrayList<>();
    List<StateData> cityModelList=new ArrayList<>();
    List<String> StateName=new ArrayList<>();
    List<String> CityName=new ArrayList<>();
    private String user_type_id,state_name,city_name;
    SessionManager sessionManager;
    private String user_id;
    private Utilities utilities;
    private String profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user__registration);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user__registration);
        sessionManager = new SessionManager(this);
        utilities = Utilities.getInstance(this);

        if (getIntent()!=null){
            user_type= getIntent().getStringExtra("user_type");
            profile=getIntent().getStringExtra("profile");
        }

        if (user_type.equals("Donor")){
            binding.tvUserType.setText("Donor");
        }else {
            binding.tvUserType.setText("User");
        }

            if (Connectivity.isConnected(this)){
                getState();
            }else {
                Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
            }

        binding.spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String  selecteditem =  adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (stateModelList!=null && !stateModelList.isEmpty()){
                    state_name=stateModelList.get(i).getCityState();
                    if (cityModelList!=null && !cityModelList.isEmpty()){
                        cityModelList.clear();
                        CityName.clear();
                    }

                    getCity(state_name);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

        binding.spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String  selecteditem =  adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];

                    if (cityModelList!=null && !cityModelList.isEmpty()){
                        city_name=cityModelList.get(i).getCity_name();
                    }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


        binding.tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String et_distt=binding.etDISTT.getText().toString();
                String et_sch_name=binding.etSchName.getText().toString();
                String et_class_name=binding.etClassName.getText().toString();
                String et_section=binding.etSection.getText().toString();
                String et_name=binding.etName.getText().toString();
                String et_mobile=binding.etMobile.getText().toString();
                String et_address=binding.etAddress.getText().toString();
                String et_reference=binding.etReference.getText().toString();

                if (user_type.equals("User")){
                    user_type_id ="1";
                    if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty() && !et_class_name.isEmpty()
                            && !et_section.isEmpty() && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                        if (et_mobile.length()<9){
                            Toast.makeText(Registration.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                        }else {
                            if (Connectivity.isConnected(Registration.this)){
                                RegisterApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                        et_mobile,et_address,et_reference);
                            }else {
                                Toast.makeText(Registration.this, "Please check internet", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else {
                        Toast.makeText(Registration.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    user_type_id ="2";
                    if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty()
                           && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                        if (et_mobile.length()<9){
                            Toast.makeText(Registration.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                        }else {
                            if (Connectivity.isConnected(Registration.this)){
                                RegisterApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                        et_mobile,et_address,et_reference);
                            }else {
                                Toast.makeText(Registration.this, "Please check internet", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else {
                        Toast.makeText(Registration.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    @SuppressLint("CheckResult")
    private void RegisterApi(final String user_type_id, String state_name, String city_name, String et_distt, String et_sch_name,
                             String et_class_name, String et_section, String et_name, String et_mobile, String et_address,
                             String et_reference) {
        final ProgressDialog progressDialog = new ProgressDialog(Registration.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_type_id", user_type_id);
        map.put("state", state_name);
        map.put("district", et_distt);
        map.put("city", city_name);
        map.put("school_name", et_sch_name);
        map.put("class_name", et_class_name);
        map.put("section", et_section);
        map.put("user_fullname", et_name);
        map.put("user_phone", et_mobile);
        map.put("address", et_address);
        map.put("reference", et_reference);


        apiInterface.RegisterApi(map)
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
                                //Toast.makeText(Registration.this, "Register successful", Toast.LENGTH_SHORT).show();

                                for (int i=0; i<response.getData().size(); i++){
                                    user_id=response.getData().get(i).getUserId();
                                }

//                                if(user_type_id.equals("1")){
//                                    sessionManager.setLoginData(response.getData());
//                                    sessionManager.setUser_Id(user_id);
//                                    Intent intent = new Intent(Registration.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }else {
//                                    sessionManager.setUser_Id(user_id);
//                                    Intent intent = new Intent(Registration.this, PaymentActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
                                utilities.dialogOK(Registration.this, "Success", "Register successful! \nPlease wait for admin approval. Thanks!", getString(R.string.ok), true);


                            } else {
                                Toast.makeText(Registration.this, "" + response.getError(), Toast.LENGTH_SHORT).show();

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
    private void getCity(String state_name) {
        final ProgressDialog progressDialog = new ProgressDialog(Registration.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.CityApi(state_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StateModel>() {
                    @Override
                    public void onNext(StateModel response) {
                        //Handle logic
                        try {
                            cityModelList.clear();
                            CityName.clear();
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResponse()) {
                                cityModelList=response.getData();
                                for (int i=0; i<response.getData().size(); i++){
                                    CityName.add(response.getData().get(i).getCity_name());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, CityName);
                                binding.spinCity.setAdapter(adp);
                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
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
    private void getState() {
          final ProgressDialog progressDialog = new ProgressDialog(Registration.this, R.style.MyGravity);
          progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
         progressDialog.setCancelable(false);
          progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.StateApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StateModel>() {
                    @Override
                    public void onNext(StateModel response) {
                        //Handle logic
                        try {
                            stateModelList.clear();
                            StateName.clear();
                              progressDialog.dismiss();
                           // Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResponse()) {
                                stateModelList=response.getData();
                                for (int i=0; i<response.getData().size(); i++){
                                    StateName.add(response.getData().get(i).getCityState());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, StateName);
                                binding.spinState.setAdapter(adp);
                            } else {
                               Toast.makeText(Registration.this, response.getError(), Toast.LENGTH_SHORT).show();
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
}
