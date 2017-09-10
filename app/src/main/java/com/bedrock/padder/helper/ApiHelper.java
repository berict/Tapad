package com.bedrock.padder.helper;

import com.bedrock.padder.api.ApiClient;
import com.bedrock.padder.api.ApiInterface;
import com.bedrock.padder.api.SchemaPresets;
import com.bedrock.padder.api.SchemaVersion;
import com.bedrock.padder.model.Schema;
import com.bedrock.padder.model.preset.PresetSchema;
import com.bedrock.padder.model.preset.Version;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.bedrock.padder.api.ApiClient.BASE_URL;

public class ApiHelper {

    private String TAG = "Retrofit";

    private ApiInterface apiService = null;

    public ApiInterface getApiService() {
        return ApiClient.getClient().create(ApiInterface.class);
    }

    // codes should be in the working code because its asynchronous task, the values cannot be retrieved immediately

    public Call<PresetSchema> getPresetSchema(String tag) {
        if (apiService == null) {
            apiService = getApiService();
        }
        Call<PresetSchema> call = apiService.getPreset(tag);
        return call;
    }

    public Call<List<PresetSchema>> getPresetSchemas() {
        if (apiService == null) {
            apiService = getApiService();
        }
        Call<List<PresetSchema>> call = apiService.getPresets();
        return call;
    }

    public Call<Schema> getSchema() {
        if (apiService == null) {
            apiService = getApiService();
        }
        Call<Schema> call = apiService.getSchema();
        return call;
    }

    public Call<Integer> getVersion() {
        if (apiService == null) {
            apiService = getApiService();
        }
        Call<Integer> call = apiService.getVersion();
        return call;
    }

    public Observable<Schema> getObservableSchema() {
        Retrofit schema = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Observable<List<PresetSchema>> presetObservable = schema
                .create(SchemaPresets.class)
                .getObservablePresets()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Version> versionObservable = schema
                .create(SchemaVersion.class)
                .getObservableVersion()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Schema> schemaObservable = Observable.zip(presetObservable, versionObservable, new Func2<List<PresetSchema>, Version, Schema>() {
            @Override
            public Schema call(List<PresetSchema> presetSchemas, Version version) {
                return new Schema(presetSchemas.toArray(new PresetSchema[presetSchemas.size()]), version.getVersionCode());
            }
        });

        return schemaObservable;
    }
}
