package com.sbfl_ide.sbfl_analyzer.failed_tests

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataConstants
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.messages.MessageDialog
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.sbfl_ide.sbfl_analyzer.SBFLEntry
import com.sbfl_ide.sbfl_analyzer.nextbug.NextBug
import java.io.File

class FindFailedTests: AnAction() {
    companion object {
        var lookupPositions = ArrayList<SBFLEntry>()
    }

    override fun actionPerformed(e: AnActionEvent) {
        NextBug.Tpos = -1
        var project = e.project;
        //var elementsToFailed: Map<String, ArrayList<String>> = hashMapOf();
        println("Right Click Menu Works!!!")
        var caret = e.getData(CommonDataKeys.CARET)
        if (caret != null) {
            var lineNum = caret.logicalPosition.line + 1
            //@Alfred this gets the current file open in the editor
            var vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
            var editor = e.dataContext.getData(CommonDataKeys.EDITOR)


            if (vFile != null) {

                var elements = vFile.path.split("/main/java/")[1].split("/")
                var classFilterString = elements[0] + "$" + elements[1].split(".")[0];
                println(project?.basePath + "/app/build/sfl/txt/spectra.csv")
                var spectraBR = File(project?.basePath + "/app/build/sfl/txt/spectra.csv").bufferedReader()
                var line = spectraBR.readLine();

                var index = -1;
                var found = false;
                while (line != null) {
                    if (line.startsWith(classFilterString)) {
                        if (line.split(":")[1].toInt() == lineNum) {
                            // We have found the correct index
                            found = true
                            break
                        }
                    }
                    line = spectraBR.readLine()
                    index++
                }
                println(index)

                if (!found) {
                    return
                }
                var testList = File(project?.basePath + "/app/build/sfl/txt/tests.csv").readLines()
                var matrixBr = File(project?.basePath + "/app/build/sfl/txt/matrix.txt").bufferedReader()
                line = matrixBr.readLine()
                var failedCases: ArrayList<String> = ArrayList()
                var failedMessage = " "
                var i = 0;
                while (line != null) {
                    var row = line.split(" ");
                    println(row)
                    if (row[index] == "1" && row.last() == "-") {

                        var failedLine  = testList[i+1].split(",")[0] +"\n"
                        failedCases.add(failedLine)
                        failedMessage += failedLine
                    }
                    i++
                    line = matrixBr.readLine()
                }
                if (failedCases.size > 0) {
                    if (project != null) {
                        println(testList)
                        var fileStr = failedCases.get(0).split("#")[0].replace(".","/")

                        var fileEditorManager = FileEditorManager.getInstance(project)
                        var failedTest = LocalFileSystem.getInstance()
                            .findFileByPath(project.basePath + "/app/src/test/java/" + fileStr + ".java")
                        if (failedTest == null) {
                            println("NULLFILE")
                            println(project.basePath + "app/src/test/java/temp_gradle/AppTest.java")
                        } else {
                            editor?.markupModel?.removeAllHighlighters()
                            fileEditorManager.openTextEditor(OpenFileDescriptor(project, failedTest), true)
                        }
                        var failedTestPosistions = ArrayList<SBFLEntry>()
                        for (fc in failedCases) {
                            var fn = project.basePath + "/app/src/test/java/" + fc.split("#")[0].replace(".","/") + ".java"
                            println(fn)
                            var testName = fc.split(",")[0].split("#")[1]
                            testName = testName.substring(0, testName.length -1)
                            var lines = File(fn).readLines()
                            var i = 0
                            while(i < lines.size) {
                                println(lines[i])
                                if (lines[i].contains(testName)) {
                                    println("found it")
                                    break
                                }
                                i++
                            }
                            if (i != lines.size) {
                              failedTestPosistions.add(SBFLEntry(fn,1.00,i))
                            }
                        }
                        lookupPositions = failedTestPosistions
                    }
                }

                Messages.showMessageDialog(failedMessage, classFilterString + ":" + lineNum + " Failed these test cases",null)
            }
        }
    }

}