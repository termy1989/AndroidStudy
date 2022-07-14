package ru.oepak22.domain.model;

// класс трейлера
public class Video {

    private String mKey;        // ключ
    private String mName;       // название

    public Video() {}

    // конструктор
    public Video(String key, String name) {
        mKey = key;
        mName = name;
    }

    // GET и SET ключа
    public String getKey() {
        return mKey;
    }
    public void setKey(String key) {
        mKey = key;
    }

    // GET и SET названия
    public String getName() {
        return mName;
    }
    public void setName(String name) { mName = name; }

}
