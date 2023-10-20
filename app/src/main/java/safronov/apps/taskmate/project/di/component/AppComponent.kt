package safronov.apps.taskmate.project.di.component

import dagger.Component
import safronov.apps.taskmate.project.di.module.AppModule
import safronov.apps.taskmate.project.di.module.DataModule
import safronov.apps.taskmate.project.di.module.DomainModule
import safronov.apps.taskmate.project.ui.fragment.start.FragmentStart
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    fun inject(fragmentStart: FragmentStart)

}