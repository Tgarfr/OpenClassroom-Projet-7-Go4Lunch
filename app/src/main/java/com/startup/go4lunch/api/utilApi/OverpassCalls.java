package com.startup.go4lunch.api.utilApi;

import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OverpassCalls {

    public interface Callback {
        void onResponse(@Nullable OverpassGsonObject overpassGsonObject);
        void onFailure();
    }

    public static void fetchRestaurantList(Callback callbacks) {
        final WeakReference<Callback> callbackWeakReference = new WeakReference<>(callbacks);

        Retrofit retrofit = OverpassRetrofitService.retrofit;
        OverpassRetrofitService overpassRetrofitService = retrofit.create(OverpassRetrofitService.class);

        Call<OverpassGsonObject> call = overpassRetrofitService.getRestaurantList();

        call.enqueue(new retrofit2.Callback<OverpassGsonObject>() {
            @Override
            public void onResponse(Call<OverpassGsonObject> call, Response<OverpassGsonObject> response) {
                if (callbackWeakReference.get() != null) {
                    callbackWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<OverpassGsonObject> call, Throwable t) {
                if (callbackWeakReference.get() != null) {
                    callbackWeakReference.get().onFailure();
                }
                Log.e("Overpass",t.toString());
            }
        });
    }
}
