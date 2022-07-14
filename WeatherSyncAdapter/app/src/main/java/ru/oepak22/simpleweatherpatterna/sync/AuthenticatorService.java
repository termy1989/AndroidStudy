package ru.oepak22.simpleweatherpatterna.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AuthenticatorService extends Service {

    private Authenticator walletAuthenticator;

    @Override
    public void onCreate() {
        walletAuthenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return walletAuthenticator.getIBinder();
    }
}