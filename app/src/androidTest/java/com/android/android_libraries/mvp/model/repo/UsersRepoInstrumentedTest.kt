package com.android.android_libraries.mvp.model.repo

import androidx.test.platform.app.InstrumentationRegistry
import com.android.android_libraries.di.module.ApiModule
import com.android.android_libraries.di.module.AppModule
import com.android.android_libraries.mvp.model.api.INetworkStatus
import com.android.android_libraries.mvp.model.api.INetworkStatus.Status
import com.android.android_libraries.mvp.model.cache.ICache
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.repo.di.DaggerTestInstrumentalComponent
import com.android.android_libraries.mvp.model.repo.di.TestInstrumentalComponent
import com.android.android_libraries.ui.NetworkStatus
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


class UsersRepoInstrumentedTest {

    @Inject
    lateinit var usersRepo: IUsersRepo

    @Inject
    lateinit var gson: Gson

    @Named("realm")
    @Inject
    lateinit var cache: ICache

    private val user: User = User("someUser", "someUrl", "someReposUrl", "someName")
    private val repositories: List<Repository> = listOf(Repository("id1", "name1"), Repository("id2", "name2"))

    companion object {
        var mockWebServer: MockWebServer = MockWebServer()
        var mockNetworkStatus: INetworkStatus = mockk()

        @BeforeClass
        @Throws(IOException::class)
        fun setupClass() {
            mockWebServer.start()
        }

        @AfterClass
        @Throws(IOException::class)
        fun tearDownClass() =
                mockWebServer.shutdown()
    }

    @Before
    fun setup() {
        val component: TestInstrumentalComponent = DaggerTestInstrumentalComponent.builder()
                .appModule(AppModule(InstrumentationRegistry.getInstrumentation().context))
                .apiModule(object : ApiModule() {
                    override fun baseUrl(): String =
                            mockWebServer.url("/").toString()


                })
                .build()
        component.inject(this)
    }

    @After
    fun tearDown() {}

    @Test
    fun getUser() {
        mockWebServer.enqueue(createUserResponse(user))
        val observer: TestObserver<User> = TestObserver()
        every { mockNetworkStatus.isOnline() } returns true
        usersRepo.getUser(user.login).subscribe(observer)

        observer.awaitTerminalEvent()

        observer.assertValueCount(1)
        assertEquals(observer.values()[0].login, user.login)
        assertEquals(observer.values()[0].avatar_url, user.avatar_url)
        assertEquals(observer.values()[0].name, user.name)
    }

    @Test
    fun getUserCacheSuccess() {
        mockWebServer.enqueue(createUserResponse(user))
        every { mockNetworkStatus.isOnline() } returns false
        var observer = TestObserver<User>()
        cache.saveUser(user.login, user).subscribe(observer)
        observer.awaitTerminalEvent()

        observer = TestObserver<User>()
        cache.getUser(user.login).subscribe(observer)
        observer.awaitTerminalEvent()

        observer.assertValueCount(1)
        assertEquals(observer.values()[0].login, "someUser")
        assertEquals(observer.values()[0].avatar_url, "someUrl")
        assertEquals(observer.values()[0].repos_url, "someReposUrl")
        assertEquals(observer.values()[0].name, "someName")
    }

    @Test
    fun getUserCacheFailure() {
        mockWebServer.enqueue(createUserResponse(user))
        val observer = TestObserver<User>()
        cache.getUser("wrongLogin").subscribe(observer)
        observer.awaitTerminalEvent()

        assertEquals(observer.errors()[0]::class, RuntimeException()::class)
    }

    private fun createUserResponse(user: User): MockResponse {
        val login = user.login
        val avatarUrl = user.avatar_url
        val reposUrl = user.repos_url
        val name = user.name
        val body = "{\"login\":\"$login\", \"avatar_url\":\"$avatarUrl\", \"repos_url\":\"$reposUrl\", \"name\":\"$name\"}"
        return MockResponse().setBody(body)
    }

    @Test
    fun getUserReposSuccess() {
        mockWebServer.enqueue(createUserReposResponse(repositories))
        val observer: TestObserver<List<Repository>> = TestObserver()
        usersRepo.getRepositories(user).subscribe(observer)

        observer.awaitTerminalEvent()

        observer.assertValueCount(1)
        assertEquals(observer.values()[0][0].id, "id1")
        assertEquals(observer.values()[0][0].name, "name1")
        assertEquals(observer.values()[0][1].id, "id2")
        assertEquals(observer.values()[0][1].name, "name2")
    }

    @Test
    fun getUserReposFailure() {
        mockWebServer.enqueue(createUserResponse(user))
        val observer = TestObserver<List<Repository>>()
        cache.getUserRepositories("wrongLogin").subscribe(observer)
        observer.awaitTerminalEvent()

        assertEquals(observer.errors()[0]::class, RuntimeException()::class)
    }

    private fun createUserReposResponse(repos: List<Repository>): MockResponse {
        val list: MutableList<Repository> = ArrayList(repos.size)
        for ((key, value) in repos) {
            list.add(Repository(key, value))
        }
        return MockResponse()
            .setBody(gson.toJson(list))
    }
}