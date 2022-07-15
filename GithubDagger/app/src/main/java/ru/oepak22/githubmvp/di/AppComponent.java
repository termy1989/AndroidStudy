package ru.oepak22.githubmvp.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.screen.auth.AuthActivity;
import ru.oepak22.githubmvp.screen.commits.CommitsActivity;
import ru.oepak22.githubmvp.screen.repositories.RepositoriesActivity;
import ru.oepak22.githubmvp.screen.walkthrough.WalkthroughActivity;

//интерфейс для внедрения зависимостей в активности
@Singleton
@Component(modules = {DataModule.class})
public interface AppComponent {

    void injectAuthActivity(AuthActivity authActivity);
    void injectRepositoriesActivity(RepositoriesActivity repositoriesActivity);
    void injectWalkthroughActivity(WalkthroughActivity walkthroughActivity);
    void injectCommitsActivity(CommitsActivity commitsActivity);
    KeyValueStorage keyValueStorage();
}
