package br.com.rocha.carfinder.data.source;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CoordinatesConverter {

    @TypeConverter
    public String fromCoordinatesArray(double[] coordinates) {
        if (coordinates == null) {
            return null;
        }
        Type type = new TypeToken<double[]>() {
        }.getType();

        return new Gson().toJson(coordinates, type);
    }

    @TypeConverter
    public double[] toCoordinatesArray(String coordinates) {
        if (coordinates == null) {
            return (null);
        }

        Type type = new TypeToken<double[]>() {
        }.getType();

        return new Gson().fromJson(coordinates, type);
    }
}