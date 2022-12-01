package com.sbfl_ide.sbfl_analyzer.nextbug

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.testFramework.waitForProjectLeakingThreads
import com.sbfl_ide.sbfl_analyzer.SBFLEntry
import com.sbfl_ide.sbfl_analyzer.SBFLInfo
import com.sbfl_ide.sbfl_analyzer.failed_tests.FindFailedTests

class PreviousBug: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        var vf = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (vf?.path?.contains("src/test") == true) {
            navigateTests(e)
        } else {
            navigateBugs(e)
        }

    }
    fun navigateTests(e: AnActionEvent) {
        var nonZeros = ArrayList<SBFLEntry>()
        println(FindFailedTests.lookupPositions.size)
        for (ent in FindFailedTests.lookupPositions) {
            if (ent.sbflVal != 0.0) {
                nonZeros.add(ent)
            }
        }
        if (nonZeros.size == 0) {
            Messages.showMessageDialog("There are no failed Tests", "Nothing to show",null)
            return
        }
        if (NextBug.Tpos == -1) {
            NextBug.Tpos = 0
        }
        NextBug.Tpos = (NextBug.Tpos + -1 + 2*nonZeros.size) % nonZeros.size
        var project = e.project
        var editor = e.dataContext.getData(CommonDataKeys.EDITOR)
        if (project != null) {
            var entry = nonZeros[NextBug.Tpos]
            var fileStr = entry.file
            var fileEditorManager = FileEditorManager.getInstance(project)
            var failedTest = LocalFileSystem.getInstance()
                .findFileByPath(fileStr)
            if (failedTest == null) {
                println("NULLFILE")
            } else {
                var fem = fileEditorManager.openTextEditor(OpenFileDescriptor(project, failedTest), true)
                var caret = fem?.caretModel
                var lp = LogicalPosition(entry.line, 0)
                caret?.moveToLogicalPosition(lp)
                fem?.scrollingModel?.scrollToCaret(ScrollType.CENTER)
            }

        }
    }
    fun navigateBugs(e: AnActionEvent) {
        var sbflvals = SBFLInfo()
        sbflvals.parseSBFL(e)
        var nonZeros = ArrayList<SBFLEntry>()
        for (ent in sbflvals.entries) {
            if (ent.sbflVal != 0.0) {
                nonZeros.add(ent)
            }
        }
        if (nonZeros.size == 0) {
            Messages.showMessageDialog("There are no bugs", "Nothing to show",null)
            return
        }
        if (NextBug.pos == -1) {
            NextBug.pos = 0
        }
        NextBug.pos = (NextBug.pos + -1 + 2*nonZeros.size) % nonZeros.size
        var project = e.project
        var editor = e.dataContext.getData(CommonDataKeys.EDITOR)
        if (project != null) {
            var entry = nonZeros[NextBug.pos]
            var fileStr = entry.file
            var fileEditorManager = FileEditorManager.getInstance(project)
            var failedTest = LocalFileSystem.getInstance()
                .findFileByPath(project.basePath + "/app/src/main/java/temp_gradle/" + fileStr + ".java")
            if (failedTest == null) {
                println("NULLFILE")
                println(project.basePath + "app/src/test/java/temp_gradle/AppTest.java")
            } else {
                var fem = fileEditorManager.openTextEditor(OpenFileDescriptor(project, failedTest), true)
                var caret = fem?.caretModel
                var lp = LogicalPosition(entry.line, 0)
                caret?.moveToLogicalPosition(lp)
                fem?.scrollingModel?.scrollToCaret(ScrollType.CENTER)
            }

        }
    }
}