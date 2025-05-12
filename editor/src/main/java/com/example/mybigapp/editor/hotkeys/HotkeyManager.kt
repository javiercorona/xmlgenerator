package com.example.mybigapp.editor.hotkeys

/**
 * Manages keyboard shortcuts for power users,
 * backed by a configurable JSON keymap.
 */
class HotkeyManager {

    /**
     * @param configJson JSON string defining key→action map
     */
    fun loadKeymap(configJson: String) {
        // TODO: parse JSON into internal map
    }
    fun onKeyPress(keyCode: Int) {
        // TODO: lookup action and invoke
    }
}
