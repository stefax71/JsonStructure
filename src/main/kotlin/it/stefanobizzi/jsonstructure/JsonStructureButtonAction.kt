package it.stefanobizzi.jsonstructure

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.annotations.NotNull;

class JsonStructureButtonAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // Empty on purpose
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val isJsonFile = file != null && "json".equals(file.extension, ignoreCase = true)
        e.presentation.isEnabledAndVisible = isJsonFile
    }

    @NotNull
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}
