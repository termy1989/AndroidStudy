package ru.oepak22.githubmvp.utils;

import androidx.annotation.Nullable;

public final class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(@Nullable CharSequence text) {
        return text == null || text.length() == 0;
    }

}
