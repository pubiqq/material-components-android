/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.topappbar;

import io.material.catalog.R;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * An Activity that displays a scrolling Top App Bar demo for the Catalog app, with a transparent
 * status bar.
 */
public class TopAppBarScrollingTransparentStatusDemoActivity
    extends BaseTopAppBarActionBarDemoActivity {

  private final Handler handler = new Handler(Looper.getMainLooper());

  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(
            R.layout.cat_topappbar_scrolling_transparent_statusbar_activity, viewGroup, false);

    Toolbar toolbar = view.findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ExtendedFloatingActionButton button = view.findViewById(R.id.fling);
    button.setOnClickListener(v -> {
      handler.postDelayed(() -> fling(
          view.getWidth() / 2,
          view.getHeight() / 2,
          view.getWidth() / 2,
          view.getHeight() / 2 - 100,
          500
      ), 0L);

      handler.postDelayed(() -> fling(
          view.getWidth() / 2,
          view.getHeight() / 2,
          view.getWidth() / 2,
          view.getHeight() / 2 + 100,
          500
      ), 1000L);
    });

    return view;
  }


  private void fling(int fromX, int fromY,
                     int toX, int toY,
                     int stepCount) {

    long downTime = SystemClock.uptimeMillis();
    long eventTime = SystemClock.uptimeMillis();

    // Pointer down

    MotionEvent event = MotionEvent.obtain(
        downTime,
        eventTime,
        MotionEvent.ACTION_DOWN,
        fromX,
        fromY,
        0
    );

    dispatchTouchEvent(event);

    // Pointer move

    float xStep = (toX - fromX) * 1.0f / stepCount;
    float yStep = (toY - fromY) * 1.0f / stepCount;

    for (int i = 0; i < stepCount; i++) {
      float x = fromX + xStep * i;
      float y = fromY + yStep * i;

      eventTime = SystemClock.uptimeMillis();
      event = MotionEvent.obtain(
          downTime,
          eventTime,
          MotionEvent.ACTION_MOVE,
          x,
          y,
          0
      );

      dispatchTouchEvent(event);
    }

    // Pointer up

    eventTime = SystemClock.uptimeMillis();
    event = MotionEvent.obtain(
        downTime,
        eventTime,
        MotionEvent.ACTION_UP,
        toX,
        toY,
        0
    );

    dispatchTouchEvent(event);
  }
}
