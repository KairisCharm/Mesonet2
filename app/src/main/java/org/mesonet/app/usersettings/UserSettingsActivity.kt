package org.mesonet.app.usersettings

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.mesonet.app.MainActivity
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.databinding.UserSettingsActivityBinding
import org.mesonet.dataprocessing.userdata.Preferences
import javax.inject.Inject
import android.content.Intent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class UserSettingsActivity: BaseActivity()
{
    @Inject
    lateinit var mPreferences: Preferences

    lateinit var mBinding: UserSettingsActivityBinding

    public override fun onCreate(inSavedInstanceState: Bundle?)
    {
        super.onCreate(inSavedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.user_settings_activity)

        mPreferences.UnitPreferencesObservable(this).subscribe (object: Observer<Preferences.UnitPreference>
        {
            var disposable: Disposable? = null
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable)
            {
                disposable = d
            }
            override fun onNext(t: Preferences.UnitPreference) {
                when (t) {
                    Preferences.UnitPreference.kImperial -> mBinding.imperialButton.isChecked = true
                    Preferences.UnitPreference.kMetric -> mBinding.metricButton.isChecked = true
                }

                disposable?.dispose()
                disposable = null
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext(Preferences.UnitPreference.kImperial)
            }

        })

        mBinding.unitsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val preference= when(checkedId) {
                R.id.imperialButton -> Preferences.UnitPreference.kImperial
                R.id.metricButton -> Preferences.UnitPreference.kMetric
                else -> Preferences.UnitPreference.kImperial
            }

            mPreferences.SetUnitPreference(this, preference).subscribe(object: SingleObserver<Int>{
                override fun onSuccess(t: Int) {}
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
        }
    }


    override fun onBackPressed() {
        mPreferences.UnitPreferencesObservable(this).observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Preferences.UnitPreference>{
            var disposable: Disposable? = null
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable)
            {
                disposable = d
            }

            override fun onNext(t: Preferences.UnitPreference) {
                val returnIntent = Intent()
                returnIntent.putExtra(MainActivity.kUserSettingsResultName, t.name)
                setResult(MainActivity.kUserSettingsRequestCode, returnIntent)
                finish()
                disposable?.dispose()
                disposable = null
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }


    override fun onDestroy() {
        mPreferences.Dispose()
        super.onDestroy()
    }

    override fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, inAnimationIn: Int, inAnimationOut: Int) {}
}