package com.example.mybigapp.validator.execution.dependency.permission

/**
 * Validates that AndroidManifest.xml declares the permissions
 * required by the code (e.g., CAMERA, INTERNET).
 */
class PermissionValidator {

    /**
     * @return missing Android permissions, e.g. "android.permission.CAMERA"
     */
    fun findMissingPermissions(
        manifestXml: String,
        usedPermissions: List<String>
    ): List<String> {
        // TODO: parse manifestXml and compare to usedPermissions
        return emptyList()
    }
}
