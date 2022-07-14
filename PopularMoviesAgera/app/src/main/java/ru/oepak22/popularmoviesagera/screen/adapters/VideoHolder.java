package ru.oepak22.popularmoviesagera.screen.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.popularmoviesagera.R;
import ru.oepak22.popularmoviesagera.model.content.Video;

public class VideoHolder extends RecyclerView.ViewHolder {

    TextView mVideoName;     // название трейлера
    TextView mVideoLink;     // ссылка

    public VideoHolder(@NonNull View itemView) {
        super(itemView);
        mVideoName = itemView.findViewById(R.id.video_name);
        mVideoLink = itemView.findViewById(R.id.video_link);
    }

    // размещение данных на элементе списка
    @SuppressLint("SetTextI18n")
    public void bind(@NonNull Video video) {
        mVideoName.setText(video.getName());
        //mVideoLink.setText("https://www.youtube.com/watch?v=" + video.getKey());
    }
}
