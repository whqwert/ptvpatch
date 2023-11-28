package io.github.whqwert.ptvpatch

import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker

private lateinit var module: Main

class Main(base: XposedInterface, param: ModuleLoadedParam) : XposedModule(base, param) {
    init {
        module = this
    }


    @XposedHooker
    class HasAccessHooker : XposedInterface.Hooker {
        companion object {
            @JvmStatic
            @BeforeInvocation
            fun beforeInvocation(callback: BeforeHookCallback): HasAccessHooker {
                callback.returnAndSkip(true)
                return HasAccessHooker()
            }
        }
    }

    @XposedHooker
    class PingHooker : XposedInterface.Hooker {
        companion object {
            @JvmStatic
            @BeforeInvocation
            fun beforeInvocation(callback: BeforeHookCallback): PingHooker {
                callback.returnAndSkip(null)
                return PingHooker()
            }
        }
    }


    override fun onPackageLoaded(param: PackageLoadedParam) {
        super.onPackageLoaded(param)
        if (param.packageName != "tv.orange") return

        module.hook(
            param.classLoader
                .loadClass("tv.orange.core.models.flag.objects.LicenseType")
                .getDeclaredMethod("hasAccess"),
            HasAccessHooker::class.java
        )

        // optional
        module.hook(
            param.classLoader
                .loadClass("tv.orange.features.tracking.Tracking")
                .getDeclaredMethod("ping"),
            PingHooker::class.java
        )
    }
}
