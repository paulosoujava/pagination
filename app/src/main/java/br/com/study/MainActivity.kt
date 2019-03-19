package br.com.study

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import br.com.study.view.characterslist.CharacterAdapter
import br.com.study.view.characterslist.CharacterViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CharacterViewModel by lazy {
        ViewModelProviders.of(this).get(CharacterViewModel::class.java)
    }

    private val adapter: CharacterAdapter by lazy {
        CharacterAdapter()
    }

    private var recyclerState: Parcelable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llm = LinearLayoutManager(this)
        recyclerCharacters.layoutManager = llm
        recyclerCharacters.adapter = adapter
        recyclerCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition = llm.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == adapter.itemCount - 1 && !viewModel.isLoading) {
                    Log.d("PJO", "loading more...")
                    loadCharacter(viewModel.currentPage + 1)
                }
            }
        })
        loadCharacter(0)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("lmState", recyclerCharacters.layoutManager?.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        recyclerState = savedInstanceState?.getParcelable("lmState")
    }

    private fun loadCharacter(page: Int) {
        val disposable = viewModel.load(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    adapter.add(it)
                    Log.d("PJO", "${it.id} - ${it.name}")
                },
                { e -> Log.d("PJO", "Error: $e") },
                {
                    if (recyclerState != null) {

                        recyclerCharacters.layoutManager?.onRestoreInstanceState(recyclerState)
                        recyclerState = null

                    }
                }
            )
    }
}
