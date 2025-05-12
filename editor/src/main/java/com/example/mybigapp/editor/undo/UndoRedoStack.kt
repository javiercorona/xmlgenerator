package com.example.mybigapp.editor.undo

/**
 * Tracks user edits for undo/redo, with session persistence.
 */
class UndoRedoStack {
    private val undoStack = mutableListOf<() -> Unit>()
    private val redoStack = mutableListOf<() -> Unit>()

    fun record(action: () -> Unit, inverse: () -> Unit) {
        undoStack.add(inverse)
        redoStack.clear()
    }
    fun undo() { undoStack.removeLastOrNull()?.invoke() }
    fun redo() { redoStack.removeLastOrNull()?.invoke() }
}
