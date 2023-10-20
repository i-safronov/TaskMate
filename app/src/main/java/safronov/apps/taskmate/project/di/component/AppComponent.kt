package safronov.apps.taskmate.project.di.component

import dagger.Component
import safronov.apps.taskmate.project.di.module.AppModule
import safronov.apps.taskmate.project.di.module.DataModule
import safronov.apps.taskmate.project.di.module.DomainModule

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {
}