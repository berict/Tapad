package com.bedrock.padder.api;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface SchemaPreset {
    @GET("tapad/presets/{tag}")
    Observable<JsonObject> getObservablePreset(@Path("tag") String tag);
}
