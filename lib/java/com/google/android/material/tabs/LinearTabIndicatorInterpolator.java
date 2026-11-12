/*
 * Copyright (C) 2026 The Android Open Source Project
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

import static com.google.android.material.animation.AnimationUtils.lerp;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

/**
 * An implementation of {@link TabIndicatorInterpolator} that moves the selected tab indicator
 * linearly between destinations.
 */
class LinearTabIndicatorInterpolator extends TabIndicatorInterpolator {

  @Override
  void updateIndicatorForOffset(
      TabLayout tabLayout,
      View startTitle,
      View endTitle,
      @FloatRange(from = 0.0, to = 1.0) float offset,
      @NonNull Drawable indicator) {
    RectF startIndicator = calculateIndicatorWidthForTab(tabLayout, startTitle);
    // Linearly interpolate the indicator's position, using it's left and right bounds, between the
    // two destinations.
    RectF endIndicator = calculateIndicatorWidthForTab(tabLayout, endTitle);
    indicator.setBounds(
        lerp((int) startIndicator.left, (int) endIndicator.left, offset),
        indicator.getBounds().top,
        lerp((int) startIndicator.right, (int) endIndicator.right, offset),
        indicator.getBounds().bottom);
  }
}
