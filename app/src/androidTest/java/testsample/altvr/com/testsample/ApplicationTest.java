package testsample.altvr.com.testsample;

import android.app.Application;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.test.ApplicationTestCase;

import org.junit.Rule;
import org.junit.Test;

import testsample.altvr.com.testsample.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application>
{
    public ApplicationTest() {
        super(Application.class);
    }
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void validateRecyclerView()
    {
        onView(withId(R.id.photosListRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, scrollTo()));

    }
}