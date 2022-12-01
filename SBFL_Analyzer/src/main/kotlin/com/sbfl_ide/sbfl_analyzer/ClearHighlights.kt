package com.sbfl_ide.sbfl_analyzer

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class ClearHighlights: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        var editor= e.getRequiredData(CommonDataKeys.EDITOR);
        editor.markupModel.removeAllHighlighters()
    }
}