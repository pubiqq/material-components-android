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

package com.google.android.material.shape;

import static com.google.common.truth.Truth.assertThat;

import android.graphics.RectF;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.internal.DoNotInstrument;

/** Tests for {@link AdjustedCornerSize}. */
@RunWith(RobolectricTestRunner.class)
@DoNotInstrument
public class AdjustedCornerSizeTest {

  private static final RectF BOUNDS = new RectF(0f, 0f, 100f, 100f);

  @Test
  public void getCornerSize_returnsOtherSizePlusAdjustment() {
    AdjustedCornerSize cornerSize =
        new AdjustedCornerSize(-2f, new AbsoluteCornerSize(10f));

    assertThat(cornerSize.getCornerSize(BOUNDS)).isEqualTo(8f);
  }

  @Test
  public void getCornerSize_clampsNegativeResultToZero() {
    AdjustedCornerSize cornerSize =
        new AdjustedCornerSize(-12f, new AbsoluteCornerSize(10f));

    assertThat(cornerSize.getCornerSize(BOUNDS)).isEqualTo(0f);
  }

  @Test
  public void getCornerSize_withNestedAdjustedCornerSize_returnsAccumulatedSize() {
    AdjustedCornerSize inner = new AdjustedCornerSize(2f, new AbsoluteCornerSize(10f));
    AdjustedCornerSize outer = new AdjustedCornerSize(4f, inner);

    assertThat(outer.getCornerSize(BOUNDS)).isEqualTo(16f);
  }

  @Test
  public void getCornerSize_withTwoNestedAdjustedCornerSizes_returnsAccumulatedSize() {
    AdjustedCornerSize inner2 = new AdjustedCornerSize(2f, new AbsoluteCornerSize(10f));
    AdjustedCornerSize inner = new AdjustedCornerSize(4f, inner2);
    AdjustedCornerSize outer = new AdjustedCornerSize(8f, inner);

    assertThat(outer.getCornerSize(BOUNDS)).isEqualTo(24f);
  }
}
