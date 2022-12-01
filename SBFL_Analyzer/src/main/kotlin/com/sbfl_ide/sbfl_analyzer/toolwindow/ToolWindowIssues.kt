package com.sbfl_ide.sbfl_analyzer.toolwindow

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.sbfl_ide.sbfl_analyzer.SBFLInfo


class ToolWindowIssues: AnAction() {
    companion object {
        var filtered = false
        var SBFLVals: SBFLInfo = SBFLInfo()

        fun changeFiltered(b: Boolean, e: AnActionEvent) {
            filtered = b
            if (filtered) {
                SBFLVals.parseSBFL(e)


            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        var project = e.project

        if (project != null) {
            ProjectView.getInstance(project).refresh();
        }
        changeFiltered(!filtered, e)

    }





}