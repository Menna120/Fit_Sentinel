package com.example.fit_sentinel.domain.repository

interface SharedPreferencesRepo<T> {
    fun save(data: T)
    fun get(): T
    fun delete()
}