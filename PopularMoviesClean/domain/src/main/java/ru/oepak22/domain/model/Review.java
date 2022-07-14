package ru.oepak22.domain.model;

// класс описания фильма
public class Review {

    private String mId;             // идентификатор
    private String mAuthor;         // автор
    private String mContent;        // содержимое
    private String mUpdate;         // дата обновления

    public Review() {}

    // конструктор
    public Review(String id, String author, String content, String update) {
        mId = id;
        mAuthor = author;
        mContent = content;
        mUpdate = update;
    }

    // GET и SET идентификатора
    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    // GET и SET автора
    public String getAuthor() {
        return mAuthor;
    }
    public void setAuthor(String author) {
        mAuthor = author;
    }

    // GET и SET содержимого
    public String getContent() {
        return mContent;
    }
    public void setContent(String content) {
        mContent = content;
    }

    // GET и SET даты
    public String getUpdateAt() {
        return mUpdate;
    }
    public void setUpdateAt(String update) {
        mUpdate = update;
    }
}
