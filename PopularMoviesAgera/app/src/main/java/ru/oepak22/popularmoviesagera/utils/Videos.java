package ru.oepak22.popularmoviesagera.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import ru.oepak22.popularmoviesagera.model.content.Video;

public final class Videos {

    private static final String YOUTUBE = "https://www.youtube.com/watch?v=";

    private Videos() {}

    public static void browseVideo(@NonNull Context context, @NonNull Video video) {
        String videoUrl = YOUTUBE + video.getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        context.startActivity(intent);
    }

}
