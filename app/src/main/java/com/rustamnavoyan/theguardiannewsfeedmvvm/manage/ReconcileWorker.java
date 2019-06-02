package com.rustamnavoyan.theguardiannewsfeedmvvm.manage;

import android.content.Context;

import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.ArticleRepository;
import com.rustamnavoyan.theguardiannewsfeedmvvm.util.DateTimeUtil;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReconcileWorker extends Worker {
    public ReconcileWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        new ArticleRepository().downloadArticleList(1, 1, articleItems -> {
            PeriodicDownloadManager.schedule();
            PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(getApplicationContext());
            Date savedDate = preferenceHelper.getDate();
            Date date = DateTimeUtil.parseDate(articleItems.get(0).getPublishedDate());

            if (date.getTime() > savedDate.getTime()) {
                NotificationManager.showNewsFeedNotification(getApplicationContext());
                preferenceHelper.saveDate(date);
            }
        });
        return Result.success();
    }
}
