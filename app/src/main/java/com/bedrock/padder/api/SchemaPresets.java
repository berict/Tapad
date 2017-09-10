package com.bedrock.padder.api;

import com.bedrock.padder.model.preset.PresetSchema;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface SchemaPresets {
    @GET("tapad/presets")
    Observable<List<PresetSchema>> getObservablePresets();
}
