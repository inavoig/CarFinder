package br.com.rocha.carfinder.cars.map;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.rocha.carfinder.base.BasePresenter;
import br.com.rocha.carfinder.base.BaseView;

public interface CarsMapContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void loadMapOnScreen();

        void showLoadingCarsError();

        void showNoCarsFound();

        void showGPSAlert();

        void addMarkerToMap(MarkerOptions carMarker);
    }

    interface Presenter extends BasePresenter {

        void loadCarsOnMap();

        void addCarMarker(Marker carMarker);

        void updateMapMarkers(Marker clickedMarker);
    }
}
