package com.hva.hva_bewear.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hva.hva_bewear.data.avatar_type.network.AvatarTypeDao
import com.hva.hva_bewear.data.avatar_type.network.response.AvatarTypeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [AvatarTypeResponse::class], version = 1, exportSchema = false)
abstract class BewearRoomDatabase : RoomDatabase() {

    abstract fun avatarTypeDao(): AvatarTypeDao

    companion object {
        private const val DATABASE_NAME = "HVA_BEWEAR_DATABASE"

        @Volatile
        private var INSTANCE: BewearRoomDatabase? = null

        fun getDatabase(context: Context): BewearRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(BewearRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BewearRoomDatabase::class.java, DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    INSTANCE?.let {database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            database.avatarTypeDao().insert(AvatarTypeResponse("m"))
                                        }
                                    }
                                }
                            })
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}