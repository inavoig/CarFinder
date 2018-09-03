package br.com.rocha.carfinder.cars.map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.rocha.carfinder.data.source.CarsRepository;

import static org.mockito.Mockito.verify;

public class CarsMapPresenterTest {

    @Mock
    private CarsRepository mCarsRepository;

    @Mock
    private CarsMapContract.View mCarsMapView;

    private CarsMapPresenter mCarsMapPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);

        mCarsMapPresenter = new CarsMapPresenter(mCarsRepository, mCarsMapView);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mCarsMapPresenter = new CarsMapPresenter(mCarsRepository, mCarsMapView);

        verify(mCarsMapView).setPresenter(mCarsMapPresenter);
    }
}
