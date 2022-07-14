package ru.oepak22.popularmoviesagera.screen.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.popularmoviesagera.R;
import ru.oepak22.popularmoviesagera.model.content.Movie;
import ru.oepak22.popularmoviesagera.utils.Images;

// класс показа данных элемента списка
public class MovieHolder extends RecyclerView.ViewHolder {

    // в списке показывается только изображение
    private ImageView mImageView;

    // создание элемента списка
    @NonNull
    public static MovieHolder create(@NonNull Context context, int imageHeight, int imageWidth) {
        View view = View.inflate(context, R.layout.image_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = imageHeight;
        layoutParams.width = imageWidth;
        imageView.requestLayout();
        return new MovieHolder(view);
    }

    // конструктор
    private MovieHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image);
    }

    // загрузка изображения и размещение его в списке
    public void bind(@NonNull Movie movie) {
        Images.loadMovie(mImageView, movie, Images.WIDTH_185);
    }
}
