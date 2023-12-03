package com.demomiru.blogburst.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

//data class Response(
//    val blogs: ArrayList<Blog>
//)

data class Blog (
    val id: String,
    val date: String? = null,
    val date_gmt: String? = null,
    val link: String? = null,
    val title: Rendered,
    val excerpt: Rendered,
    val content : Rendered,

){
    data class Rendered(
        val rendered: String,
    )
}

@Entity(tableName = "offline_blog")
data class OfflineBlog(
    @PrimaryKey val id: Int,
    val title: String,
    val excerpt: String,
    val content: String,

)

@Dao
interface BlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(offlineBlog: OfflineBlog)

    @Delete
    suspend fun delete(offlineBlog: OfflineBlog)

    @Query("DELETE FROM offline_blog WHERE `id`=:id")
    suspend fun deleteBlog(id:Int)

    @Query("SELECT * FROM offline_blog WHERE `id`=:id")
    suspend fun getOfflineBlog(id: Int): OfflineBlog


    @Query("SELECT * FROM offline_blog")
    fun getOfflineBlogs(): Flow<List<OfflineBlog>>

    @Query("DELETE FROM offline_blog")
    fun deleteAll()
}

@Database(entities = [OfflineBlog::class], version = 1)
abstract class OfflineBlogDatabase : RoomDatabase(){

    abstract val dao : BlogDao

}

