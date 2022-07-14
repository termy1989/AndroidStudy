package ru.oepak22.popularmoviesclean.screen.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.domain.model.Review;
import ru.oepak22.popularmoviesclean.R;

public class ReviewHolder extends RecyclerView.ViewHolder {

    TextView mAutorName;     // ник автора
    TextView mUpdatedAt;     // дата изменения

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        mAutorName = itemView.findViewById(R.id.autor_name);
        mUpdatedAt = itemView.findViewById(R.id.updated_at);
    }

    // размещение данных на элементе списка
    @SuppressLint("SetTextI18n")
    public void bind(@NonNull Review review) {
        mAutorName.setText("  > " + review.getAuthor());
        //mUpdatedAt.setText(review.getUpdateAt());
    }
}