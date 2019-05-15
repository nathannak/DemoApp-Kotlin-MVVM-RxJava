package demoapp.imgur.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import demoapp.imgur.base.BaseApplication
import demoapp.imgur.di.module.ActivityBindingModule
import demoapp.imgur.di.module.ApplicationModule
import demoapp.imgur.di.module.ContextModule
import javax.inject.Singleton


/*
    Daggers application component
    all the modules are added referenced here.

 */
@Singleton
@Component(modules = [ContextModule::class,
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}
