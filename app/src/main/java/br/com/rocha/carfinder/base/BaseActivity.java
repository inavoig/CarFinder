package br.com.rocha.carfinder.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.util.EspressoIdlingResource;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseActivityListener {

    private int mCountRequestPermission;
    private SparseArray<Runnable> mRequestPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestPermission = new SparseArray<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            mRequestPermission.remove(requestCode);
            return;
        }

        for (Integer grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                showPermissionDeniedMsg();

                mRequestPermission.remove(requestCode);
                return;
            }
        }

        Runnable runnable = mRequestPermission.get(requestCode);
        if (runnable != null) {
            runnable.run();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void checkPermissionAndRun(Runnable action, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            action.run();
            return;
        }

        List<String> needPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission);
            }
        }

        if (needPermissions.size() == 0) {
            action.run();

        } else {
            for (String needPermission : needPermissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, needPermission)) {
                    showPermissionDeniedMsg();
                    return;
                }
            }
            mCountRequestPermission++;
            mRequestPermission.put(mCountRequestPermission, action);
            ActivityCompat.requestPermissions(this,
                    needPermissions.toArray(new String[needPermissions.size()]), mCountRequestPermission);
        }
    }

    private void showPermissionDeniedMsg() {
        Snackbar.make(this.findViewById(android.R.id.content), R.string.permission_denied,
                Snackbar.LENGTH_LONG).show();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
