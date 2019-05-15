package demoapp.imgur.base


import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import demoapp.imgur.di.component.DaggerApplicationComponent

//Application class with dagger initializtion

class BaseApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)

        return component
    }
}
