package ru.oepak22.popularmoviesclean.screen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.oepak22.domain.model.Review;
import ru.oepak22.popularmoviesclean.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewHolder> {

    // список ревьюшек
    private final List<Review> mReviews;

    // выбор ревью из списка по нажатию
    private final OnItemClick mOnItemClick;

    // обработчик нажатия на элемент списка
    private final View.OnClickListener mInternalListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Review review = (Review) view.getTag();
            mOnItemClick.onItemClick(review);
        }
    };

    // конструктор
    public ReviewsAdapter(@NonNull List<Review> reviews, @NonNull OnItemClick onItemClick) {

        mReviews = reviews;                     // инициализация списка ревью
        mOnItemClick = onItemClick;             // инициализация переданного обработчика нажатия
    }

    // ? пересоздание списка ?
    public void changeDataSet(@NonNull List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    // создание поля для элемента списка
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(itemView);
    }

    // отображение объекта (ревью) в элементе списка
    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.bind(review);
        holder.itemView.setTag(review);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    // размер списка
    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    // интерфейс обработчика нажатия на элемент списка (для активности)
    public interface OnItemClick { void onItemClick(@NonNull Review review); }
}
