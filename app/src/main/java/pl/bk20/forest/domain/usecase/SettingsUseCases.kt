package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.SettingsRepository

class SettingsUseCases(
    repository: SettingsRepository
) {

    val getSettings: GetSettings = GetSettingsImpl(repository)
}