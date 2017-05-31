package com.bedrock.padder.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bedrock.padder.model.FirebaseMetadata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FirebaseService {

    private String TAG = "FirebaseService";
    private static final int REQUEST_WRITE_STORAGE = 112;

    public static String PROJECT_LOCATION = Environment.getExternalStorageDirectory().getPath() + "/Tapad";
    public static String PROJECT_LOCATION_PRESETS = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets";
    public static String PROJECT_LOCATION_PRESET_METADATA = Environment.getExternalStorageDirectory().getPath() + "/Tapad/presets/metadata.txt";

    public void saveFromFirebase(StorageReference storageReference, final String fileLocation, Activity activity) {
        // permission check
        boolean hasPermission = ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            // no permission
            Log.e(TAG, "No permission acquired");
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
        } else {
            if (isConnected(activity)) {
                File file = new File(fileLocation);

                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Successful download at " + fileLocation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to download");
                    }
                });
            } else {
                Log.e(TAG, "Not connected to the internet");
            }
        }
    }

    public FirebaseMetadata saveFromFirebaseGetFirebaseMetadata(StorageReference storageReference, final String fileLocation, Activity activity) {
        // permission check
        boolean hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            // no permission
            Log.e(TAG, "No permission acquired");
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
        } else {
            if (isConnected(activity)) {
                File file = new File(fileLocation);

                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Successful download at " + fileLocation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to download");
                    }
                });
            } else {
                Log.e(TAG, "Not connected to the internet");
            }
        }
        return getFirebaseMetadata(activity);
    }
    
    public void downloadFirebaseMetadata(Activity activity) {
        FirebaseApp.initializeApp(activity);

        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets")
                        .child("metadata.txt");
        this.saveFromFirebase(metadataReference, PROJECT_LOCATION_PRESET_METADATA, activity);
    }
    
    public FirebaseMetadata getFirebaseMetadata(Activity activity) {
        FirebaseApp.initializeApp(activity);
        String metadataLocation = PROJECT_LOCATION_PRESET_METADATA;
        StorageReference metadataReference =
                FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tapad-4d342.appspot.com/presets")
                        .child("metadata.txt");
        if (new File(metadataLocation).exists()) {
            // metadata file exists
            String metadata = getStringFromFile(metadataLocation);
            if (getStorageMetadata(metadataReference, activity).getUpdatedTimeMillis() 
                    > new File(metadataLocation).lastModified()) {
                // updated, download new one
                return saveFromFirebaseGetFirebaseMetadata(
                        metadataReference,
                        metadataLocation,
                        activity
                );
            } else {
                Log.d(TAG, "Attached adapter");
                // offline or not updated, continue
                Gson gson = new Gson();
                FirebaseMetadata firebaseMetadata = gson.fromJson(metadata, FirebaseMetadata.class);
                if (firebaseMetadata.getPresets() == null ||
                        firebaseMetadata.getVersionCode() == null) {
                    // corrupted metadata, download again
                    return saveFromFirebaseGetFirebaseMetadata(
                            metadataReference,
                            metadataLocation,
                            activity
                    );
                } else {
                    return firebaseMetadata;
                }
            }
        } else {
            return saveFromFirebaseGetFirebaseMetadata(
                    metadataReference,
                    metadataLocation,
                    activity
            );
        }
    }

    public String getStringFromFile(String fileLocation) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileLocation));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public StorageMetadata getStorageMetadata(StorageReference storageReference, Activity activity) {
        FirebaseApp.initializeApp(activity);
        final StorageMetadata[] mStorageMetadata = {null};
        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "Successful getting metadata");
                mStorageMetadata[0] = storageMetadata;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get metadata");
            }
        });
        return mStorageMetadata[0];
    }

    private boolean isConnected(Context context) {
        // returns whether the device is connected to the internet
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
