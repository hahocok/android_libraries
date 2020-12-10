package com.android.android_libraries.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.android_libraries.App
import com.android.android_libraries.R
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.android.android_libraries.mvp.presenter.MainPresenter
import com.android.android_libraries.mvp.view.MainView
import com.android.android_libraries.ui.adapter.RepositoriesAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var adapter: RepositoriesAdapter? = null

    @Inject
    lateinit var imageLoader: IImageLoader<ImageView>

    init {
        App.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @ProvidePresenter
    fun createPresenter(): MainPresenter {
        val presenter = MainPresenter(AndroidSchedulers.mainThread())
        App.component.inject(presenter)
        return presenter
    }

    override fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun init() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RepositoriesAdapter(presenter.repositoriesListPresenter)
        recyclerView.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showLoading() {
        rl_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rl_loading.visibility = View.GONE
    }

    override fun setUsername(username: String) {
        tv_username.text = username
    }

    override fun loadImage(url: String) {
        imageLoader.loadInto(url, iv_avatar)
    }
}