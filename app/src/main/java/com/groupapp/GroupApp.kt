package com.groupapp

import android.app.Application
import com.groupapp.data.GatheringRepository
import com.groupapp.data.LocalStore

class GroupApp : Application() {

    lateinit var repo: GatheringRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repo = GatheringRepository(LocalStore(this))
    }
}
