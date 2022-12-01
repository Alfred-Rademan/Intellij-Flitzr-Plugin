package com.sbfl_ide.sbfl_analyzer.rerun

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.sbfl_ide.sbfl_analyzer.nextbug.NextBug
import org.apache.lucene.analysis.ar.ArabicAnalyzer
import java.nio.charset.StandardCharsets

class RerunSBFL: AnAction() {


     override fun actionPerformed(e: AnActionEvent) {
         NextBug.pos = -1
         var project = e.project
         if (project != null) {
             var commands: ArrayList<String> = ArrayList()
             // TODO: Chat to Dylan about this
             commands.add("./runner.sh")
             var cmdline  = GeneralCommandLine(commands)
             cmdline.charset = StandardCharsets.UTF_8
             cmdline.setWorkDirectory(project.basePath)
             println(project.basePath)

             var consoleView =  TextConsoleBuilderFactory.getInstance().createBuilder(project).console
             var toolWindow = ToolWindowManager.getInstance(project).getToolWindow("SBFL")!!
             if (toolWindow.contentManager.contentCount != 0) {
                 toolWindow.contentManager.removeAllContents(true)
             }
             var content:Content = toolWindow.contentManager.factory.createContent(consoleView.component, "SBFL", true)
             toolWindow.contentManager.addContent(content)
             toolWindow.show()
             toolWindow.contentManager.setSelectedContent(content)


             var procHandler = OSProcessHandler(cmdline)
             consoleView.attachToProcess(procHandler)
             procHandler.startNotify()
         }

    }

    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }
}