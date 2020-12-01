package com.android.android_libraries.mvp.presenter

import android.annotation.SuppressLint
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.repo.Common
import com.android.android_libraries.mvp.model.repo.RepositoriesRepo
import com.android.android_libraries.mvp.model.repo.UsersRepo
import com.android.android_libraries.mvp.presenter.list.IRepositoriesListPresenter
import com.android.android_libraries.mvp.view.MainView
import com.android.android_libraries.mvp.view.list.RepositoryItemView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response

@InjectViewState
class MainPresenter(var mainThread: Scheduler) : MvpPresenter<MainView>() {
    inner class RepositoriesListPresenter : IRepositoriesListPresenter {

        var repositories: MutableList<Repository> = ArrayList<Repository>()

        override fun bind(view: RepositoryItemView) {
            val repository: Repository = repositories[view.getCurrentPos()]
            view.setName(repository.name)
        }

        override fun getCount(): Int {
            return repositories.size
        }
    }

    private val USERNAME = "googlesamples"

    private var usersRepo: UsersRepo? = null
    private var common: Common? = null
    var repositoriesListPresenter: RepositoriesListPresenter? = null

    private var repositoriesRepo: RepositoriesRepo? = null

    init {
        repositoriesRepo = RepositoriesRepo()
        usersRepo = UsersRepo()
        common = Common
        repositoriesRepo = RepositoriesRepo()
        repositoriesListPresenter = RepositoriesListPresenter()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadUser()
    }

    @SuppressLint("CheckResult")
    private fun loadUser() {
        viewState.showLoading()
        usersRepo!!.getUser(USERNAME)
            .observeOn(mainThread)
            .doOnEvent { user, throwable ->
                setUser(user)
                setErrorMsg(throwable)
            }
            .observeOn(Schedulers.io())
            .flatMap { user -> getRepositoriesByUrl(user.repos_url) }
            .observeOn(mainThread)
            .subscribe({ repositories: List<Repository?>? -> setUserRepositories(repositories) }) { throwable: Throwable ->
                setErrorMsg(throwable)
            }
    }

    private fun setErrorMsg(throwable: Throwable) {
        viewState.hideLoading()
        viewState.showMessage(throwable.message)
    }

    private fun setUser(user: User?) {
        viewState.setUsername(user!!.login)
        viewState.loadImage(user.avatar)
    }

    private fun getRepositoriesByUrl(url: String): Single<List<Repository?>?>? {
        return repositoriesRepo!!.getRepositories(url)
    }

    private fun setUserRepositories(repositories: List<Repository?>?) {
        repositoriesListPresenter!!.repositories.clear()
        repositoriesListPresenter!!.repositories = repositories as MutableList<Repository>
        viewState.updateList()
        viewState.hideLoading()
    }
}