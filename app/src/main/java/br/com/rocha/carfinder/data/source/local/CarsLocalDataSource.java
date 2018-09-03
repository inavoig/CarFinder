package br.com.rocha.carfinder.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;
import br.com.rocha.carfinder.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class CarsLocalDataSource implements CarsDataSource {

    private static volatile CarsLocalDataSource INSTANCE;

    private CarDao mCarDao;
    private AppExecutors mAppExecutors;

    private CarsLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull CarDao carDao) {
        mAppExecutors = appExecutors;
        mCarDao = carDao;
    }

    public static CarsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull CarDao carDao) {
        if (INSTANCE == null) {
            synchronized (CarsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CarsLocalDataSource(appExecutors, carDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getCars(@NonNull LoadCarsCallback callback) {
        Runnable runnable = () -> {
            final List<Car> cars = mCarDao.getCars();
            mAppExecutors.mainThread().execute(() -> {
                if (cars.isEmpty()) {
                    callback.onDataNotAvailable();

                } else {
                    callback.onCarsLoaded(cars);
                }
            });
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveCar(@NonNull final Car car) {
        checkNotNull(car);
        Runnable saveRunnable = () -> mCarDao.insert(car);
        mAppExecutors.diskIO().execute(saveRunnable);
    }
}
