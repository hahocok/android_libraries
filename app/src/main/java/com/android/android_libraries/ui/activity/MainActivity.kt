package com.android.android_libraries.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.android_libraries.databinding.ActivityMainBinding
import com.android.android_libraries.mvp.model.image.IImageLoader
import com.android.android_libraries.mvp.presenter.MainPresenter
import com.android.android_libraries.mvp.view.MainView
import com.android.android_libraries.ui.adapter.RepositoriesAdapter
import com.android.android_libraries.ui.image.GlideImageLoader
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    var adapter: RepositoriesAdapter? = null

    var imageLoader: IImageLoader<ImageView> = GlideImageLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    @ProvidePresenter
    fun createPresenter(): MainPresenter {
        return MainPresenter(AndroidSchedulers.mainThread())
    }

    override fun showMessage(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun init() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = RepositoriesAdapter(presenter.repositoriesListPresenter)
        binding.rv.adapter = adapter
    }

    override fun updateList() {
        adapter!!.notifyDataSetChanged()
    }

    override fun showLoading() {
        binding.rlLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.rlLoading.visibility = View.GONE
    }

    override fun setUsername(username: String?) {
        binding.tvUsername.text = username
    }

    override fun loadImage(url: String?) {
        imageLoader.loadInto(url, binding.ivAvatar)
    }
}