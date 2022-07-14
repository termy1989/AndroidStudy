package ru.oepak22.simpleweatherpatterna.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import ru.arturvasilov.sqlite.core.SQLite;
import ru.arturvasilov.sqlite.core.Where;
import ru.oepak22.simpleweatherpatterna.adapter.GsonHolder;
import ru.oepak22.simpleweatherpatterna.model.AllCities;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.tables.AllCitiesTable;
import ru.oepak22.simpleweatherpatterna.tables.CityTable;
import ru.oepak22.simpleweatherpatterna.tables.RequestTable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

// класс сервиса для запросов, размещения результатов в базе и уведомления подписчиков
public class NetworkService extends IntentService {


    private static final String REQUEST_KEY = "request";

    // запуск сервиса
    public static void start(@NonNull Context context, @NonNull Request request/*, @NonNull String cityName*/) {

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(REQUEST_KEY, GsonHolder.getGson().toJson(request));
        context.startService(intent);
    }

    // конструктор
    @SuppressWarnings("unused")
    public NetworkService() {
        super(NetworkService.class.getName());
    }

    // главный процесс, исполняемый сервисом
    @Override
    protected void onHandleIntent(Intent intent) {

        // получение текущей записи о запросе погодной информации
        Request request = GsonHolder.getGson().fromJson(intent.getStringExtra(REQUEST_KEY), Request.class);
        Request savedRequest = SQLite.get().querySingle(RequestTable.TABLE,
                Where.create().equalTo(RequestTable.REQUEST, request.getRequest()));

        // если запрос находится в процессе выполнения, то этот новый вызов игнорируется
        if (savedRequest != null && request.getStatus() == RequestStatus.IN_PROGRESS) {
            return;
        }

        // перевод запрос в статус IN_PROGRESS и уведомление подписчиков
        // чтобы отображался процесс загрузки
        request.setStatus(RequestStatus.IN_PROGRESS);
        SQLite.get().insert(RequestTable.TABLE, request);
        SQLite.get().notifyTableChanged(RequestTable.TABLE);

        // если запрос из таблицы соответствует текущему запросу о погоде
        if (TextUtils.equals(NetworkRequest.CITY_WEATHER, request.getRequest())) {

            // исполнение сетевого запроса
            executeCityRequest(request);
        }
    }

    // сетевой запрос
    private void executeCityRequest(@NonNull Request request/*, @NonNull String cityName*/) {

        try {
            // получение информации обо всех городах
            AllCities list = ApiFactory.getWeatherService()
                    .getCityList()
                    .execute()
                    .body();

            SQLite.get().delete(AllCitiesTable.TABLE);

            SQLite.get().insert(AllCitiesTable.TABLE, list);

            request.setStatus(RequestStatus.SUCCESS);
        }
        catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(Objects.requireNonNull(e.getMessage()));
        }
        finally {

            // помещение в таблицу запроса о городе
            SQLite.get().insert(RequestTable.TABLE, request);

            // уведомление об изменении в таблице запросов
            SQLite.get().notifyTableChanged(RequestTable.TABLE);
        }
    }

}