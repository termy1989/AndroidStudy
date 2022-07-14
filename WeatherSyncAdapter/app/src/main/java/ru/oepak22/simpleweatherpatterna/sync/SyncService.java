package ru.oepak22.simpleweatherpatterna.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter walletSyncAdapter = null;

    @Override
    public void onCreate() {

        Log.v("XXXX", "....oncreate sync service....");
        synchronized (sSyncAdapterLock){
            if(walletSyncAdapter == null){
                walletSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v("XXXX", "....onbind sync service....");
        return walletSyncAdapter.getSyncAdapterBinder();
    }
}