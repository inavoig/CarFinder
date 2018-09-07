package br.com.rocha.carfinder.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

public class BaseFragment extends Fragment {

    protected BaseActivityListener mBaseActivityListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mBaseActivityListener = (BaseActivityListener) context;

        } catch (Exception e) {
            Log.e("LISTENER_ERROR", "BaseActivityListener must be implement", e);
        }
    }
}
