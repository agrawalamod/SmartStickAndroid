/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSubscribeAndLog() throws InterruptedException {
        onView(withId(R.id.informationTextView)).check(matches(isDisplayed()));

        // Click subscribe button and check toast
        onView(allOf(withId(R.id.subscribeButton), withText(R.string.subscribe_to_news)))
                .check(matches(isDisplayed()))
                .perform(click());
        confirmToastStartsWith(mActivityRule.getActivity().getString(R.string.msg_subscribed));

        // Sleep so the Toast goes away, this is lazy but it works (Toast.LENGTH_SHORT = 2000)
        Thread.sleep(2000);

        // Click log token and check toast
        onView(allOf(withId(R.id.logTokenButton), withText(R.string.log_token)))
                .check(matches(isDisplayed()))
                .perform(click());
        confirmToastStartsWith(mActivityRule.getActivity().getString(R.string.msg_token_fmt, ""));
    }

    private void confirmToastStartsWith(String string) {
        View activityWindowDecorView = mActivityRule.getActivity().getWindow().getDecorView();
        onView(withText(startsWith(string)))
                .inRoot(withDecorView(not(is(activityWindowDecorView))))
                .check(matches(isDisplayed()));
    }

}
