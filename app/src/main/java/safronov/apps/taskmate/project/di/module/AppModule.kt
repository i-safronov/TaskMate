package safronov.apps.taskmate.project.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {

    @Provides @Singleton
    fun provideAppContext() = context

}