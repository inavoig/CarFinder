package br.com.rocha.carfinder;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.rocha.carfinder.data.source.CarsRepository;
import br.com.rocha.carfinder.data.source.local.CarFinderDatabase;
import br.com.rocha.carfinder.data.source.local.CarsLocalDataSource;
import br.com.rocha.carfinder.data.source.remote.CarsRemoteDataSource;
import br.com.rocha.carfinder.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static CarsRepository provideCarsRepository(@NonNull Context context) {
        checkNotNull(context);
        CarFinderDatabase database = CarFinderDatabase.getInstance(context);
        return CarsRepository.getInstance(CarsRemoteDataSource.getInstance(),
                CarsLocalDataSource.getInstance(new AppExecutors(), database.carDao()));
    }
}
