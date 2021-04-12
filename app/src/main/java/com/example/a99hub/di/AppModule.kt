package com.example.a99hub.di

import android.content.Context
import com.example.a99hub.data.dataStore.LimitManager
import com.example.a99hub.data.dataStore.ProfileManager
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.network.*
import com.example.a99hub.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    /*---------------START-LOGIN----------------*/
    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java)
    }

    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        userPreferences: UserManager
    ): AuthRepository {
        return AuthRepository(authApi, userPreferences)
    }
    /*--------------END-LOGIN----------------*/

    /*-------------START-HOME-----------------*/
    @Singleton
    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource,
    ): HomeApi {
        return remoteDataSource.buildApi(HomeApi::class.java)
    }

    @Provides
    fun provideUserRepository(
        homeApi: HomeApi,
        limitPreferences: LimitManager
    ): HomeRepository {
        return HomeRepository(homeApi, limitPreferences)
    }
    /*-------------END-HOME-----------------*/

    /*-------------START-INPLAY-----------------*/
    @Singleton
    @Provides
    fun provideInplayApi(
        remoteDataSource: RemoteDataSource,
    ): InPlayApi {
        return remoteDataSource.buildApi(InPlayApi::class.java)
    }

    @Provides
    fun provideInplayRepository(
        api: InPlayApi
    ): InPlayRepository {
        return InPlayRepository(api)
    }
    /*-------------END-INPLAY-----------------*/

    /*-------------START-UGAME-----------------*/
    @Singleton
    @Provides
    fun provideUGameApi(
        remoteDataSource: RemoteDataSource,
    ): UGameApi {
        return remoteDataSource.buildApi(UGameApi::class.java)
    }

    @Provides
    fun provideUGameRepository(
        api: UGameApi
    ): UGameRepository {
        return UGameRepository(api)
    }
    /*-------------END-UGAME-----------------*/
    /*-------------START-CGAME-----------------*/
    @Singleton
    @Provides
    fun provideCGameApi(
        remoteDataSource: RemoteDataSource,
    ): CGameApi {
        return remoteDataSource.buildApi(CGameApi::class.java)
    }

    @Provides
    fun provideCGameRepository(
        api: CGameApi
    ): CGameRepository {
        return CGameRepository(api)
    }
    /*-------------END-CGAME-----------------*/

    /*-------------START-PROFILE-----------------*/
    @Singleton
    @Provides
    fun provideProfileApi(
        remoteDataSource: RemoteDataSource,
    ): ProfileApi {
        return remoteDataSource.buildApi(ProfileApi::class.java)
    }

    @Provides
    fun provideProfileRepository(
        api: ProfileApi
    ): ProfileRepository {
        return ProfileRepository(api)
    }
    /*-------------END-PROFILE-----------------*/

    /*-------------START-LEDGER-----------------*/
    @Singleton
    @Provides
    fun provideLedgerApi(
        remoteDataSource: RemoteDataSource,
    ): LedgerApi {
        return remoteDataSource.buildApi(LedgerApi::class.java)
    }

    @Provides
    fun provideLedgerRepository(
        api: LedgerApi
    ): LedgerRepository {
        return LedgerRepository(api)
    }
    /*-------------END-LEDGER-----------------*/

    /*--------------START-CHANGE-PASS----------------*/
    @Singleton
    @Provides
    fun provideChangePassApi(
        remoteDataSource: RemoteDataSource
    ): ChangePassApi {
        return remoteDataSource.buildApi(ChangePassApi::class.java)
    }

    @Provides
    fun provideChangePassRepository(
        ledgerApi: ChangePassApi
    ): ChangePassRepository {
        return ChangePassRepository(ledgerApi)
    }
    /*--------------END-CHANGE-PASS----------------*/



    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserManager {
        return UserManager(context)
    }

    @Singleton
    @Provides
    fun provideLimitPreferences(@ApplicationContext context: Context): LimitManager {
        return LimitManager(context)
    }
    @Singleton
    @Provides
    fun provideProfilePreferences(@ApplicationContext context: Context): ProfileManager {
        return ProfileManager(context)
    }

}