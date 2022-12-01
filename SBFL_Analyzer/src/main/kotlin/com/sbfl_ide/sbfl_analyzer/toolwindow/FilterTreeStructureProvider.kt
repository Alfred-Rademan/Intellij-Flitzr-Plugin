package com.sbfl_ide.sbfl_analyzer.toolwindow

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.sbfl_ide.sbfl_analyzer.SBFLInfo

class FilterTreeStructureProvider: TreeStructureProvider {
    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings?
    ): MutableCollection<AbstractTreeNode<*>> {
        var nodes = ArrayList<AbstractTreeNode<*>>()
        for (n in children) {
            if (n is PsiFileNode) {
                var file = (n as PsiFileNode).virtualFile
                if (file != null && !file.isDirectory && (file.extension == "java") && (ToolWindowIssues.SBFLVals.getMax(ToolWindowIssues.SBFLVals.getClassInfo(file.nameWithoutExtension)) < 100) && ToolWindowIssues.filtered && (!file.nameWithoutExtension.contains("Test")) ) {
                    continue
                }
            }
        nodes.add(n)
        }
        return nodes
    }
}