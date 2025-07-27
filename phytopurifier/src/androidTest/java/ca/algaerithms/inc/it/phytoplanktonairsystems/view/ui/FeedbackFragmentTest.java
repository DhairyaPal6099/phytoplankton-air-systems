package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.RatingBar;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FeedbackFragmentTest {

    @Rule
    public ActivityScenarioRule<SplashScreenActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashScreenActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.POST_NOTIFICATIONS");

    @Before
    public void resetFeedbackSubmitTimer() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Task<Void> task = firestore.collection("users").document("oZTwImIjG0adKZPwJk6GCcbA19I3").update(Map.of("feedback_disabled_time", -1));

        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmailIdPrefilled() {
        loginToApp();
        openFeedbackFragment();

        onView(withId(R.id.etEmail)).check(matches(isDisplayed())).check(matches(withText("brucershall@gmail.com")));
    }

    @Test
    public void testNamePrefilled() {
        loginToApp();
        openFeedbackFragment();

        onView(withId(R.id.etName)).check(matches(isDisplayed())).check(matches(withText("Bruce Marshall")));
    }

    @Test
    public void testProgressBarAppearedOnSubmit() throws InterruptedException {
        loginToApp();
        openFeedbackFragment();
        submitFeedback();

        Thread.sleep(1000);
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
    }

    @Test
    public void testAlertDialogBoxAppearsAfterSubmitting() throws InterruptedException {
        loginToApp();
        openFeedbackFragment();
        submitFeedback();

        Thread.sleep(5000);
        onView(withText("OK")).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmitButtonGrayedOutAfterSubmitting() throws InterruptedException {
        loginToApp();
        openFeedbackFragment();
        submitFeedback();

        Thread.sleep(5000);
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnSubmit)).check(matches(not(isEnabled())));
    }

    //Check if a textView appeared showing a countdown timer

    private void loginToApp() {
        onView(withId(R.id.login_username)).perform(replaceText("brucershall@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_Password)).perform(replaceText("Malaika1"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
    }

    private void openFeedbackFragment() {
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withId(R.id.nav_feedback)).perform(click());
    }

    public static ViewAction setRating(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((RatingBar) view).setRating(rating);
            }
        };
    }

    private void submitFeedback() {
        onView(withId(R.id.etPhone)).perform(typeText("4378182727"));
        onView(withId(R.id.etComment)).perform(typeText("Mediocre application"));
        onView(withId(R.id.ratingBar)).perform(setRating(4.0f));
        onView(withId(R.id.btnSubmit)).perform(click());
    }
}
