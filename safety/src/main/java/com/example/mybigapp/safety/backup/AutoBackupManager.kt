package com.example.mybigapp.safety.backup

import java.io.File

/**
 * Creates .backup copies of generated files before overwrites.
 * @param frequencyMinutes how often to back up
 */
class AutoBackupManager(private val frequencyMinutes: Int) {

    /**
     * Registers a file or directory to be backed up automatically.
     * @return true if registration succeeded
     */
    fun registerBackup(source: File, backupDir: File): Boolean {
        // TODO: schedule periodic copy tasks
        return true
    }
}
