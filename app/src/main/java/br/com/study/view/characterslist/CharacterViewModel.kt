package br.com.study.view.characterslist

import android.arch.lifecycle.ViewModel
import android.util.Log
import br.com.study.model.api.MarvelApi
import br.com.study.model.entity.Character
import io.reactivex.Observable

class CharacterViewModel : ViewModel(){

    var isLoading: Boolean = false
    private set

    var currentPage = -1
    private set

    private val characters = mutableListOf<Character>()

    fun load(page: Int): Observable<Character>{
        isLoading = true
        Log.d("PJO", "page $page | currentPage: $currentPage")

        return if (page <= currentPage){
            Observable.fromIterable(characters)
        }else{
            currentPage = page
            MarvelApi.getService().allCharacters( page * 20)
                .flatMapIterable{ response ->
                    response.data.results
                }
                .doOnNext{ c ->
                    characters.add(c)
                    Observable.just(c)
                }
        }.doOnComplete{ isLoading = false }
    }
    fun reset(){
        isLoading = false
        currentPage = -1
        characters.clear()
    }
}