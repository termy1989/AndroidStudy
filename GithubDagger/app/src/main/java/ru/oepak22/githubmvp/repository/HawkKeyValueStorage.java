package ru.oepak22.githubmvp.repository;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

public class HawkKeyValueStorage implements KeyValueStorage {

    @NonNull
    @Override
    public String getUserName() { return Hawk.get(USER_NAME_KEY, ""); }

    @Override
    public void saveUserName(@NonNull String userName) { Hawk.put(USER_NAME_KEY, userName); }

    @NonNull
    @Override
    public String getToken() { return Hawk.get(TOKEN_KEY, ""); }

    @Override
    public void saveToken(@NonNull String token) { Hawk.put(TOKEN_KEY, token); }

    @Override
    public void saveWalkthroughPassed() { Hawk.put(WALKTHROUGH_PASSED_KEY, true); }

    @Override
    public boolean isWalkthroughPassed() { return Hawk.get(WALKTHROUGH_PASSED_KEY, false); }
}
