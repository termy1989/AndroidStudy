package ru.oepak22.githubmvp.di;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.oepak22.githubmvp.api.ApiKeyInterceptor;
import ru.oepak22.githubmvp.api.GithubService;
import ru.oepak22.githubmvp.repository.DefaultGithubRepository;
import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.repository.HawkKeyValueStorage;
import ru.oepak22.githubmvp.repository.KeyValueStorage;

// класс поставщика зависимостей
@Module
public class DataModule {

    // предоставление зависимости GithubRepository
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    GithubRepository provideGithubRepository(
            @NonNull GithubService githubService,
            @NonNull KeyValueStorage keyValueStorage) {
        return new DefaultGithubRepository(githubService, keyValueStorage);
    }

    // предоставление зависимости GithubService
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    GithubService provideGithubService(@NonNull Retrofit retrofit) {
        return retrofit.create(GithubService.class);
    }

    // предоставление зависимости Retrofit
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    Retrofit provideRetrofit(@NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    // предоставление зависимости OkHttpClient
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                //.addInterceptor(httpLoggingInterceptor())
                .addInterceptor(ApiKeyInterceptor.create())
                .build();
    }

    // предоставление зависимости Realm
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    // предоставление зависимости KeyValueStorage
    // зависимость предоставляется один раз
    @Provides
    @Singleton
    KeyValueStorage provideKeyValueStorage() {
        return new HawkKeyValueStorage();
    }
}
