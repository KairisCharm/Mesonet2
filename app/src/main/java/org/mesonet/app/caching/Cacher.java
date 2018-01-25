package org.mesonet.app.caching;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import org.mesonet.app.dependencyinjection.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;


@PerActivity
public class Cacher
{
    private Realm mRealm;
    private HandlerThread mRealmThread = new HandlerThread("Realm");



    @Inject
    public Cacher(final Activity inContext)
    {
        mRealmThread.start();
        new Handler(mRealmThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                synchronized (Cacher.this) {
                    Realm.init(inContext);
                    RealmConfiguration configuration = new RealmConfiguration.Builder().schemaVersion(26)
                            .modules(new MesonetRealmModule())
                            .name(inContext.getPackageName())
                            .build();
                    mRealm = Realm.getInstance(configuration);

                    return false;
                }
            }
        }).sendEmptyMessage(0);
    }



    public void SaveToCache(final CacheDataProvider inCacheController)
    {
        new Handler(mRealmThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                synchronized (Cacher.this) {
                    
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm inRealm) {
                            inRealm.where(inCacheController.GetClass()).findAll().deleteAllFromRealm();
                            inRealm.copyToRealm(inCacheController.ListToCache());
                        }
                    });

                    return false;
                }
            }
        }).sendEmptyMessage(0);
    }



    public <T extends RealmModel, U> void FindAll(final FindAllListener<T, U> inListener)
    {
        final Looper callingLooper = Looper.myLooper();
        new Handler(mRealmThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                synchronized (Cacher.this) {
                    final U result = inListener.Process(mRealm.where(inListener.GetClass()).findAll());

                    new Handler(callingLooper, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {
                            inListener.Found(result);
                            return false;
                        }
                    }).sendEmptyMessage(0);

                    return false;
                }
            }
        }).sendEmptyMessage(0);
    }



    @Override
    public void finalize()
    {
        if(mRealm != null)
            mRealm.close();

        if(mRealmThread != null)
            mRealmThread.quit();
    }



    public interface FindAllListener<T extends RealmModel, U> extends CacheController<T>
    {
        void Found(U inResults);
        U Process(List<T> inResults);
    }



    public interface CacheDataProvider<T extends RealmModel> extends CacheController<T>
    {
        List<T> ListToCache();
    }



    public interface CacheController<T extends RealmModel>
    {
        Class<T> GetClass();
    }
}
