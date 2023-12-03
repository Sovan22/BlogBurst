package com.demomiru.blogburst

import android.app.Application
import androidx.room.Room
import com.demomiru.blogburst.data.BlogRepoImpl
import com.demomiru.blogburst.data.BlogRepository
import com.demomiru.blogburst.data.OfflineBlogDatabase
import com.google.gson.Gson
import com.lagradost.nicehttp.Requests
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class BlogApp : Application()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    @Singleton
//    fun provideRequestClient() : Requests{
//        return Requests()
//    }
//
//    @Provides
//    @Singleton
//    fun provideJsonExtractor() : Gson{
//        return Gson()
//    }

    @Provides
    @Singleton
    fun provideBlogDatabase(app: Application): OfflineBlogDatabase{
        return Room.databaseBuilder(
            app,
            OfflineBlogDatabase::class.java,
            "blog_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideBlogRepo(db: OfflineBlogDatabase) : BlogRepository{
        return BlogRepoImpl(db.dao)
    }
}