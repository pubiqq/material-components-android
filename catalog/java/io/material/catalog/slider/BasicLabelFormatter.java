/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.slider;

import androidx.annotation.NonNull;

import com.google.android.material.slider.LabelFormatter;

import java.util.Locale;

class BasicLabelFormatter implements LabelFormatter {

  private static final long TRILLION = 1000000000000L;
  private static final int BILLION = 1000000000;
  private static final int MILLION = 1000000;
  private static final int THOUSAND = 1000;

  @NonNull
  @Override
  public String getFormattedValue(float value) {
    if (value >= TRILLION) {
      return String.format(Locale.ENGLISH, "%.1fT", value / TRILLION);
    } else if (value >= BILLION) {
      return String.format(Locale.ENGLISH, "%.1fB", value / BILLION);
    } else if (value >= MILLION) {
      return String.format(Locale.ENGLISH, "%.1fM", value / MILLION);
    } else if (value >= THOUSAND) {
      return String.format(Locale.ENGLISH, "%.1fK", value / THOUSAND);
    }

    return String.format(Locale.ENGLISH, "%.0f", value);
  }
}
