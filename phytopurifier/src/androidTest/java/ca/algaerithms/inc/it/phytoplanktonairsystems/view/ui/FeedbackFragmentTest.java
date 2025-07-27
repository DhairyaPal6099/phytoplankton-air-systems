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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RatingBar;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void feedbackFragmentTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_activity),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("brucershall@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_Password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_activity),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Malaika1"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_button), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_activity),
                                        0),
                                5),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_feedback),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.etPhone), withText("5551234567"),
                        childAtPosition(
                                allOf(withId(R.id.feedback_layout),
                                        childAtPosition(
                                                withId(R.id.nav_feedback),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.etComment),
                        childAtPosition(
                                allOf(withId(R.id.feedback_layout),
                                        childAtPosition(
                                                withId(R.id.nav_feedback),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("Good application, I like it!"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnSubmit), withText("Submit Feedback"),
                        childAtPosition(
                                allOf(withId(R.id.btnSubmitContainer),
                                        childAtPosition(
                                                withId(R.id.feedback_layout),
                                                5)),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        //Check if a textView appeared showing a countdown timer

        //Check if the R.id.btnSubmit is grayed out (disabled)
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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

        onView(withId(R.id.etPhone)).perform(typeText("4378182727"));
        onView(withId(R.id.etComment)).perform(typeText("Mediocre application"));
        onView(withId(R.id.ratingBar)).perform(setRating(4.0f));
        onView(withId(R.id.btnSubmit)).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
    }

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
}
