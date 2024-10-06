package pl.bk20.forest.activity_tracker_service.domain.model

data class StepCountActivity(
    val id: Long,
    val timeRange: ActivityTimeRange,
    val stepCount: Int,
)