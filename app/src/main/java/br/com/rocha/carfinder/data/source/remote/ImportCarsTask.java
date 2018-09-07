package br.com.rocha.carfinder.data.source.remote;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;


public class ImportCarsTask extends AsyncTask<Void, Void, Void> {

    private static final String FILE_LOCATIONS_JSON = "locations.json";

    private CarsDataSource.LoadCarsCallback mLoadCarsCallback;

    ImportCarsTask(CarsDataSource.LoadCarsCallback callback) {
        mLoadCarsCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        try {
            StorageReference locationsFileRef = storageRef.child(FILE_LOCATIONS_JSON);
            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), FILE_LOCATIONS_JSON);
            locationsFileRef.getFile(destinationFile)
                    .addOnSuccessListener(taskSnapshot ->
                            mLoadCarsCallback.onCarsLoaded(retrieveDownloadedData(destinationFile.getAbsolutePath())))
                    .addOnFailureListener(e -> mLoadCarsCallback.onDataNotAvailable());

        } catch (Exception e) {
            Log.d("Error creating JSON: ", e.getMessage());
            mLoadCarsCallback.onDataNotAvailable();
        }
        return null;
    }

    private List<Car> retrieveDownloadedData(String path) {
        try (FileInputStream content = new FileInputStream(path);
             Reader reader = new InputStreamReader(content)) {

            return new Gson().fromJson(reader, PlaceMarks.class).getCars();

        } catch (IOException e) {
            Log.e("ERROR_FILE", "Error reading file: ", e);
            return null;
        }
    }

    public class PlaceMarks implements Serializable {

        @SerializedName("placemarks")
        private List<Car> mCars;

        public List<Car> getCars() {
            return mCars;
        }

        public void setCars(List<Car> cars) {
            mCars = cars;
        }
    }
}