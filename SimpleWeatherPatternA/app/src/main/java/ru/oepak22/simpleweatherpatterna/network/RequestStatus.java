package ru.oepak22.simpleweatherpatterna.network;

// варианты статуса запроса
public enum RequestStatus {

    IN_PROGRESS,                // показывается процесс загрузки
    SUCCESS,                    // информация передается подписчику для вывода на интерфейс
    ERROR                       // показывается ошибка и "прокидывается" дальше
}
