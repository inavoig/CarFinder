package br.com.rocha.carfinder.cars.list;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.rocha.carfinder.Injection;
import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.base.BaseActivity;
import br.com.rocha.carfinder.util.ActivityUtils;

public class CarsActivity extends BaseActivity {

    private CarsPresenter mCarsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CarsFragment carsFragment =
                (CarsFragment) getSupportFragmentManager().findFragmentById(R.id.cars);
        if (carsFragment == null) {
            carsFragment = CarsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), carsFragment, R.id.cars);
        }

        mCarsPresenter = new CarsPresenter(
                Injection.provideCarsRepository(getApplicationContext()), carsFragment);
    }
}
