/*
 * Copyright 2018 Roger Bagué
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
package net.rbague.screenshotcallback;

import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ScreenshotObserver allows the app to know when the user has taken a screenshot of the app.
 * <p>
 * <p>Each time a screenshot is taken and saved to [Pictures/Screenshots, DCIM/Screenshots, sdcard/ScreenCapture],
 * {@link ScreenshotObserver#onScreenshotTaken(String)} will be called with the path of the screenshot</p>
 * <p>
 * <p>In order for the callback to work start/stopListening must be called</p>
 *
 * @see ScreenshotObserver#startListnening()
 * @see ScreenshotObserver#stopListening()
 */
public abstract class ScreenshotObserver {

    /**
     * List to keep track of all the observers so the user can start/stop watching,
     * and they are not being garbage collected.
     *
     * @see FileObserver#startWatching()
     * @see FileObserver#stopWatching()
     */
    private List<FileObserver> mObservers;

    /**
     * Constructor.
     * Sets up the {@link FileObserver} instances.
     */
    public ScreenshotObserver() {
        final Handler handler = new Handler();

        File[] screenshotDirs = new File[]{
                new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "Screenshots"
                ),
                new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "Screenshots"
                ),
                new File(Environment.getExternalStorageDirectory(), "ScreenCapture")
        };

        mObservers = new ArrayList<>(screenshotDirs.length);
        for (final File dir : screenshotDirs) {
            if (dir.exists() && dir.isDirectory()) {
                mObservers.add(new FileObserver(dir.getPath(), FileObserver.CREATE) {

                    private String mLastObservedPath = "";

                    @Override
                    public void onEvent(int event, final @Nullable String path) {
                        if (!TextUtils.isEmpty(path) && event == CREATE) {
                            if (!mLastObservedPath.equalsIgnoreCase(path)) {
                                final File screenshot = new File(dir.getPath(), path);
                                if (screenshot.exists() && !screenshot.isDirectory() && screenshot.isFile()) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            onScreenshotTaken(screenshot.getPath());
                                        }
                                    });
                                    mLastObservedPath = path;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Must be called in order for the observer to work.
     * Otherwise {@link #onScreenshotTaken(String)} won't receive any calls.
     */
    public void startListnening() {
        if (mObservers != null) {
            for (FileObserver observer : mObservers) {
                observer.startWatching();
            }
        }
    }

    /**
     * Called to stop receiving calls to {@link #onScreenshotTaken(String)}.
     */
    public void stopListening() {
        if (mObservers != null) {
            for (FileObserver observer : mObservers) {
                observer.stopWatching();
            }
        }
    }

    /**
     * Called when a screenshot has been taken.
     *
     * @param path The absolute path of the screenshot
     */
    public abstract void onScreenshotTaken(String path);
}
