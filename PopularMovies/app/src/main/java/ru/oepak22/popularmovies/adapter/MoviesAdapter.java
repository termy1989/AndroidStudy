package ru.oepak22.popularmovies.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.popularmovies.model.Movie;

// класс адаптера для списка фильмов
public class MoviesAdapter extends RecyclerView.Adapter<MovieHolder> {

    // список фильмов
    private final List<Movie> mMovies;

    // размер постера
    private final int mImageHeight;
    private final int mImageWidth;

    // выбор элемента списка по нажатию
    private final OnItemClickListener mOnItemClickListener;

    // обработчик нажатия на элемент списка
    private final View.OnClickListener mInternalListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Movie movie = (Movie) view.getTag();
            mOnItemClickListener.onItemClick(view, movie);
        }
    };

    // конструктор
    public MoviesAdapter(int imageHeight, int imageWidth, @NonNull OnItemClickListener onItemClickListener) {
        mMovies = new ArrayList<>();
        mImageHeight = imageHeight;
        mImageWidth = imageWidth;
        mOnItemClickListener = onItemClickListener;
    }

    // изменение списка
    public void changeDataSet(@NonNull List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    // создание поля для элемента списка
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieHolder.create(parent.getContext(), mImageHeight, mImageWidth);
    }

    // отображение элемента списка на экране
    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(movie);

        holder.itemView.setOnClickListener(mInternalListener);
        holder.itemView.setTag(movie);
    }

    // размер списка
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // интерфейс обработчика нажатия на элемент списка (для активности)
    public interface OnItemClickListener {

        void onItemClick(@NonNull View view, @NonNull Movie movie);

    }
}
