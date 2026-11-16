/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.material.tabs;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.view.View;
import androidx.test.core.app.ApplicationProvider;
import com.google.android.material.test.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public final class TabLayoutTest {

  private final Context context = ApplicationProvider.getApplicationContext();

  @Before
  public void themeApplicationContext() {
    context.setTheme(R.style.Theme_MaterialComponents_Light);
  }

  @Test
  public void recycledTabView_hasNoBadge() {
    TabLayout tabLayout = new TabLayout(context);
    TabLayout.Tab tab1 = tabLayout.newTab().setText("Tab 1");
    tabLayout.addTab(tab1);
    TabLayout.Tab tab2 = tabLayout.newTab().setText("Tab 2");
    tabLayout.addTab(tab2);

    tab2.getOrCreateBadge().setNumber(123);

    TabLayout.TabView tab2View = tab2.view;
    tabLayout.removeTab(tab2);

    TabLayout.Tab tab3 = tabLayout.newTab().setText("Tab 3");
    tabLayout.addTab(tab3);

    assertThat(tab3.view).isSameInstanceAs(tab2View);
    assertThat(tab3.getBadge()).isNull();
  }

  @Test
  public void recycledTabView_hasNoId() {
    TabLayout tabLayout = new TabLayout(context);
    TabLayout.Tab tab1 = tabLayout.newTab().setText("Tab 1");
    tabLayout.addTab(tab1);
    TabLayout.Tab tab2 = tabLayout.newTab().setText("Tab 2");
    tabLayout.addTab(tab2);

    tab2.setId(123);

    TabLayout.TabView tab2View = tab2.view;
    tabLayout.removeTab(tab2);

    TabLayout.Tab tab3 = tabLayout.newTab().setText("Tab 3");
    tabLayout.addTab(tab3);

    assertThat(tab3.view).isSameInstanceAs(tab2View);
    assertThat(tab3.view.getId()).isEqualTo(View.NO_ID);
  }
}
