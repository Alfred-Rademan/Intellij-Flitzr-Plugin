package com.sbfl_ide.sbfl_analyzer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/*
The Visualisations Class is purely for visual purposes in the SBFL dropdown menu
It consists of two functions, one of which is anPerformed and an update which
checks if there is a file open in the editor so that the Visualisations option
can be clicked from the dropdown
 */

class Visualisations: AnAction() {
     override fun actionPerformed(e: AnActionEvent) {
    }

    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }
}