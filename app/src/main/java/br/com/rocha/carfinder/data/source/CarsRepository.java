package br.com.rocha.carfinder.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.rocha.carfinder.data.Car;

import static com.google.common.base.Preconditions.checkNotNull;

public class CarsRepository implements CarsDataSource {

    private static CarsRepository INSTANCE = null;

    private final CarsDataSource mCarsRemoteDataSource;
    private final CarsDataSource mCarsLocalDataSource;

    private Map<String, Car> mCachedCars;

    private CarsRepository(@NonNull CarsDataSource carsRemoteDataSource,
                           @NonNull CarsDataSource carsLocalDataSource) {
        mCarsRemoteDataSource = checkNotNull(carsRemoteDataSource);
        mCarsLocalDataSource = checkNotNull(carsLocalDataSource);
    }

    public static CarsRepository getInstance(CarsDataSource carsRemoteDataSource,
                                             CarsDataSource carsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CarsRepository(carsRemoteDataSource, carsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCars(@NonNull LoadCarsCallback callback) {
        checkNotNull(callback);

        if (mCachedCars != null) {
            callback.onCarsLoaded(new ArrayList<>(mCachedCars.values()));
            return;
        }

        mCarsLocalDataSource.getCars(new LoadCarsCallback() {
            @Override
            public void onCarsLoaded(List<Car> cars) {
                refreshCache(cars);
                callback.onCarsLoaded(cars);
            }

            @Override
            public void onDataNotAvailable() {
                getTasksFromRemoteDataSource(callback);
            }
        });
    }

    @Override
    public void saveCar(@NonNull Car car) {
        mCarsLocalDataSource.saveCar(car);
    }

    private void getTasksFromRemoteDataSource(@NonNull LoadCarsCallback callback) {
        mCarsRemoteDataSource.getCars(new LoadCarsCallback() {
            @Override
            public void onCarsLoaded(List<Car> cars) {
                for (Car car : cars) {
                    saveCar(car);
                }
                refreshCache(cars);
                callback.onCarsLoaded(cars);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Car> cars) {
        if (mCachedCars == null) {
            mCachedCars = new LinkedHashMap<>();
        }

        mCachedCars.clear();
        for (Car car : cars) {
            mCachedCars.put(car.getVin(), car);
        }
    }
}
