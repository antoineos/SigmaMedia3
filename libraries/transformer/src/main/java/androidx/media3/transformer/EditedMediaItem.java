/*
 * Copyright 2023 The Android Open Source Project
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
package androidx.media3.transformer;

import static androidx.media3.common.util.Assertions.checkState;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** A {@link MediaItem} with the transformations to apply to it. */
@UnstableApi
public class EditedMediaItem {

  /** A builder for {@link EditedMediaItem} instances. */
  public static final class Builder {

    private final MediaItem mediaItem;

    private boolean removeAudio;
    private boolean removeVideo;
    private @MonotonicNonNull Effects effects;

    /**
     * Creates an instance.
     *
     * @param mediaItem The {@link MediaItem} on which transformations are applied.
     */
    public Builder(MediaItem mediaItem) {
      this.mediaItem = mediaItem;
    }

    /**
     * Sets whether to remove the audio from the {@link MediaItem}.
     *
     * <p>The default value is {@code false}.
     *
     * <p>The audio and video cannot both be removed because the output would not contain any
     * samples.
     *
     * @param removeAudio Whether to remove the audio.
     * @return This builder.
     */
    @CanIgnoreReturnValue
    public Builder setRemoveAudio(boolean removeAudio) {
      this.removeAudio = removeAudio;
      return this;
    }

    /**
     * Sets whether to remove the video from the {@link MediaItem}.
     *
     * <p>The default value is {@code false}.
     *
     * <p>The audio and video cannot both be removed because the output would not contain any
     * samples.
     *
     * @param removeVideo Whether to remove the video.
     * @return This builder.
     */
    @CanIgnoreReturnValue
    public Builder setRemoveVideo(boolean removeVideo) {
      this.removeVideo = removeVideo;
      return this;
    }

    /**
     * Sets the {@link Effects} to apply to the {@link MediaItem}.
     *
     * <p>The default value is an empty {@link Effects} instance.
     *
     * @param effects The {@link Effects} to apply.
     * @return This builder.
     */
    @CanIgnoreReturnValue
    public Builder setEffects(Effects effects) {
      this.effects = effects;
      return this;
    }

    /** Builds an {@link EditedMediaItem} instance. */
    public EditedMediaItem build() {
      if (effects == null) {
        effects =
            new Effects(
                /* audioProcessors= */ ImmutableList.of(), /* videoEffects= */ ImmutableList.of());
      }
      return new EditedMediaItem(mediaItem, removeAudio, removeVideo, effects);
    }
  }

  /* package */ final MediaItem mediaItem;
  /* package */ final boolean removeAudio;
  /* package */ final boolean removeVideo;
  /* package */ final Effects effects;

  private EditedMediaItem(
      MediaItem mediaItem, boolean removeAudio, boolean removeVideo, Effects effects) {
    checkState(!removeAudio || !removeVideo, "Audio and video cannot both be removed");
    this.mediaItem = mediaItem;
    this.removeAudio = removeAudio;
    this.removeVideo = removeVideo;
    this.effects = effects;
  }
}
