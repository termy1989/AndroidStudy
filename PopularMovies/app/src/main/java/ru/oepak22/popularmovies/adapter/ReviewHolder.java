package ru.oepak22.popularmovies.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ru.oepak22.popularmovies.R;
import ru.oepak22.popularmovies.model.Review;

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