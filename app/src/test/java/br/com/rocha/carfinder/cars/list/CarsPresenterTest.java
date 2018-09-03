package br.com.rocha.carfinder.cars.list;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.data.source.CarsDataSource;
import br.com.rocha.carfinder.data.source.CarsRepository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class CarsPresenterTest {

    private static List<Car> CARS;

    @Mock
    private CarsRepository mCarsRepository;

    @Mock
    private CarsContract.View mCarsView;

    @Captor
    private ArgumentCaptor<CarsDataSource.LoadCarsCallback> mLoadCarsCallbackCaptor;

    private CarsPresenter mCarsPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);

        mCarsPresenter = new CarsPresenter(mCarsRepository, mCarsView);

        CARS = Lists.newArrayList(new Car(), new Car());
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mCarsPresenter = new CarsPresenter(mCarsRepository, mCarsView);

        verify(mCarsView).setPresenter(mCarsPresenter);
    }

    @Test
    public void loadCarsFromRepositoryIntoView_withData() {
        mCarsPresenter.loadCars();

        verify(mCarsRepository).getCars(mLoadCarsCallbackCaptor.capture());
        mLoadCarsCallbackCaptor.getValue().onCarsLoaded(CARS);

        InOrder inOrder = inOrder(mCarsView);
        inOrder.verify(mCarsView).setLoadingIndicator(true);
        inOrder.verify(mCarsView).setLoadingIndicator(false);
        ArgumentCaptor<List> showCarsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mCarsView).showCars(showCarsArgumentCaptor.capture());
        assertTrue(showCarsArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void loadCarsFromRepositoryIntoView_errorReturn() {
        mCarsPresenter.loadCars();

        verify(mCarsRepository).getCars(mLoadCarsCallbackCaptor.capture());
        mLoadCarsCallbackCaptor.getValue().onDataNotAvailable();

        InOrder inOrder = inOrder(mCarsView);
        inOrder.verify(mCarsView).setLoadingIndicator(true);
        inOrder.verify(mCarsView).setLoadingIndicator(false);
        verify(mCarsView).showLoadingCarsError();
    }

    @Test
    public void loadCarsFromRepositoryIntoView_emptyReturn() {
        mCarsPresenter.loadCars();

        verify(mCarsRepository).getCars(mLoadCarsCallbackCaptor.capture());
        mLoadCarsCallbackCaptor.getValue().onCarsLoaded(Collections.emptyList());

        InOrder inOrder = inOrder(mCarsView);
        inOrder.verify(mCarsView).setLoadingIndicator(true);
        inOrder.verify(mCarsView).setLoadingIndicator(false);
        verify(mCarsView).showNoCarsFound();
    }

    @Test
    public void clickOnFab_showsMapUi() {
        mCarsPresenter.showMap();

        verify(mCarsView).openMap();
    }

    @Test
    public void reloadCarsFromRepositoryIntoView() {
        mCarsPresenter.reload();

        verify(mCarsRepository).getCars(mLoadCarsCallbackCaptor.capture());
        mLoadCarsCallbackCaptor.getValue().onCarsLoaded(CARS);

        InOrder inOrder = inOrder(mCarsView);
        inOrder.verify(mCarsView).hideReloadButton();
        inOrder.verify(mCarsView).setLoadingIndicator(true);
        inOrder.verify(mCarsView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mCarsView).showCars(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void startUI_loadAllCars() {
        mCarsPresenter.start();

        verify(mCarsRepository).getCars(mLoadCarsCallbackCaptor.capture());
        mLoadCarsCallbackCaptor.getValue().onCarsLoaded(CARS);

        InOrder inOrder = inOrder(mCarsView);
        inOrder.verify(mCarsView).setLoadingIndicator(true);
        inOrder.verify(mCarsView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mCarsView).showCars(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 2);
    }
}
