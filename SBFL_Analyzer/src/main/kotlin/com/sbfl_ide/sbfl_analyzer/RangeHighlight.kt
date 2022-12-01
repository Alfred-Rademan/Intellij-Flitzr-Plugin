package com.sbfl_ide.sbfl_analyzer

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.markup.TextAttributes
import java.util.Objects
import javax.swing.JOptionPane
import javax.swing.JTextField
//import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class RangeHighlight : AnAction() {

    private val highlighter = Highlighter()
    override fun actionPerformed(e: AnActionEvent) {
        var low = JTextField()
        var high = JTextField()
        var objects = arrayOf("LowerBound:", low, "UpperBound", high)
        var Inputs = JOptionPane.showConfirmDialog(null, objects, "Enter boundaries", JOptionPane.OK_CANCEL_OPTION)
        if (Inputs == JOptionPane.OK_OPTION) {
            var lowBound = low.text.toDouble()
            var highBound = high.text.toDouble()
            System.out.println(lowBound.toString() + " " + highBound.toString())
            var editor = e.getRequiredData(CommonDataKeys.EDITOR);
            editor.markupModel.removeAllHighlighters()
            var vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
            var highlighterInfo = highlighter.setHighlighterInfo(vFile,e )

            for (info in highlighterInfo) {
                System.out.println(info.first)
                if (info.first in lowBound..highBound) {
                    var ta = TextAttributes()
                    ta.backgroundColor = info.third
                    editor.markupModel.addLineHighlighter(info.second, 1, ta)
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }
}