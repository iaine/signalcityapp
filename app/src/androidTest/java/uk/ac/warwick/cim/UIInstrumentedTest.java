package uk.ac.warwick.cim;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import uk.ac.warwick.cim.signalApp.MainActivity;
import uk.ac.warwick.cim.signalApp.ModelState;
import uk.ac.warwick.cim.signalApp.R;

@RunWith(AndroidJUnit4.class)
public class UIInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void changeState_sameActivity() {
        ModelState modelState = new ModelState();
        onView(withId(R.id.button)).perform(click());
        assertEquals(true, modelState.inLoop);
    }

    @Test
    public void changeStateBack_sameActivity() {
        ModelState modelState = new ModelState();
        onView(withId(R.id.button)).perform(click());
        assertEquals(true, modelState.inLoop);
        onView(withId(R.id.button)).perform(click());
        assertEquals(false, modelState.inLoop);
    }
}
