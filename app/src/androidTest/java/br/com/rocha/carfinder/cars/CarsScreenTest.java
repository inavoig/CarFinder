package br.com.rocha.carfinder.cars;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.cars.list.CarsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CarsScreenTest {

    @Rule
    public ActivityTestRule<CarsActivity> mCarsActivityTestRule =
            new ActivityTestRule<>(CarsActivity.class);

    @Test
    public void clickMapButton_opensCarsMapUi() {
        onView(withId(R.id.fab_map)).perform(click());

        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}
