/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.android.material.internal;

import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Constructor;

/**
 * Class to create StaticLayout.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * StaticLayout staticLayout =
 *   StaticLayoutBuilderCompat.obtain("Lorem Ipsum", new TextPaint(), 100)
 *     .setAlignment(Alignment.ALIGN_NORMAL)
 *     .build();
 * }</pre>
 *
 * @hide
 */
@RestrictTo(Scope.LIBRARY_GROUP)
public final class StaticLayoutBuilderCompat {

  static final int DEFAULT_HYPHENATION_FREQUENCY = StaticLayout.HYPHENATION_FREQUENCY_NORMAL;

  // Default line spacing values to match android.text.Layout constants.
  static final float DEFAULT_LINE_SPACING_ADD = 0.0f;
  static final float DEFAULT_LINE_SPACING_MULTIPLIER = 1.0f;

  private static boolean initialized;

  @Nullable private static Constructor<StaticLayout> constructor;
  @Nullable private static Object textDirection;

  private CharSequence source;
  private final TextPaint paint;
  private final int width;
  private int start;
  private int end;

  private Alignment alignment;
  private int maxLines;
  private float lineSpacingAdd;
  private float lineSpacingMultiplier;
  private int hyphenationFrequency;
  private boolean includePad;
  private boolean isRtl;
  @Nullable private TextUtils.TruncateAt ellipsize;
  @Nullable private StaticLayoutBuilderConfigurer staticLayoutBuilderConfigurer;

  private StaticLayoutBuilderCompat(CharSequence source, TextPaint paint, int width) {
    this.source = source;
    this.paint = paint;
    this.width = width;
    this.start = 0;
    this.end = source.length();
    this.alignment = Alignment.ALIGN_NORMAL;
    this.maxLines = Integer.MAX_VALUE;
    this.lineSpacingAdd = DEFAULT_LINE_SPACING_ADD;
    this.lineSpacingMultiplier = DEFAULT_LINE_SPACING_MULTIPLIER;
    this.hyphenationFrequency = DEFAULT_HYPHENATION_FREQUENCY;
    this.includePad = true;
    this.ellipsize = null;
  }

  /**
   * Obtain a builder for constructing StaticLayout objects.
   *
   * @param source The text to be laid out, optionally with spans
   * @param paint The base paint used for layout
   * @param width The width in pixels
   * @return a builder object used for constructing the StaticLayout
   */
  @NonNull
  public static StaticLayoutBuilderCompat obtain(
      @NonNull CharSequence source, @NonNull TextPaint paint, @IntRange(from = 0) int width) {
    return new StaticLayoutBuilderCompat(source, paint, width);
  }

