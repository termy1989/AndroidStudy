package ru.oepak22.githubmvp.content;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public final class GsonHolder {

    private GsonHolder() {
    }

    @NonNull
    public static Gson getGson() {
        return Holder.GSON;
    }

    public static final class Holder {
        private static final Gson GSON = new Gson();
    }

}
