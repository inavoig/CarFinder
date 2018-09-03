package br.com.rocha.carfinder.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import br.com.rocha.carfinder.R;

public class AnimUtils {

    public static void replaceView(View view1, View view2) {
        if (view1.getVisibility() != View.VISIBLE && view2.getVisibility() == View.VISIBLE) {
            return;
        }
        hideViewWithAnim(view1);
        showViewWithAnim(view2);
    }

    public static void hideViewWithAnim(View view) {
        if (view.getVisibility() == View.INVISIBLE) {
            return;
        }

        view.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
        view.startAnimation(animation);
    }

    public static void showViewWithAnim(final View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }

        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        view.startAnimation(animation);
    }
}
