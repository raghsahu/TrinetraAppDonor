package com.trinetraapp.utils;


import com.trinetraapp.model.RegistrationModel;
import com.trinetraapp.model.StateModel;
import com.trinetraapp.model.login_model.LoginModel;
import com.trinetraapp.model.ques_data.TestQuesModel;
import com.trinetraapp.model.test_name.TestNameModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static com.trinetraapp.utils.Base_Url.get_city;
import static com.trinetraapp.utils.Base_Url.get_questions;
import static com.trinetraapp.utils.Base_Url.get_state;
import static com.trinetraapp.utils.Base_Url.get_tests;
import static com.trinetraapp.utils.Base_Url.user_login;
import static com.trinetraapp.utils.Base_Url.user_signup;
import static com.trinetraapp.utils.Base_Url.user_test_submit;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Api_Call {

    @GET(get_state)
    Observable<StateModel> StateApi();

    @FormUrlEncoded
    @POST(get_city)
    Observable<StateModel> CityApi(
            @Field("city_state") String city_state);


    @FormUrlEncoded
    @POST(user_signup)
    Observable<LoginModel>  RegisterApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(user_login)
    Observable<LoginModel>  LoginApi(@FieldMap  Map<String, String> map);

    @GET(get_tests)
    Observable<TestNameModel>TestNameApi();

    @FormUrlEncoded
    @POST(get_questions)
    Observable<TestQuesModel>  TestQuesApi(
            @Field("test_id") String testId);


    @Multipart
    @POST(user_test_submit)
    Observable<RegistrationModel>  QuizSubmitApi(
            @Part ("description") RequestBody desc,
            @Part ("user_id")  RequestBody user_id,
            @Part ("test_id") RequestBody test_id,
            @Part MultipartBody.Part body);
}
