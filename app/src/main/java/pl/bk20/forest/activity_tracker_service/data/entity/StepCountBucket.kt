package pl.bk20.forest.activity_tracker_service.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_step_count")
data class StepCountBucket(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @Embedded
    val timeBucket: TimeBucket,

    @ColumnInfo(name = "step_count")
    val stepCount: Int
)