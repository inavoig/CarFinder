package br.com.rocha.carfinder.cars.list;

import java.util.List;

import br.com.rocha.carfinder.base.BasePresenter;
import br.com.rocha.carfinder.base.BaseView;
import br.com.rocha.carfinder.data.Car;

public interface CarsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showCars(List<Car> cars);

        void showLoadingCarsError();

        void showNoCarsFound();

        void openMap();

        void hideReloadButton();
    }

    interface Presenter extends BasePresenter {

        void loadCars();

        void showMap();

        void reload();
    }
}
