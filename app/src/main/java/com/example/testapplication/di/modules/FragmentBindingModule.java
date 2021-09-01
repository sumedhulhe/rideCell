package com.example.testapplication.di.modules;



import com.example.testapplication.map.view.MapsFragment;
import com.example.testapplication.myprofile.view.MyProfileFragment;
import com.example.testapplication.signup.view.SignUpFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {


    @ContributesAndroidInjector
    abstract MyProfileFragment myProfileFragment();

    @ContributesAndroidInjector
    abstract SignUpFragment signUpFragment();

    @ContributesAndroidInjector
    abstract MapsFragment mapsFragment();


}