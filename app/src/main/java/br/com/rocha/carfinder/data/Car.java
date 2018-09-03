package br.com.rocha.carfinder.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.rocha.carfinder.data.source.CoordinatesConverter;

@Entity(tableName = "car")
public final class Car implements Serializable {

    private static final String COLUMN_VIN = "vin";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ENGINE_TYPE = "engineType";
    private static final String COLUMN_FUEL = "fuel";
    private static final String COLUMN_EXTERIOR = "exterior";
    private static final String COLUMN_INTERIOR = "interior";
    private static final String COLUMN_COORDINATES = "coordinates";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = COLUMN_VIN)
    @SerializedName(COLUMN_VIN)
    private String mVin;

    @ColumnInfo(name = COLUMN_NAME)
    @SerializedName(COLUMN_NAME)
    private String mName;

    @ColumnInfo(name = COLUMN_ADDRESS)
    @SerializedName(COLUMN_ADDRESS)
    private String mAddress;

    @ColumnInfo(name = COLUMN_ENGINE_TYPE)
    @SerializedName(COLUMN_ENGINE_TYPE)
    private String mEngineType;

    @ColumnInfo(name = COLUMN_FUEL)
    @SerializedName(COLUMN_FUEL)
    private int mFuel;

    @ColumnInfo(name = COLUMN_EXTERIOR)
    @SerializedName(COLUMN_EXTERIOR)
    private String mExterior;

    @ColumnInfo(name = COLUMN_INTERIOR)
    @SerializedName(COLUMN_INTERIOR)
    private String mInterior;

    @ColumnInfo(name = COLUMN_COORDINATES)
    @SerializedName(COLUMN_COORDINATES)
    @TypeConverters(CoordinatesConverter.class)
    private double[] mCoordinates;

    @NonNull
    public String getVin() {
        return mVin;
    }

    public void setVin(@NonNull String vin) {
        mVin = vin;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getEngineType() {
        return mEngineType;
    }

    public void setEngineType(String engineType) {
        mEngineType = engineType;
    }

    public int getFuel() {
        return mFuel;
    }

    public void setFuel(int fuel) {
        mFuel = fuel;
    }

    public String getExterior() {
        return mExterior;
    }

    public void setExterior(String exterior) {
        mExterior = exterior;
    }

    public String getInterior() {
        return mInterior;
    }

    public void setInterior(String interior) {
        mInterior = interior;
    }

    public double[] getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(double[] coordinates) {
        mCoordinates = coordinates;
    }
}
