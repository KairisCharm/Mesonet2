package org.mesonet.app.webview.dependencyinjection;


import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.webview.WebViewFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerChildFragment
@Subcomponent(modules = WebViewFragmentModule.class)
public interface WebViewFragmentSubcomponent extends AndroidInjector<WebViewFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<WebViewFragment> {
    }
}