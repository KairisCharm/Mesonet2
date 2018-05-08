package org.mesonet.app.contact

import android.Manifest
import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.databinding.ContactActivityBinding
import android.content.Intent
import android.net.Uri
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import org.mesonet.androidsystem.Permissions


class ContactActivity: BaseActivity()
{
    override fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, inAnimationIn: Int, inAnimationOut: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding: ContactActivityBinding = DataBindingUtil.setContentView(this, R.layout.contact_activity)

        binding.webButton.setOnClickListener {
            val url = "http://www.mesonet.org"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.emailButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "operator@mesonet.org", null))
            startActivity(Intent.createChooser(emailIntent, "Email OK Mesonet"))
        }

        binding.twitterButton.setOnClickListener {
            OpenAppWithBackup(Intent.ACTION_VIEW, "twitter://user?screen_name=okmesonet", Intent.ACTION_VIEW, "https://twitter.com/okmesonet")
        }

        binding.facebookButton.setOnClickListener {
            OpenAppWithBackup(Intent.ACTION_VIEW, "fb://page/101813120731", Intent.ACTION_VIEW, "https://www.facebook.com/mesonet")
        }

        binding.phoneButton.setOnClickListener {
            mPermissions.RequestPermission(Manifest.permission.CALL_PHONE, object: Permissions.PermissionListener{
                override fun GetContext(): Activity {
                    return this@ContactActivity
                }

                override fun PermissionGranted() {
                    if(ContextCompat.checkSelfPermission(this@ContactActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:4053252541")))
                        }
                        catch (e: Exception)
                        {

                        }
                    }
                }

                override fun PermissionDenied() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }

        binding.addressButton.setOnClickListener {
            try {
                val gmmIntentUri = Uri.parse("q=120+David+L.+Boren+Blvd.+Norman,+OK+73071")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"
                startActivity(mapIntent)
            }
            catch (e: Exception)
            {
                val gmmIntentUri = Uri.parse("https://www.google.com/maps?q=120+David+L.+Boren+Blvd.+Norman,+OK+73071")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(mapIntent)
            }
        }
    }


    fun OpenAppWithBackup(inIntent1: String, inUri1: String, inIntent2: String, inUri2: String )
    {
        try {
            OpenApp(inIntent1, inUri1)
        } catch (exception: ActivityNotFoundException) {
            OpenApp(inIntent2, inUri2)
        }

    }



    fun OpenApp(inIntent: String, inUri: String)
    {
        startActivity( Intent(inIntent, Uri.parse(inUri)))
    }
}