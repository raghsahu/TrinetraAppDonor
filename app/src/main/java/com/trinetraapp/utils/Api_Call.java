package com.trinetraapp.utils;


import com.trinetraapp.model.RegistrationModel;
import com.trinetraapp.model.StateModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.trinetraapp.utils.Base_Url.get_city;
import static com.trinetraapp.utils.Base_Url.get_state;
import static com.trinetraapp.utils.Base_Url.user_signup;

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
    Observable<RegistrationModel>  RegisterApi(@FieldMap Map<String, String> map);
}
