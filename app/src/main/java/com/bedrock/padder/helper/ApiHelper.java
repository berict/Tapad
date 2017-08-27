package com.bedrock.padder.helper;

import com.bedrock.padder.api.ApiClient;
import com.bedrock.padder.api.ApiInterface;
import com.bedrock.padder.model.preset.PresetSchema;

import java.util.List;

import retrofit2.Call;

public class ApiHelper {

    private String TAG = "Retrofit";

    private ApiInterface apiService = null;

    public ApiInterface getApiService() {
        return ApiClient.getClient().create(ApiInterface.class);
    }

    // codes should be in the working code because its asynchronous task, the value cannot be retrieved immediately

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
}
