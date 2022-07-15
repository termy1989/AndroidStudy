package ru.oepak22.githubmvp.content;

import com.google.gson.annotations.SerializedName;

// класс результата авторизации
public class Authorization {

    @SerializedName("id")
    private int mId;

    /*@SerializedName("token")
    private String mToken;*/

    @SerializedName("login")
    private String mLogin;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    /*public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }*/

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }
}
