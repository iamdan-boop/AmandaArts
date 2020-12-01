package com.arts.amanda.di

import com.arts.amanda.service.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
object RepositoryModule {

    @Provides
    @ActivityScoped
    fun provideFirebaseService() = FirebaseService()

}