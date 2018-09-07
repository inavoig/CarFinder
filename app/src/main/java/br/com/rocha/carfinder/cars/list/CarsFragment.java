package br.com.rocha.carfinder.cars.list;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Objects;

import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.base.BaseFragment;
import br.com.rocha.carfinder.cars.map.CarsMapActivity;
import br.com.rocha.carfinder.data.Car;
import br.com.rocha.carfinder.util.AnimUtils;

public class CarsFragment extends BaseFragment implements CarsContract.View {

    private RecyclerView mRvCars;
    private FloatingActionButton mFabMap;
    private ProgressBar mLoading;
    private Button mReload;

    private CarsContract.Presenter mCarsPresenter;
    private CarsAdapter mCarsAdapter;

    public CarsFragment() {
    }

    public static CarsFragment newInstance() {
        return new CarsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCarsAdapter = new CarsAdapter(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        mRvCars.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCars.setAdapter(mCarsAdapter);

        mFabMap.setOnClickListener(view -> mCarsPresenter.showMap());
        mReload.setOnClickListener(view -> mBaseActivityListener.checkPermissionAndRun(() ->
                mCarsPresenter.reload(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    @Override
    public void onResume() {
        super.onResume();
        mCarsPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);

        mRvCars = view.findViewById(R.id.rv_cars);
        mFabMap = view.findViewById(R.id.fab_map);
        mLoading = view.findViewById(R.id.loading);
        mReload = view.findViewById(R.id.bt_reload);

        return view;
    }

    @Override
    public void setPresenter(CarsContract.Presenter presenter) {
        mCarsPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            AnimUtils.replaceView(mRvCars, mLoading);
        } else {
            AnimUtils.replaceView(mLoading, mRvCars);
        }
    }

    @Override
    public void showCars(List<Car> cars) {
        mCarsAdapter.setCars(cars);
    }

    @Override
    public void showLoadingCarsError() {
        Snackbar.make(Objects.requireNonNull(getView()), R.string.error_loading_cars,
                Snackbar.LENGTH_LONG).show();
        AnimUtils.showViewWithAnim(mReload);

    }

    @Override
    public void showNoCarsFound() {
        Snackbar.make(Objects.requireNonNull(getView()), R.string.no_cars_found,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openMap() {
        startActivity(new Intent(getContext(), CarsMapActivity.class));
    }

    @Override
    public void hideReloadButton() {
        AnimUtils.hideViewWithAnim(mReload);
    }
}
