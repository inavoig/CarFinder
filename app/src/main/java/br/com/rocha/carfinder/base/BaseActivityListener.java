package br.com.rocha.carfinder.base;

public interface BaseActivityListener {

    void checkPermissionAndRun(Runnable action, String... permissions);
}
