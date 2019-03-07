package com.android.baselibrary.baseutil;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: WangHao
 * Created On: 2019/3/7 0007 13:14
 * Description:
 */
public class ThreadUtils {

    public static void runOnUiThread(@NonNull final Runnable target) {
        runOnThread(target, AndroidSchedulers.mainThread());
    }

    public static void runOnThread(@NonNull final Runnable target, @NonNull Scheduler scheduler) {
        checkNotNull(target);
        checkNotNull(scheduler);
        Observable
            .create(new ObservableOnSubscribe<Void>() {
                @Override
                public void subscribe(ObservableEmitter<Void> e) throws Exception {
                    target.run();
                    e.onComplete();
                }
            })
            .subscribeOn(scheduler)
            .subscribe();
    }
}
