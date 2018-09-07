package br.com.rocha.carfinder.cars.map;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.rocha.carfinder.Injection;
import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.base.BaseActivity;
import br.com.rocha.carfinder.util.ActivityUtils;

public class CarsMapActivity extends BaseActivity {

    private CarsMapPresenter mCarsMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        CarsMapFragment carsMapFragment =
                (CarsMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (carsMapFragment == null) {
            carsMapFragment = CarsMapFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), carsMapFragment, R.id.map);
        }

        mCarsMapPresenter = new CarsMapPresenter(
                Injection.provideCarsRepository(getApplicationContext()), carsMapFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
