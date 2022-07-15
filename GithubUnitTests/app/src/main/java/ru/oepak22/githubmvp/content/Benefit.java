package ru.oepak22.githubmvp.content;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import ru.oepak22.githubmvp.R;

// список сообщений с котами при старте приложения
public enum Benefit {

    // константы
    WORK_TOGETHER(R.string.benefit_work_together, R.drawable.cat1),
    CODE_HISTORY(R.string.benefit_code_history, R.drawable.cat2),
    PUBLISH_SOURCE(R.string.benefit_publish_source, R.drawable.cat3),;

    private final int mTextId;          // сообщение
    private final int mDrawableId;      // изображение кота

    // конструктор
    Benefit(@StringRes int textId, @DrawableRes int drawableId) {
        mTextId = textId;
        mDrawableId = drawableId;
    }

    // получение сообщения из ресурсов
    @StringRes
    public int getTextId() {
        return mTextId;
    }

    // получение изображение кота из ресурсов
    @DrawableRes
    public int getDrawableId() {
        return mDrawableId;
    }

}
