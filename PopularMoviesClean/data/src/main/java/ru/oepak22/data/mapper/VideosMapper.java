package ru.oepak22.data.mapper;

import ru.oepak22.domain.model.Video;
import rx.functions.Func1;

// класс маппера полученных данных
public class VideosMapper implements Func1<ru.oepak22.data.model.content.Video, Video> {

    @Override
    public Video call(ru.oepak22.data.model.content.Video video) {
        return new Video(video.getKey(),
                            video.getName());
    }
}
