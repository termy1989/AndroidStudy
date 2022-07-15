package ru.oepak22.githubmvp.screen.repositories;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.test.MockLifecycleHandler;
import ru.oepak22.githubmvp.test.TestGithubRepository;
import rx.Observable;

@RunWith(JUnit4.class)
public class RepositoriesPresenterTest {

    private RepositoriesPresenter mPresenter;
    private RepositoriesView repositoriesView;

    // инициализация объектов
    @Before
    public void setUp() throws Exception {

        LifecycleHandler lifecycleHandler = new MockLifecycleHandler();

        repositoriesView = Mockito.mock(RepositoriesView.class);
        mPresenter = new RepositoriesPresenter(lifecycleHandler, repositoriesView);
    }

    // проверка на то, что презентер корректно создается и инициализируется
    @Test
    public void testCreated() throws Exception {
        assertNotNull(mPresenter);
    }

    // проверка на отсутствие каких-либо действий
    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(repositoriesView);
    }

    // проверка на вывод списка репозиториев
    @Test
    public void testSuccessRepo() throws Exception {

        // инициализация списка репозиториев
        List<Repository> repositories = new ArrayList<>();
        Repository repository = new Repository("repo1",true, "acv",
                                                "en", 0, 0, 0);
        repositories.add(repository);

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new RepositoriesPresenterTest.RepoTestRepository(repositories));

        // инициализация презентера
        mPresenter.init();

        // вывод списка репозиториев
        Mockito.verify(repositoriesView, times(1)).showLoading();                            // показ индикатора загрузки
        Mockito.verify(repositoriesView, times(1)).hideLoading();                            // закрытие индикатора загрузки
        Mockito.verify(repositoriesView).showRepositories(repositories);                                           // вывод списка
    }

    // проверка на пустой список репозиториев
    @Test
    public void testErrorRepo() throws Exception {

        // инициализация пустого списка репозиториев
        List<Repository> repositories = new ArrayList<>();

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new RepositoriesPresenterTest.RepoTestRepository(repositories));

        // инициализация презентера
        mPresenter.init();

        // ошибка - пустой список
        Mockito.verify(repositoriesView, times(1)).showLoading();                             // показ индикатора загрузки
        Mockito.verify(repositoriesView, times(1)).hideLoading();                             // закрытие индикатора загрузки
        Mockito.verify(repositoriesView).showError();                                                               // ошибка
    }

    @Test
    public void testClick() throws Exception {

        // инициализация списка репозиториев
        List<Repository> repositories = new ArrayList<>();
        Repository repository = new Repository("repo1", true, "acv",
                                            "en", 0, 0, 0);
        repositories.add(repository);

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new RepositoriesPresenterTest.RepoTestRepository(repositories));

        // выбор репозитория из списка
        mPresenter.onItemClick(repository);

        // вывод списка коммитов репозитория
        Mockito.verify(repositoriesView).showCommits(repository);
    }

    // конец работы
    @SuppressWarnings("ConstantConditions")
    @After
    public void tearDown() throws Exception {
        RepositoryProvider.setKeyValueStorage(null);
        RepositoryProvider.setGithubRepository(null);
    }

    // класс тестового репозитория с переопределенным выводом списка репозиториев
    private class RepoTestRepository extends TestGithubRepository {

        private List<Repository> list;

        public RepoTestRepository(List<Repository> list) { this.list = list; }

        @NonNull
        @Override
        public Observable<List<Repository>> repositories() {

            if (list.isEmpty()) return Observable.error(new IOException()); //return Observable.empty();
            return Observable.just(this.list);
        }
    }
}