package pl.bk20.forest.core.domain.usecase

import pl.bk20.forest.core.domain.repository.SettingsRepository

class SettingsUseCases(
    repository: SettingsRepository
) {

    val getSettings: GetSettings = GetSettingsImpl(repository)
}