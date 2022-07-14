package ru.oepak22.popularmoviesagera.screen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.oepak22.popularmoviesagera.R;
import ru.oepak22.popularmoviesagera.model.content.Video;

public class VideosAdapter extends RecyclerView.Adapter<VideoHolder> {

    // список трейлеров
    private final List<Video> mVideos;

    // выбор трейлера из списка по нажатию
    private final OnItemClick mOnItemClick;

    // обработчик нажатия на элемент списка
    private final View.OnClickListener mInternalListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Video video = (Video) view.getTag();
            mOnItemClick.onItemClick(video);
        }
    };

    // конструктор
    public VideosAdapter(@NonNull List<Video> video, @NonNull OnItemClick onItemClick) {

        mVideos = video;                        // инициализация списка трейлеров
        mOnItemClick = onItemClick;             // инициализация переданного обработчика нажатия
    }

    // ? пересоздание списка ?
    public void changeDataSet(@NonNull List<Video> videos) {
        mVideos.clear();
        mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    // создание поля для элемента списка
    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.video_item, parent, false);
        return new VideoHolder(itemView);
    }

    // отображение объекта (трейлера) в элементе списка
    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = mVideos.get(position);
        holder.bind(video);
        holder.itemView.setTag(video);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    // размер списка
    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    // интерфейс обработчика нажатия на элемент списка (для активности)
    public interface OnItemClick { void onItemClick(@NonNull Video video); }
}
