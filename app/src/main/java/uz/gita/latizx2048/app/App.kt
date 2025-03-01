package uz.gita.latizx2048.app

import android.app.Application
import android.content.Context
import uz.gita.latizx2048.data.LocalStorage

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalStorage.init(this.getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE))
    }
}