package br.com.rocha.carfinder.cars.list;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;
import br.com.rocha.carfinder.data.source.CarsRepository;
import br.com.rocha.carfinder.util.EspressoIdlingResource;

import static com.google.common.base.Preconditions.checkNotNull;

public class CarsPresenter implements CarsContract.Presenter {

    private final CarsRepository mCarsRepository;
    private final CarsContract.View mCarsView;

    public CarsPresenter(@NonNull CarsRepository carsRepository, @NonNull CarsContract.View carsView) {
        mCarsRepository = checkNotNull(carsRepository, "carsRepository cannot be null");
        mCarsView = checkNotNull(carsView, "carsView cannot be null!");

        mCarsView.setPresenter(this);
    }

    @Override
    public void loadCars() {
        mCarsView.setLoadingIndicator(true);

        EspressoIdlingResource.increment();

        mCarsRepository.getCars(new CarsDataSource.LoadCarsCallback() {
            @Override
            public void onCarsLoaded(List<Car> cars) {
                setLoadingFinished();

                if (cars.isEmpty()) {
                    mCarsView.showNoCarsFound();

                } else {
                    mCarsView.showCars(cars);
                }
            }

            @Override
            public void onDataNotAvailable() {
                setLoadingFinished();
                mCarsView.showLoadingCarsError();
            }

            private void setLoadingFinished() {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mCarsView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void showMap() {
        mCarsView.openMap();
    }

    @Override
    public void reload() {
        mCarsView.hideReloadButton();

        loadCars();
    }

    @Override
    public void start() {
        loadCars();
    }
}
