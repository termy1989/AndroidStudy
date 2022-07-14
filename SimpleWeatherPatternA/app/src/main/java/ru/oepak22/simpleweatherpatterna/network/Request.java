package ru.oepak22.simpleweatherpatterna.network;

import androidx.annotation.NonNull;

// класс статуса и информации о каждом запросе
public class Request {

    // идентификатор запроса
    @NetworkRequest
    private final String mRequest;

    // статус запроса
    private RequestStatus mStatus;

    // ошибка запроса
    private String mError;

    // неполный конструктор
    public Request(@NonNull @NetworkRequest String request) {
        mRequest = request;
    }

    // полный конструктор
    public Request(@NonNull @NetworkRequest String request,
                   @NonNull RequestStatus status, @NonNull String error) {
        mRequest = request;
        mStatus = status;
        mError = error;
    }

    // получение идентификатора запроса
    @NetworkRequest
    @NonNull
    public String getRequest() {
        return mRequest;
    }

    // GET и SET статуса запроса
    @NonNull
    public RequestStatus getStatus() {
        return mStatus;
    }
    public void setStatus(@NonNull RequestStatus status) {
        mStatus = status;
    }

    // GET и SET ошибки запроса
    @NonNull
    public String getError() {
        return mError;
    }
    public void setError(@NonNull String error) {
        mError = error;
    }
}
