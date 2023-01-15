package pl.bk20.forest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "step")
data class Steps(
    @PrimaryKey(autoGenerate = false) val date: LocalDate,
    var count: Int
)
