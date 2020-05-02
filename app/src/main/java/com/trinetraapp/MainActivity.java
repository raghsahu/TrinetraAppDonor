package com.trinetraapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.trinetraapp.activity.LoginActivity;
import com.trinetraapp.activity.Payment_History_Activity;
import com.trinetraapp.activity.Profile_Activity;
import com.trinetraapp.activity.Registration;
import com.trinetraapp.activity.SplashActivity;
import com.trinetraapp.activity.Test_History_Activity;
import com.trinetraapp.databinding.ActivityMainBinding;
import com.trinetraapp.model.RegistrationModel;
import com.trinetraapp.model.StateData;
import com.trinetraapp.model.StateModel;
import com.trinetraapp.model.login_model.LoginModel;
import com.trinetraapp.model.ques_data.TestQuesData;
import com.trinetraapp.model.ques_data.TestQuesModel;
import com.trinetraapp.model.test_name.TestNameData;
import com.trinetraapp.model.test_name.TestNameModel;
import com.trinetraapp.utils.Api_Call;
import com.trinetraapp.utils.Connectivity;
import com.trinetraapp.utils.RxApiClient;
import com.trinetraapp.utils.SessionManager;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
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

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<TestNameData> testNameData = new ArrayList<>();
    List<TestQuesData> testQuesData = new ArrayList<>();
    List<String> TestName = new ArrayList<>();
    List<String> QuesName = new ArrayList<>();
    String TestId, user_id;
    private File img_file;
    private boolean isPermitted;
    private static final int CAMERA_REQUEST = 1888;
    File finalFile;
    SessionManager sessionManager;
    private MultipartBody.Part body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sessionManager = new SessionManager(this);
        user_id = sessionManager.getUser_Id();

        SetGraph();
        if (Connectivity.isConnected(this)) {
            getTest();
        } else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }
        //post image show choose camera gallery
        binding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission must be granted
                checkRunTimePermission();
            }
        });


        binding.spinTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (testNameData != null && !testNameData.isEmpty()) {
                    TestId = testNameData.get(i).getTestId();
                    if (testQuesData != null && !testQuesData.isEmpty()) {
                        testQuesData.clear();
                        QuesName.clear();
                    }

                    getQuestions(TestId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //**************************************************
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
                                intent = new Intent(MainActivity.this, Profile_Activity.class);
                                startActivity(intent);
                                return true;

                            case R.id.navigation_pay_history:
                                intent = new Intent(MainActivity.this, Payment_History_Activity.class);
                                startActivity(intent);
                                return true;

                            case R.id.test_history:
                                intent = new Intent(MainActivity.this, Test_History_Activity.class);
                                startActivity(intent);
                                return true;

                            case R.id.logout:
                                sessionManager.logout();

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

        //*****************************************
        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalFile != null) {
                    SubmitQuiz(binding.etDesc.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Please Capture Answer sheet Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("CheckResult")
    private void SubmitQuiz(String et_desc) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);
//        Map<String, String> map = new HashMap<>();
//        map.put("description", et_desc);
//        map.put("user_id", user_id);
//        map.put("test_id", TestId);
        RequestBody Desc = RequestBody.create(MediaType.parse("text/plain"), et_desc);
        RequestBody User_id = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody test_id = RequestBody.create(MediaType.parse("text/plain"), TestId);

        if (finalFile != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), finalFile);
            body = MultipartBody.Part.createFormData("image", finalFile.getName(), fileBody);
        }

        apiInterface.QuizSubmitApi(Desc, User_id, test_id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RegistrationModel>() {
                    @Override
                    public void onNext(RegistrationModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());

                            if (response.getResponse()) {
                                Toast.makeText(MainActivity.this, "Submit successful", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(MainActivity.this, "" + response.getError(), Toast.LENGTH_SHORT).show();

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

    private void checkRunTimePermission() {

        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            // selectImage();
            OpenCamera();

        }
    }

    private void OpenCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
                    binding.ivCamera.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    img_file = bitmapToFile(MainActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + img_file);
                    String filename = img_file.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(MainActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(MainActivity.this);
    }

    @SuppressLint("CheckResult")
    private void getQuestions(String testId) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.TestQuesApi(testId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TestQuesModel>() {
                    @Override
                    public void onNext(TestQuesModel response) {
                        //Handle logic
                        try {
                            testQuesData.clear();
                            QuesName.clear();
                            progressDialog.dismiss();
                            // Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResponse()) {
                                testQuesData = response.getData();
                                for (int i = 0; i < response.getData().size(); i++) {
                                    QuesName.add(response.getData().get(i).getQuestion());
                                }

                                ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, QuesName);
                                binding.list.setAdapter(adp);

                            } else {
                                testQuesData.clear();
                                QuesName.clear();
                                Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_SHORT).show();
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
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, TestName);
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

    private void SetGraph() {

        try {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
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
                            Toast.makeText(MainActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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
                //   selectImage();
                OpenCamera();

            } else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            binding.ivCamera.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));
            // System.out.println(mImageCaptureUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

}
