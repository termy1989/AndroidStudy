package ru.oepak22.simpleweatherpatterna;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import ru.oepak22.simpleweatherpatterna.sqlite.CityContentProvider;

public class WeatherApp extends Application {

    public static Account newAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        //SyncAdapter.initializeSyncAdapter(this);

        AccountManager accountManager =
                (AccountManager) getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE);

        newAccount = new Account(
                getApplicationContext().getString(R.string.app_name), CityContentProvider.ACOOUNT_TYPE);

        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return; // error
            }

            // Without calling setSyncAutomatically, our periodic sync will not be enabled.
            ContentResolver.setSyncAutomatically(newAccount, CityContentProvider.AUTHORITY, true);

            /*SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(5, 5/3).
                    setSyncAdapter(newAccount, WalletContentProvider.AUTHORITY).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);*/

            /*ContentResolver.addPeriodicSync(newAccount,
                    WalletContentProvider.AUTHORITY, new Bundle(), 600L);*/

            /*ContentResolver.addPeriodicSync(newAccount,
                    CityContentProvider.AUTHORITY, new Bundle(), 900L);*/

            /*ContentResolver.addPeriodicSync(newAccount,
                    WalletContentProvider.AUTHORITY, new Bundle(), 1800L);
            ContentResolver.addPeriodicSync(newAccount,
                    WalletContentProvider.AUTHORITY, new Bundle(), 3600L);
            ContentResolver.addPeriodicSync(newAccount,
                    WalletContentProvider.AUTHORITY, new Bundle(), 10800L);
            ContentResolver.addPeriodicSync(newAccount,
                    WalletContentProvider.AUTHORITY, new Bundle(), 86400L);*/


            // Finally, let's do a sync to get things started

            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

            ContentResolver.requestSync(newAccount,
                    CityContentProvider.AUTHORITY, bundle);

            //ContentResolver.addPeriodicSync(newAccount,
            //        WalletContentProvider.AUTHORITY, Bundle.EMPTY, 3600);

            Log.v("XXXX", "syncImmediately: ");
        }
    }
}
