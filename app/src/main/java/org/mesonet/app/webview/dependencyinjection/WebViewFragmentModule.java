package org.mesonet.app.webview.dependencyinjection;


import android.support.v4.app.Fragment;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.webview.WebViewFragment;

import dagger.Binds;
import dagger.Module;

@Module
abstract class WebViewFragmentModule
{
    @Binds
    @PerChildFragment
    abstract Fragment fragment(WebViewFragment inWebViewFragment);
}
