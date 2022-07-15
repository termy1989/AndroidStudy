package ru.oepak22.githubmvp.screen.walkthrough;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Benefit;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.screen.auth.AuthPresenterTest;
import ru.oepak22.githubmvp.test.TestKeyValueStorage;

@RunWith(JUnit4.class)
public class WalkthroughPresenterTest {

    private WalkthroughPresenter mPresenter;
    private WalkthroughView mView;

    // инициализация объектов
    @Before
    public void setUp() throws Exception {

        mView = Mockito.mock(WalkthroughView.class);
        mPresenter = new WalkthroughPresenter(mView);
    }

    // проверка на то, что презентер корректно создается и инициализируется
    @Test
    public void testCreated() throws Exception {
        assertNotNull(mPresenter);
    }

    // проверка на отсутствие каких-либо действий
    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(mView);
    }

    // проверка для init на не первый запуск приложения
    @Test
    public void testIsPassed() throws Exception {

        // инициализация хранилища ключей - с ключом
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(true);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        // переход на окно авторизации
        Mockito.verify(mView).startAuth();
    }

    // проверка для init на первый запуск приложения
    @Test
    public void testIsNotPassed() throws Exception {

        // инициализация хранилища ключей - без ключа
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        // инициализация списка окошек
        Mockito.verify(mView).showBenefits(new ArrayList<Benefit>() {
            {
                add(Benefit.WORK_TOGETHER);
                add(Benefit.CODE_HISTORY);
                add(Benefit.PUBLISH_SOURCE);
            }
        });

        // кнопка "next"
        Mockito.verify(mView).showActionButtonText(R.string.next_uppercase);
    }

    // проверка нажатия на кнопку c переходом на последнюю страницу
    @Test
    public void testButtonClickIsLast() {

        // инициализация хранилища ключей - без ключа
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        // два нажатия на кнопку
        mPresenter.onActionButtonClick();
        mPresenter.onActionButtonClick();

        // финишная надпись на кнопке
        Mockito.verify(mView).showActionButtonText(R.string.finish_uppercase);

        // переход на следующую страницу
        Mockito.verify(mView, times(2)).scrollToNextBenefit();
    }

    // проверка нажатия на кнопку без перехода на последнюю страницу
    @Test
    public void testButtonClickIsNotLast() {

        // инициализация хранилища ключей - без ключа
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        // нажатие на кнопку
        mPresenter.onActionButtonClick();

        // не финишная надпись на кнопке
        Mockito.verify(mView, times(2)).showActionButtonText(R.string.next_uppercase);

        // переход на следующую страницу
        Mockito.verify(mView).scrollToNextBenefit();
    }

    // проверка onPageChanged
    @Test
    public void testOnPageChangedNotLast() throws Exception {

        // инициализация хранилища ключей - без ключа
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        mPresenter.onPageChanged(1, true);
        Mockito.verify(mView, times(2)).showActionButtonText(R.string.next_uppercase);
    }

    // проверка onPageChanged
    @Test
    public void testOnPageChangedLast() throws Exception {

        // инициализация хранилища ключей - без ключа
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        mPresenter.onPageChanged(2, true);
        Mockito.verify(mView).showActionButtonText(R.string.finish_uppercase);
    }

    // проверка onPageChanged
    @Test
    public void testOnPageChangedNotUser() throws Exception {
        mPresenter.onPageChanged(2, false);
        Mockito.verifyNoMoreInteractions(mView);
    }

    @Test
    public void testBig() throws Exception {

        // обнуление токена
        KeyValueStorage storage = Mockito.mock(KeyValueStorage.class);
        Mockito.when(storage.isWalkthroughPassed()).thenReturn(false);
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();
        Mockito.verify(mView).showBenefits(new ArrayList<Benefit>() {
            {
                add(Benefit.WORK_TOGETHER);
                add(Benefit.CODE_HISTORY);
                add(Benefit.PUBLISH_SOURCE);
            }
        });
        Mockito.verify(mView).showActionButtonText(R.string.next_uppercase);

        mPresenter.onActionButtonClick();
        Mockito.verify(mView, times(2)).showActionButtonText(R.string.next_uppercase);
        Mockito.verify(mView).scrollToNextBenefit();

        //mPresenter.onPageChanged(0, true);
        //Mockito.verify(mView, times(3)).showActionButtonText(R.string.next_uppercase);

        mPresenter.onActionButtonClick();
        Mockito.verify(mView, times(2)).showActionButtonText(R.string.next_uppercase);
        Mockito.verify(mView, times(2)).scrollToNextBenefit();

        mPresenter.onActionButtonClick();
        Mockito.verify(mView).showActionButtonText(R.string.finish_uppercase);
        Mockito.verify(mView, times(2)).scrollToNextBenefit();

        mPresenter.onActionButtonClick();
        Mockito.verify(mView, times(2)).startAuth();
    }

    // конец работы
    @SuppressWarnings("ConstantConditions")
    @After
    public void tearDown() throws Exception {
        RepositoryProvider.setKeyValueStorage(null);
        RepositoryProvider.setGithubRepository(null);
    }
}
