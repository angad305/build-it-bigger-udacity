package com.udacity.gradle.builditbigger;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.AllOf.allOf;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.angadcheema.androidjokes.AndroidJoke;
import com.angadcheema.javajoke.JavaJoke;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//Test is made Test Async response on Paid Debug App

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

  @Rule
  public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Before
  public void init() {
    activityActivityTestRule.getActivity().getFragmentManager().beginTransaction();
  }

  @Test
  public void TestAutoComplete() {
    onView(withId(R.id.button)).perform(click());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    onView(withId(R.id.textView))
        .check(matches(withText("How do you rejuvenate an old boat?\n" + "\n" + "Boat-tox.")));
  }

}

