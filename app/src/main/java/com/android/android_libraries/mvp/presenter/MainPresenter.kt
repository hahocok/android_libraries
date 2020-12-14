package com.android.android_libraries.mvp.presenter

import android.annotation.SuppressLint
import com.android.android_libraries.mvp.model.entity.Repository
import com.android.android_libraries.mvp.model.entity.User
import com.android.android_libraries.mvp.model.repo.IUsersRepo
import com.android.android_libraries.mvp.model.repo.UsersRepo
import com.android.android_libraries.mvp.presenter.list.IRepositoriesListPresenter
import com.android.android_libraries.mvp.view.MainView
import com.android.android_libraries.mvp.view.list.RepositoryItemView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@InjectViewState
open class MainPresenter(var mainThread: Scheduler) : MvpPresenter<MainView>() {
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

    @Inject
    lateinit var usersRepo: IUsersRepo

    var repositoriesListPresenter: RepositoriesListPresenter = RepositoriesListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadUser()
    }

    @SuppressLint("CheckResult")
    fun loadUser() {
        viewState?.showLoading()
        usersRepo.getUser(USERNAME)
            .observeOn(mainThread)
            .doOnEvent { user, throwable ->
                setUser(user)
                throwable?.let { setErrorMsg(it) }
            }
            .observeOn(Schedulers.io())
            .flatMap { user -> getRepositoriesByUrl(user) }
            .observeOn(mainThread)
            .subscribe({ repositories: List<Repository> -> setUserRepositories(repositories) }) { throwable: Throwable ->
                setErrorMsg(throwable)
            }
    }

    private fun setErrorMsg(throwable: Throwable) {
        viewState.hideLoading()
        throwable.message?.let { viewState.showMessage(it) }
    }

    private fun setUser(user: User) {
        viewState.setUsername(user.login)
        viewState.loadImage(user.avatar_url)
    }

    private fun getRepositoriesByUrl(user: User): Single<List<Repository>> {
        return usersRepo.getRepositories(user)
    }

    private fun setUserRepositories(repositories: List<Repository>) {
        repositoriesListPresenter.repositories.clear()
        repositoriesListPresenter.repositories = repositories.toMutableList()
        viewState.updateList()
        viewState.hideLoading()
    }
}