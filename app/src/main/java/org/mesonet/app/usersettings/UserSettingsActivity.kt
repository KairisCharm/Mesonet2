package org.mesonet.app.usersettings

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.RadioGroup
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.databinding.UserSettingsActivityBinding
import org.mesonet.dataprocessing.userdata.Preferences
import javax.inject.Inject

class UserSettingsActivity: BaseActivity()
{
    @Inject
    lateinit var mPreferences: Preferences

    lateinit var mBinding: UserSettingsActivityBinding

    public override fun onCreate(inSavedInstanceState: Bundle?)
    {
        super.onCreate(inSavedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.user_settings_activity)

        mPreferences.UnitPreferencesSubject().subscribe (object: Observer<Preferences.UnitPreference>
        {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Preferences.UnitPreference) {
                when(t)
                {
                    Preferences.UnitPreference.kImperial -> mBinding.imperialButton.isChecked = true
                    Preferences.UnitPreference.kMetric -> mBinding.metricButton.isChecked = true
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext(Preferences.UnitPreference.kImperial)
            }

        })

        mBinding.unitsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.imperialButton -> mPreferences.SetUnitPreference(Preferences.UnitPreference.kImperial)
                R.id.metricButton -> mPreferences.SetUnitPreference(Preferences.UnitPreference.kMetric)
            }
        }
    }
    override fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, inAnimationIn: Int, inAnimationOut: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}