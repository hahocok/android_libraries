package com.android.android_libraries.mvp.presenter

import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.repo.IUsersRepo
import com.android.android_libraries.mvp.presenter.di.DaggerTestComponent
import com.android.android_libraries.mvp.presenter.di.TestRepoModule
import com.android.android_libraries.mvp.view.MainView
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainPresenterUnitTest {

    private lateinit var presenter: MainPresenter
    private lateinit var testScheduler: TestScheduler
    @Mock
    private lateinit var mainView: MainView

    @Before
    fun setup() {
        Timber.d("setup")
        MockitoAnnotations.openMocks(this)

        testScheduler = TestScheduler()
        presenter = Mockito.spy(MainPresenter(testScheduler))
    }

    @After
    fun tearDown() {
        Timber.d("tearDown")
    }



    companion object {

        @BeforeClass
        fun setupClass() {
            Timber.plant(DebugTree())
            Timber.d("setupClass")
        }

        @AfterClass
        fun tearDownClass() {
            Timber.d("tearDownClass")
        }
    }


    @Test
    fun loadUserSuccess() {
        val user = User("googlesamples", "someAvatarUrl", "someReposUrl", "someName")
        val component = DaggerTestComponent.builder()
            .testRepoModule(object : TestRepoModule() {
                override fun usersRepo(): IUsersRepo {
                    val repo: IUsersRepo = super.usersRepo()
                    Mockito.`when`(repo.getUser(user.login)).thenReturn(Single.just(user))
                    Mockito.`when`(repo.getRepositories(user)).thenReturn(Single.just(ArrayList()))
                    return repo
                }
            })
            .build()
        component.inject(presenter)

        presenter.attachView(mainView)

        Mockito.verify(mainView).init()
        Mockito.verify(presenter, Mockito.atLeastOnce()).loadUser()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        Mockito.verify(mainView).run {
            showLoading()
            loadImage(user.avatar_url)
            setUsername(user.login)
            updateList()
            hideLoading()
        }

    }

    @Test
    fun loadUserFailure() {
        val user = User("googlesamples", "someAvatarUrl", "someReposUrl", "someName")
        val errorMessage = "network error"
        val component = DaggerTestComponent.builder()
                .testRepoModule(object : TestRepoModule() {
                    override fun usersRepo(): IUsersRepo {
                        val repo = super.usersRepo()
                        Mockito.`when`(repo.getUser(user.login))
                                .thenReturn(Single.error(Exception(errorMessage)))
                        Mockito.`when`(repo.getRepositories(user))
                                .thenReturn(Single.just(Collections.emptyList()))
                        return repo
                    }
                })
                .build()

        component.inject(presenter)

        presenter.attachView(mainView)
        Mockito.verify(mainView).init()
        Mockito.verify(presenter, Mockito.atLeastOnce()).loadUser()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        Mockito.verify(mainView).run {
            showLoading()
            hideLoading()
            showMessage(errorMessage)
        }
    }
}