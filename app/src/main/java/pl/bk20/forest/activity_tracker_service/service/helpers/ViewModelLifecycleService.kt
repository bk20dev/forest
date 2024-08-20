package pl.bk20.forest.activity_tracker_service.service.helpers

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras

open class ViewModelLifecycleService : LifecycleService(), ViewModelStoreOwner {

    override val viewModelStore: ViewModelStore by lazy { ViewModelStore() }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }

    val defaultViewModelCreationExtras: CreationExtras
        get() = MutableCreationExtras().also { extras ->
            application?.let { extras[APPLICATION_KEY] = it }
        }
}

inline fun <reified VM : ViewModel> ViewModelLifecycleService.viewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)
): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryProducer,
        extrasProducer = { extrasProducer?.invoke() ?: defaultViewModelCreationExtras },
    )
}