  /**
   * Set the alignment. The default is {@link Layout.Alignment#ALIGN_NORMAL}.
   *
   * @param alignment Alignment for the resulting {@link StaticLayout}
   * @return this builder, useful for chaining
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setAlignment(@NonNull Alignment alignment) {
    this.alignment = alignment;
    return this;
  }

  /**
   * Set whether to include extra space beyond font ascent and descent (which is needed to avoid
   * clipping in some languages, such as Arabic and Kannada). The default is {@code true}.
   *
   * @param includePad whether to include padding
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setIncludeFontPadding
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setIncludePad(boolean includePad) {
    this.includePad = includePad;
    return this;
  }

  /**
   * Set the index of the start of the text
   *
   * @return this builder, useful for chaining
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setStart(@IntRange(from = 0) int start) {
    this.start = start;
    return this;
  }

  /**
   * Set the index + 1 of the end of the text
   *
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setIncludeFontPadding
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setEnd(@IntRange(from = 0) int end) {
    this.end = end;
    return this;
  }

  /**
   * Set maximum number of lines. This is particularly useful in the case of ellipsizing, where it
   * changes the layout of the last line. The default is unlimited.
   *
   * @param maxLines maximum number of lines in the layout
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setMaxLines
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setMaxLines(@IntRange(from = 0) int maxLines) {
    this.maxLines = maxLines;
    return this;
  }

  /**
   * Set the line spacing addition and multiplier frequency.
   *
   * @param spacingAdd Line spacing addition for the resulting {@link StaticLayout}
   * @param lineSpacingMultiplier Line spacing multiplier for the resulting {@link StaticLayout}
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setLineSpacing(float, float)
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setLineSpacing(float spacingAdd, float lineSpacingMultiplier) {
    this.lineSpacingAdd = spacingAdd;
    this.lineSpacingMultiplier = lineSpacingMultiplier;
    return this;
  }

  /**
   * Set the hyphenation frequency.
   *
   * @param hyphenationFrequency Hyphenation frequency for the resulting {@link StaticLayout}
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setHyphenationFrequency(int)
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setHyphenationFrequency(int hyphenationFrequency) {
    this.hyphenationFrequency = hyphenationFrequency;
    return this;
  }

  /**
   * Set ellipsizing on the layout. Causes words that are longer than the view is wide, or exceeding
   * the number of lines (see #setMaxLines).
   *
   * @param ellipsize type of ellipsis behavior
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setEllipsize
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setEllipsize(@Nullable TextUtils.TruncateAt ellipsize) {
    this.ellipsize = ellipsize;
    return this;
  }

  /**
   * Set the {@link StaticLayoutBuilderConfigurer} which allows additional custom configurations on
   * the static layout.
   */
  @NonNull
  @CanIgnoreReturnValue
  public StaticLayoutBuilderCompat setStaticLayoutBuilderConfigurer(
      @Nullable StaticLayoutBuilderConfigurer staticLayoutBuilderConfigurer) {
    this.staticLayoutBuilderConfigurer = staticLayoutBuilderConfigurer;
    return this;
  }

  /**
   * A method that builds the {@link StaticLayout} after options have been set.
   */
  @NonNull
  public StaticLayout build() {
    if (source == null) {
      source = "";
    }

    int availableWidth = Math.max(0, width);
    CharSequence textToDraw = source;
    if (maxLines == 1) {
      textToDraw = TextUtils.ellipsize(source, paint, availableWidth, ellipsize);
    }

    end = Math.min(textToDraw.length(), end);
    if (isRtl && maxLines == 1) {
      alignment = Alignment.ALIGN_OPPOSITE;
    }
    StaticLayout.Builder builder =
        StaticLayout.Builder.obtain(
            textToDraw, start, end, paint, availableWidth);
    builder.setAlignment(alignment);
    builder.setIncludePad(includePad);
    TextDirectionHeuristic textDirectionHeuristic = isRtl
        ? TextDirectionHeuristics.RTL
        : TextDirectionHeuristics.LTR;
    builder.setTextDirection(textDirectionHeuristic);
    if (ellipsize != null) {
      builder.setEllipsize(ellipsize);
    }
    builder.setMaxLines(maxLines);
    if (lineSpacingAdd != DEFAULT_LINE_SPACING_ADD
        || lineSpacingMultiplier != DEFAULT_LINE_SPACING_MULTIPLIER) {
      builder.setLineSpacing(lineSpacingAdd, lineSpacingMultiplier);
    }
    if (maxLines > 1) {
      builder.setHyphenationFrequency(hyphenationFrequency);
    }
    if (staticLayoutBuilderConfigurer != null) {
      staticLayoutBuilderConfigurer.configure(builder);
    }
    return builder.build();
  }

  @NonNull
  public StaticLayoutBuilderCompat setIsRtl(boolean isRtl) {
    this.isRtl = isRtl;
    return this;
  }

  /**
   * Class representing a StaticLayoutBuilder exception from initializing a StaticLayout.
   *
   * @deprecated This exception is no longer thrown by {@link #build()}.
   * @hide
   */
  @Deprecated
  @RestrictTo(Scope.LIBRARY_GROUP)
  public static class StaticLayoutBuilderCompatException extends Exception {

    StaticLayoutBuilderCompatException(Throwable cause) {
      super("Error thrown initializing StaticLayout " + cause.getMessage(), cause);
    }
  }
}
