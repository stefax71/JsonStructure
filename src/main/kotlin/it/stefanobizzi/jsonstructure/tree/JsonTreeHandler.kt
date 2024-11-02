package it.stefanobizzi.jsonstructure.tree

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.treeStructure.Tree
import it.stefanobizzi.jsonstructure.JsonTreeCellRenderer
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class JsonTreeHandler(
    private val project: Project,
    private val file: PsiFile,
    private val treeProcessor: JsonPsiTreeProcessor,
    private val navigationCallback: TreeNavigationCallback) {

    val tree: Tree = Tree(DefaultMutableTreeNode("Root"))

    init {
        setupTree()
        rebuildTree()
    }

    private fun setupTree() {
        tree.cellRenderer = JsonTreeCellRenderer()
    }

    fun addMouseListener() {
        tree.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent) {
                if (e.clickCount == 2) {
                    val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode ?: return
                    val psiElement = selectedNode.userObject as? PsiElement ?: return
                    navigationCallback.navigateTo(psiElement)
                }
            }
        })
    }

    fun rebuildTree() {
        val newRootNode = treeProcessor.createTreeRoot()
        (tree.model as? DefaultTreeModel)?.setRoot(newRootNode)
        (tree.model as? DefaultTreeModel)?.reload()
    }

    fun getComponent(): Tree {
        return tree
    }

}