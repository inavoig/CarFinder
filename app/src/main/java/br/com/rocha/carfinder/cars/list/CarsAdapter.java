package br.com.rocha.carfinder.cars.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.cars.CarStatus;
import br.com.rocha.carfinder.data.Car;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {

    class CarsViewHolder extends RecyclerView.ViewHolder {

        private View mViewStatusInterior;
        private View mViewStatusExterior;
        private TextView mTvEngine;
        private TextView mTvFuel;
        private TextInputLayout mTilName;
        private TextInputLayout mTilVin;

        private CarsViewHolder(View itemView) {
            super(itemView);

            mViewStatusInterior = itemView.findViewById(R.id.view_status_top);
            mViewStatusExterior = itemView.findViewById(R.id.view_status_bottom);
            mTvEngine = itemView.findViewById(R.id.tv_engine);
            mTvFuel = itemView.findViewById(R.id.tv_fuel);
            mTilName = itemView.findViewById(R.id.til_name);
            mTilVin = itemView.findViewById(R.id.til_vin);
        }
    }

    private LayoutInflater mInflater;
    private List<Car> mCars;

    CarsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCars = new ArrayList<>();
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarsViewHolder(mInflater.inflate(R.layout.adapter_car_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder carHolder, int position) {
        Car car = mCars.get(position);

        if (Objects.equals(car.getInterior(), CarStatus.GOOD.name())) {
            setCarStatusBarBackground(carHolder.mViewStatusInterior, R.drawable.top_status_bar_green);
        } else {
            setCarStatusBarBackground(carHolder.mViewStatusInterior, R.drawable.top_status_bar_red);
        }

        if (Objects.equals(car.getExterior(), CarStatus.GOOD.name())) {
            setCarStatusBarBackground(carHolder.mViewStatusExterior, R.drawable.bottom_status_bar_green);
        } else {
            setCarStatusBarBackground(carHolder.mViewStatusExterior, R.drawable.bottom_status_bar_red);
        }

        carHolder.mTvEngine.setText(car.getEngineType());
        carHolder.mTvFuel.setText(String.valueOf(car.getFuel()));
        Objects.requireNonNull(carHolder.mTilName.getEditText()).setText(car.getName());
        Objects.requireNonNull(carHolder.mTilVin.getEditText()).setText(car.getVin());
    }

    private void setCarStatusBarBackground(@NonNull View statusView, int backgroundId) {
        statusView.setBackground(ContextCompat.getDrawable(mInflater.getContext(), backgroundId));
    }

    void setCars(List<Car> cars) {
        mCars.clear();
        mCars.addAll(cars);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }
}