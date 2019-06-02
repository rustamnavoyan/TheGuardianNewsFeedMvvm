package com.rustamnavoyan.theguardiannewsfeedmvvm.manage;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class PeriodicDownloadManager {
    private static final String UNIQUE_WORK_NAME = "periodic_download_work";
    private static final long REPEAT_INTERVAL = 30;

    public static void schedule() {
        OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(ReconcileWorker.class)
                        .setInitialDelay(REPEAT_INTERVAL, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance().enqueueUniqueWork(UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP, workRequest);
    }

    public static void cancel() {
        WorkManager.getInstance().cancelUniqueWork(UNIQUE_WORK_NAME);
    }
}