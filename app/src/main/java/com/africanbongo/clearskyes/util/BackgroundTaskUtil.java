package com.africanbongo.clearskyes.util;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *     Static class that allows background work to be done,
 *     then consume the result using {@link Callback} when the task has been finished.
 * </p>
 * <p>
 *     All background tasks are run using the {@link java.util.concurrent.Executor} framework.
 * </p>
 * <p>
 *     UI tasks are run using the {@link android.os.Handler} class.
 * </p>
 *<p>
 *     Inspired by <a href="https://stackoverflow.com/a/58767934/13725690">EpicPandaForce</a>
 *</p>
 */
public final class BackgroundTaskUtil {

    /**
     * {@link ExecutorService} using CachedThreadPool in order to maximise on performance.
     */
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private BackgroundTaskUtil() {}
    /**
     * An interface used to assimilate results of background processes
     * @param <R>
     */
    public interface Callback<R> {
        void onComplete(R result);
    }

    /**
     * Runs a task on a background thread and the result is consumed in the {@link Callback} argument
     * @param backgroundTask {@link Callable} used to run background task and return the result
     * @param uiTask {@link Callback} that consumes the result of the background task,
     *                     it is run on the application's main {@link Looper}.
     * @param <R> The {@link Object} type passed as a consumable object
     */
    public static <R> void runAndUpdateUI(Callable<R> backgroundTask, Callback<R> uiTask) {
        executorService.execute(() -> {
            try {
                final R result = backgroundTask.call();
                new Handler(Looper.getMainLooper()).post(() ->
                        uiTask.onComplete(result));
            } catch (Exception e) {
                Log.e(BackgroundTaskUtil.class.getSimpleName(), "Error carrying out background task", e);
            }
        });
    }

    /**
     * <p>
     *     Executes a runnable task within an {@link ExecutorService} using a cached thread pool.
     *     Do not run task that includes UI changes otherwise app crashes.
     * </p>
     *
     * <p>
     *     If the need arises to execute UI Tasks asynchronously use {@link #runAndUpdateUI(Callable, Callback)}
     * </p>
     * @param task
     */
    public static void runTask(Runnable task) {
        executorService.submit(task);
    }
}
