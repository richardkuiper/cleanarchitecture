package com.richardkuiper.cleanarchitecture;

import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxImmediateSchedulerRule implements TestRule {
    private Scheduler immediate = Schedulers.trampoline();

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
                RxJavaPlugins.setInitIoSchedulerHandler(schedulerCallable -> immediate);
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> immediate);
                RxJavaPlugins.setInitComputationSchedulerHandler(schedulerCallable -> immediate);
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> immediate);
                RxJavaPlugins.setInitNewThreadSchedulerHandler(schedulerCallable -> immediate);
                RxJavaPlugins.setSingleSchedulerHandler(scheduler -> immediate);
                RxJavaPlugins.setInitSingleSchedulerHandler(schedulerCallable -> immediate);
                RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> immediate);
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> immediate);

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
