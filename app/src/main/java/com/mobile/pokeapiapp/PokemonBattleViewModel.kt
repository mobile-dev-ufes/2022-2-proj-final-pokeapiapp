package com.mobile.pokeapiapp

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class PokemonBattleViewModel : ViewModel() {
    private var pokemon1Id = 0
    private var pokemon2Id = 0
    private var pokemon1Observable = PublishSubject.create<Int>()
    private var pokemon2Observable = PublishSubject.create<Int>()
    private var anyPokemonSetObservable = PublishSubject.create<Boolean>()

    fun setPokemon(id: Int){
        if (pokemon1Id == 0){
            pokemon1Id = id
            pokemon1Observable.onNext(pokemon1Id)
        } else if(pokemon2Id == 0) {
            pokemon2Id = id
            pokemon2Observable.onNext(pokemon2Id)
        }
        if (isAnyPokemonSet()) {
            anyPokemonSetObservable.onNext(true)
        } else {
            anyPokemonSetObservable.onNext(false)
        }
    }

    fun getPokemon1Id(): Int {
        return pokemon1Id
    }
    fun getPokemon2Id(): Int {
        return pokemon2Id
    }

    fun isPokemon1Set(): Boolean {
        return pokemon1Id != 0
    }
    fun isPokemon2Set(): Boolean {
        return pokemon2Id != 0
    }

    fun unsetPokemon(opponent: Int){
        if(opponent == 1){
            pokemon1Id = 0
            pokemon1Observable.onNext(pokemon1Id)
        } else if(opponent == 2) {
            pokemon2Id = 0
            pokemon2Observable.onNext(pokemon2Id)
        }
        if (isAnyPokemonSet()) {
            anyPokemonSetObservable.onNext(true)
        } else {
            anyPokemonSetObservable.onNext(false)
        }
    }

    fun getPokemon1Observable(): Observable<Int> {
        return pokemon1Observable
    }

    fun getPokemon2Observable(): Observable<Int> {
        return pokemon2Observable
    }

    fun getAnyPokemonObservable(): Observable<Boolean> {
        return anyPokemonSetObservable
    }

    fun isAnyPokemonSet(): Boolean {
        return pokemon1Id != 0 || pokemon2Id != 0
    }
    fun isBothPokemonSet(): Boolean {
        return pokemon1Id != 0 && pokemon2Id != 0
    }
}