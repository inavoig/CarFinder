package br.com.rocha.carfinder.cars.map;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;
import br.com.rocha.carfinder.data.source.CarsRepository;
import br.com.rocha.carfinder.util.EspressoIdlingResource;

import static com.google.common.base.Preconditions.checkNotNull;

public class CarsMapPresenter implements CarsMapContract.Presenter {

    private final CarsRepository mCarsRepository;
    private final CarsMapContract.View mCarsMapView;

    private List<Marker> mCarMarkers;
    private boolean mShowMarkers;

    public CarsMapPresenter(@NonNull CarsRepository carsRepository, @NonNull CarsMapContract.View carsMapView) {
        mCarsRepository = checkNotNull(carsRepository, "carsRepository cannot be null");
        mCarsMapView = checkNotNull(carsMapView, "carsMapView cannot be null!");

        mCarsMapView.setPresenter(this);
    }

    @Override
    public void loadCarsOnMap() {
        mCarsMapView.setLoadingIndicator(true);

        EspressoIdlingResource.increment();

        mCarsRepository.getCars(new CarsDataSource.LoadCarsCallback() {
            @Override
            public void onCarsLoaded(List<Car> cars) {
                setLoadingFinished();

                if (cars.isEmpty()) {
                    mCarsMapView.showNoCarsFound();

                } else {
                    mCarMarkers = new ArrayList<>();

                    for (Car car : cars) {
                        MarkerOptions marker = new MarkerOptions()
                                .title(car.getName())
                                .position(new LatLng(car.getCoordinates()[1], car.getCoordinates()[0]));
                        mCarsMapView.addMarkerToMap(marker);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
                setLoadingFinished();
                mCarsMapView.showLoadingCarsError();
            }

            private void setLoadingFinished() {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mCarsMapView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void addCarMarker(Marker carMarker) {
        mCarMarkers.add(carMarker);
    }

    @Override
    public void updateMapMarkers(Marker clickedMarker) {
        for (Marker carMarker : mCarMarkers) {
            if (!clickedMarker.equals(carMarker)) {
                carMarker.setVisible(mShowMarkers);
            } else {
                if (!mShowMarkers) {
                    carMarker.showInfoWindow();
                } else {
                    carMarker.hideInfoWindow();
                }
            }
        }

        mShowMarkers = !mShowMarkers;
    }

    @Override
    public void start() {
        mCarsMapView.loadMapOnScreen();
    }
}
