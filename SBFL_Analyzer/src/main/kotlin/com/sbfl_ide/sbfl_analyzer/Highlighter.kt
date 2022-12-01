package com.sbfl_ide.sbfl_analyzer

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorTextField
import java.awt.Color
import java.awt.event.ActionEvent
import java.io.File
import java.util.Scanner
import javax.swing.JFileChooser
import kotlin.math.abs

public class Highlighter: AnAction() {

    var SBFLVals = SBFLInfo()

    var average = 0.0;
    override fun actionPerformed(e: AnActionEvent) {
        SBFLVals.parseSBFL(e)
        var editor= e.getRequiredData(CommonDataKeys.EDITOR)
        editor.markupModel.removeAllHighlighters()
        var vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
        var highlighterInfo = setHighlighterInfo(vFile, e)
        print(vFile!!.nameWithoutExtension)
        for (info in highlighterInfo) {

            var ta = TextAttributes()
            ta.backgroundColor = info.third
            editor.markupModel.addLineHighlighter(info.second,1,ta)

        }
    }
    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }

    fun getColourCode(sbfl_val: Double ): Color {

        var H = 0.0
        if (sbfl_val < 100) {
            H = if (sbfl_val <= average) {
                0.15
            } else {
                0.1
            }
        }
        val S = 0.8
        val B = 0.5
        val Col = Color.getHSBColor(H.toFloat(),S.toFloat(),B.toFloat())
        return Col
    }

    fun setHighlighterInfo(vFile: VirtualFile?, e: AnActionEvent): ArrayList<Triple<Double,Int,Color>> {


        SBFLVals.parseSBFL(e)
        average = 0.0
        var count = 0
        var entries = SBFLVals.entries

        val highlighterInfo = ArrayList<Triple<Double,Int,Color>>()
        for(entry in entries) {
            if (entry.sbflVal < 100 && entry.sbflVal > 0){
                average += entry.sbflVal
                count ++
            }
        }
        average /= count

        for(entry in entries) {

            if (entry.sbflVal != 0.0 && entry.file == vFile!!.nameWithoutExtension) {
                var infoAdd = Triple(entry.sbflVal,entry.line,getColourCode(entry.sbflVal))
                highlighterInfo.add(infoAdd)
            }


        }
        return highlighterInfo
    }


}