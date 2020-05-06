package com.trinetraapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.trinetraapp.MainActivity;
import com.trinetraapp.R;
import com.trinetraapp.databinding.ActivityProfileBinding;
import com.trinetraapp.model.StateData;
import com.trinetraapp.model.StateModel;
import com.trinetraapp.model.login_model.LoginModel;
import com.trinetraapp.model.profile_update_model;
import com.trinetraapp.utils.Api_Call;
import com.trinetraapp.utils.Connectivity;
import com.trinetraapp.utils.RxApiClient;
import com.trinetraapp.utils.SessionManager;
import com.trinetraapp.utils.Utilities;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

import static com.trinetraapp.utils.Base_Url.BaseUrl;
import static com.trinetraapp.utils.PathUtils.bitmapToFile;

public class Profile_Activity extends AppCompatActivity {
    ActivityProfileBinding binding;
    //String user_type;
    List<StateData> stateModelList=new ArrayList<>();
    List<StateData> cityModelList=new ArrayList<>();
    List<String> StateName=new ArrayList<>();
    List<String> CityName=new ArrayList<>();
    private String user_type_id,state_name,city_name;
    SessionManager sessionManager;
     String user_id;
     Utilities utilities;
    private File img_file;
    private MultipartBody.Part body;
    private boolean isPermitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_profile_);

        sessionManager = new SessionManager(this);
        user_id = sessionManager.getUser_Id();
        utilities = Utilities.getInstance(this);

        for (int i=0; i<sessionManager.getLoginData().size(); i++){
            binding.etName.setText(sessionManager.getLoginData().get(i).getUserFullname());
            binding.etDISTT.setText(sessionManager.getLoginData().get(i).getDistrict());
            binding.etSchName.setText(sessionManager.getLoginData().get(i).getSchoolName());
            binding.etClassName.setText(sessionManager.getLoginData().get(i).getClassName());
            binding.etSection.setText(sessionManager.getLoginData().get(i).getSection());
            binding.etMobile.setText(sessionManager.getLoginData().get(i).getUserPhone());
            binding.etAddress.setText(sessionManager.getLoginData().get(i).getAddress());
            binding.etReference.setText(sessionManager.getLoginData().get(i).getReference());
            user_type_id=sessionManager.getLoginData().get(i).getUserTypeId();
        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(this)){
            getState();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

        //post image show choose camera gallery
        binding.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission must be granted
                checkRunTimePermission();
            }
        });

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

                if (img_file==null){
                    if (user_type_id.equals("1")){
                        if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty() && !et_class_name.isEmpty()
                                && !et_section.isEmpty() && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                            if (et_mobile.length()<9){
                                Toast.makeText(Profile_Activity.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Connectivity.isConnected(Profile_Activity.this)){
                                    UpdateProfileApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                            et_mobile,et_address,et_reference);
                                }else {
                                    Toast.makeText(Profile_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else {
                            Toast.makeText(Profile_Activity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty()
                                && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                            if (et_mobile.length()<9){
                                Toast.makeText(Profile_Activity.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Connectivity.isConnected(Profile_Activity.this)){
                                    UpdateProfileApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                            et_mobile,et_address,et_reference);
                                }else {
                                    Toast.makeText(Profile_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }else {
                            Toast.makeText(Profile_Activity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    if (user_type_id.equals("1")){
                        if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty() && !et_class_name.isEmpty()
                                && !et_section.isEmpty() && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                            if (et_mobile.length()<9){
                                Toast.makeText(Profile_Activity.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Connectivity.isConnected(Profile_Activity.this)){
                                    UpdateProfileImageApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                            et_mobile,et_address,et_reference);
                                }else {
                                    Toast.makeText(Profile_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else {
                            Toast.makeText(Profile_Activity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        if (!state_name.isEmpty() && !city_name.isEmpty() && !et_distt.isEmpty() && !et_sch_name.isEmpty()
                                && !et_name.isEmpty() && !et_mobile.isEmpty() && !et_address.isEmpty() && !et_reference.isEmpty() ){

                            if (et_mobile.length()<9){
                                Toast.makeText(Profile_Activity.this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Connectivity.isConnected(Profile_Activity.this)){
                                    UpdateProfileImageApi(user_type_id,state_name,city_name,et_distt,et_sch_name,et_class_name,et_section,et_name,
                                            et_mobile,et_address,et_reference);
                                }else {
                                    Toast.makeText(Profile_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }else {
                            Toast.makeText(Profile_Activity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        });

    }

    @SuppressLint("CheckResult")
    private void UpdateProfileImageApi(String user_type_id, String state_name, String city_name, String et_distt,
                                       String et_sch_name, String et_class_name, String et_section, String et_name,
                                       String et_mobile, String et_address, String et_reference) {

        final ProgressDialog progressDialog = new ProgressDialog(Profile_Activity.this, R.style.MyGravity);
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
        map.put("user_id", user_id);

        if (img_file != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), img_file);
            body = MultipartBody.Part.createFormData("image", img_file.getName(), fileBody);
        }

        apiInterface.UpdateImageApi(map,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<profile_update_model>() {
                    @Override
                    public void onNext(profile_update_model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());

                            if (response.getResponse()) {
                                //Toast.makeText(Registration.this, "Register successful", Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(Profile_Activity.this, "Success", "Profile update successful", getString(R.string.ok), true);

                            } else {
                                Toast.makeText(Profile_Activity.this, "" + response.getError(), Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("update_img_error", e.toString());

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

    private void checkRunTimePermission() {

        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
          selectImage();


        }
    }

    private void selectImage() {

        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());

        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    binding.ivImg.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());
                    img_file = bitmapToFile(Profile_Activity.this, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(Profile_Activity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(Profile_Activity.this);
    }

    @SuppressLint("CheckResult")
    private void UpdateProfileApi(String user_type_id, String state_name, String city_name, String et_distt, String et_sch_name,
                                  String et_class_name, String et_section, String et_name, String et_mobile, String et_address,
                                  String et_reference) {

        final ProgressDialog progressDialog = new ProgressDialog(Profile_Activity.this, R.style.MyGravity);
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
        map.put("user_id", user_id);

        apiInterface.UpdateApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<profile_update_model>() {
                    @Override
                    public void onNext(profile_update_model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());

                            if (response.getResponse()) {
                                //Toast.makeText(Registration.this, "Register successful", Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(Profile_Activity.this, "Success", "Profile update successful", getString(R.string.ok), true);

                            } else {
                                Toast.makeText(Profile_Activity.this, "" + response.getError(), Toast.LENGTH_SHORT).show();

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
        final ProgressDialog progressDialog = new ProgressDialog(Profile_Activity.this, R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(Profile_Activity.this, android.R.layout.simple_spinner_dropdown_item, CityName);
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
        final ProgressDialog progressDialog = new ProgressDialog(Profile_Activity.this, R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(Profile_Activity.this, android.R.layout.simple_spinner_dropdown_item, StateName);
                                binding.spinState.setAdapter(adp);
                            } else {
                                Toast.makeText(Profile_Activity.this, response.getError(), Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(Profile_Activity.this, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            } catch (Exception e) {

            }

            if (isPermitted) {
                 selectImage();


            } else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
