package com.bedrock.padder.api;

import com.bedrock.padder.model.preset.Version;

import retrofit2.http.GET;
import rx.Observable;

public interface SchemaVersion {
    @GET("tapad/version")
    Observable<Version> getObservableVersion();
}
