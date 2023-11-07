package safronov.apps.taskmate.project.di.component

import dagger.Component
import safronov.apps.taskmate.project.di.module.AppModule
import safronov.apps.taskmate.project.di.module.DataModule
import safronov.apps.taskmate.project.di.module.DomainModule
import safronov.apps.taskmate.project.ui.activity.MainActivity
import safronov.apps.taskmate.project.ui.fragment.fragment_main.FragmentMain
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.FragmentCreateTaskList
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.FragmentCreateTaskText
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.search.FragmentSearchTasks
import safronov.apps.taskmate.project.ui.fragment.start.FragmentStart
import safronov.apps.taskmate.project.ui.fragment.welcome.FragmentWelcome
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(fragmentStart: FragmentStart)
    fun inject(fragmentWelcome: FragmentWelcome)
    fun inject(fragmentMain: FragmentMain)
    fun inject(fragmentCreateTaskText: FragmentCreateTaskText)
    fun inject(fragmentCreateTaskList: FragmentCreateTaskList)
    fun inject(fragmentSearchTasks: FragmentSearchTasks)

}