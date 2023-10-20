package safronov.apps.taskmate.project.app

import android.app.Application
import safronov.apps.taskmate.project.di.component.AppComponent
import safronov.apps.taskmate.project.di.component.DaggerAppComponent
import safronov.apps.taskmate.project.di.module.AppModule

class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(context = this@App)).build()
    }

    fun getAppComponent(): AppComponent = appComponent

    companion object {
        const val TAG = "sfrLog"
    }

}