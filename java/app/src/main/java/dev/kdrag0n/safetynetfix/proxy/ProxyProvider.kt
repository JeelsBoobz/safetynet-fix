package dev.kdrag0n.safetynetfix.proxy

import android.os.Build
import dev.kdrag0n.safetynetfix.SecurityHooks
import dev.kdrag0n.safetynetfix.logDebug
import java.security.Provider
import kotlin.concurrent.thread

// This is mostly just a pass-through provider that exists to change the provider's ClassLoader.
// This works because Service looks up the class by name from the *provider* ClassLoader, not
// necessarily the bootstrap one.
class ProxyProvider(
    orig: Provider,
) : Provider(orig.name, orig.version, orig.info) {
    init {
        logDebug("Init proxy provider - wrapping $orig")

        putAll(orig)
        this["KeyStore.${SecurityHooks.PROVIDER_NAME}"] = ProxyKeyStoreSpi::class.java.name
    }

    override fun getService(type: String?, algorithm: String?): Service? {
        logDebug("Provider: get service - type=$type algorithm=$algorithm")
        if (type == "KeyStore" && Build.HOST != "xiaomi.eu") {

            val patchedProduct       = /* ro.product.name                 */ "exouhl_00617"
            val patchedDevice        = /* ro.product.device               */ "htc_exouhl"
            val patchedModel         = /* ro.product.model                */ "EXODUS 1"
            val patchedBrand         = /* ro.product.brand                */ "htc"
            val patchedManufacturer  = /* ro.product.manufacturer         */ "HTC"
            val patchedSecurityPatch = /* ro.build.version.security_patch */ "2020-03-01"
            val patchedFirstApiLevel = /* ro.product.first_api_level      */ 28
            val patchedSDKVersion    = /* ro.build.version.sdk            */ 28
            val patchedFingerprint   = /* ro.build.fingerprint            */ "htc/exouhl_00617/htc_exouhl:9/PQ2A.190205.003/1109191.2:user/release-keys"
            val patchedBuildID       = /* ro.build.id                     */ "PQ2A.190205.003"

            logDebug("Patch PRODUCT prop. Set it to: $patchedProduct")
            Build::class.java.getDeclaredField("PRODUCT").let { field ->
                field.isAccessible = true
                field.set(null, patchedProduct)
            }
            logDebug("Patch DEVICE prop. Set it to: $patchedDevice")
            Build::class.java.getDeclaredField("DEVICE").let { field ->
                field.isAccessible = true
                field.set(null, patchedDevice)
            }
            logDebug("Patch MODEL prop. Set it to: $patchedModel")
            Build::class.java.getDeclaredField("MODEL").let { field ->
                field.isAccessible = true
                field.set(null, patchedModel)
            }
            logDebug("Patch BRAND prop. Set it to: $patchedBrand")
            Build::class.java.getDeclaredField("BRAND").let { field ->
                field.isAccessible = true
                field.set(null, patchedBrand)
            }
            logDebug("Patch MANUFACTURER prop. Set it to: $patchedManufacturer")
            Build::class.java.getDeclaredField("MANUFACTURER").let { field ->
                field.isAccessible = true
                field.set(null, patchedManufacturer)
            }
            logDebug("Patch SECURITY_PATCH prop. Set it to: $patchedSecurityPatch")
            Build.VERSION::class.java.getDeclaredField("SECURITY_PATCH").let { field ->
                field.isAccessible = true
                field.set(null, patchedSecurityPatch)
            }
            logDebug("Patch FIRST_API_LEVEL prop. Set it to: $patchedFirstApiLevel")
            Build.VERSION::class.java.getDeclaredField("FIRST_API_LEVEL").let { field ->
                field.isAccessible = true
                field.set(null, patchedFirstApiLevel)
            }
            logDebug("Patch SDK_VERSION prop. Set it to: $patchedSDKVersion")
            Build.VERSION::class.java.getDeclaredField("SDK_INT").let { field ->
                field.isAccessible = true
                field.set(null, patchedSDKVersion)
            }
            logDebug("Patch FINGERPRINT prop. Set it to: $patchedFingerprint")
            Build::class.java.getDeclaredField("FINGERPRINT").let { field ->
                field.isAccessible = true
                field.set(null, patchedFingerprint)
            }
            logDebug("Patch BUILD_ID prop. Set it to: $patchedBuildID")
            Build::class.java.getDeclaredField("ID").let { field ->
                field.isAccessible = true
                field.set(null, patchedBuildID)
            }
        }
        return super.getService(type, algorithm)
    }

    override fun getServices(): MutableSet<Service>? {
        logDebug("Get services")
        return super.getServices()
    }
}
