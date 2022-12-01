package com.sbfl_ide.sbfl_analyzer

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.markup.TextAttributes
import com.sbfl_ide.sbfl_analyzer.Highlighter

class HighlightCritical: AnAction() {
    private val highlighter = Highlighter()
    override fun actionPerformed(e: AnActionEvent) {
        var editor= e.getRequiredData(CommonDataKeys.EDITOR);
        editor.markupModel.removeAllHighlighters()
        var vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
        var highlighterInfo = highlighter.setHighlighterInfo(vFile, e)

        for (info in highlighterInfo) {
            if (info.first >= 100) {
                var ta = TextAttributes()
                ta.backgroundColor = info.third
                editor.markupModel.addLineHighlighter(info.second,1,ta)
            }


        }
    }
    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }

}