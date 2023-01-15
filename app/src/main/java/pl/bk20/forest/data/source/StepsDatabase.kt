package pl.bk20.forest.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.bk20.forest.data.source.util.Converters
import pl.bk20.forest.domain.model.Steps

@Database(entities = [Steps::class], version = 1)
@TypeConverters(Converters::class)
abstract class StepsDatabase : RoomDatabase() {

    abstract val stepsDao: StepsDao

    companion object {
        const val DATABASE_NAME = "steps_database"
    }
}

