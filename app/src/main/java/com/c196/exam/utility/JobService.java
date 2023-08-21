package com.c196.exam.utility;

import android.app.job.JobParameters;

public class JobService extends android.app.job.JobService {
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {

        new Thread(() -> {
            if(jobCancelled) {
                return;
            }
            jobFinished(params, true);
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true;
    }
}
