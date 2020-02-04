package com.inved.realestatemanager.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.inved.realestatemanager.retrofit.exchangePojo.Exchange;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.sharedpreferences.ManageCurrency;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitServiceApiExchange {

    private static Retrofit retrofit = null;

    public interface ExchangeApi {

        @GET("latest")
        Call<Exchange> getExchangeValue(
                @Query("symbols") String currency);
    }

   public void retrofitCall(){
       if(retrofit == null){
           OkHttpClient client = new OkHttpClient.Builder()
                   .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                   .build();


           retrofit = new Retrofit.Builder()
                   .baseUrl("https://api.exchangeratesapi.io/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .client(client)
                   .build();
       }

       ExchangeApi service = retrofit.create(ExchangeApi.class);

       Call<Exchange> call = service.getExchangeValue("USD");

       call.enqueue(new Callback<Exchange>() {
           @Override
           public void onResponse(@NonNull Call<Exchange> call, @NonNull Response<Exchange> response) {

               if(response.body()!=null) {
                   double d = response.body().getRates().getUSD();
                   float result = (float)d ;
                   ManageCurrency.saveRate(MainApplication.getInstance().getApplicationContext(),result);
               }
           }

           @Override
           public void onFailure(@NonNull Call<Exchange> call, @NonNull Throwable t) {
               Log.d("debago", "inRetrofit failure :" + t);
           }
       });


   }





}
