package pl.bk20.forest.settings.domain.usecase

import pl.bk20.forest.settings.domain.repository.SettingsRepository

class SettingsUseCases(
    repository: SettingsRepository
) {

    val getSettings: GetSettings = GetSettingsImpl(repository)
}