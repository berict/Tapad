package com.bedrock.padder.api;

import com.bedrock.padder.model.preset.PresetSchema;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("tapad/presets")
    Call<List<PresetSchema>> getPresets();

    @GET("tapad/presets/{tag}")
    Call<PresetSchema> getPreset(@Path("tag") String tag);
}
