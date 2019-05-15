package demoapp.imgur.base

import android.os.Bundle
import android.support.annotation.LayoutRes

import butterknife.ButterKnife
import dagger.android.support.DaggerAppCompatActivity

/*
    Base Activity to set layout and bind butterknife.
    This can also be done by kotlin synthatic

 */

abstract class BaseActivity : DaggerAppCompatActivity() {

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())

        ButterKnife.bind(this)
    }
}
