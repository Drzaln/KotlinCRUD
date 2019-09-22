package com.example.kotlincrud

import android.app.Application
import com.example.kotlincrud.data.model.remote.BelanjaApi
import com.example.kotlincrud.data.model.repository.ProductRepository

class App : Application() {
    val repository: ProductRepository by lazy {
        ProductRepository(BelanjaApi.create())
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        @Volatile
        private var INSTANCE: App? = null
        val instance: App
            get() {
                if (INSTANCE == null) {
                    synchronized(App::class.java) {
                        if (INSTANCE == null) {
                            throw RuntimeException("Something Wrong!!")
                        }
                    }
                }
                return INSTANCE!!
            }
    }
}