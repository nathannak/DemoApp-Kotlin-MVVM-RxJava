package demoapp.imgur.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import demoapp.imgur.ui.detail.DetailActivity
import demoapp.imgur.ui.main.MainActivity

/*
    Binding to Main Activity and Detail Activity
 */

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity


    @ContributesAndroidInjector
    abstract fun bindDetailActivity(): DetailActivity
}
