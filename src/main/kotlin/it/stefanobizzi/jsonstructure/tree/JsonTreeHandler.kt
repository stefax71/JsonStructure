package it.stefanobizzi.jsonstructure.tree

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.treeStructure.Tree
import it.stefanobizzi.jsonstructure.JsonTreeCellRenderer
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

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
        val expandedPaths = saveExpandedPaths()
        val newRootNode = treeProcessor.createTreeRoot()
        (tree.model as? DefaultTreeModel)?.setRoot(newRootNode)
        (tree.model as? DefaultTreeModel)?.reload()
        restoreExpandedPaths(expandedPaths)
    }

    fun getComponent(): Tree {
        return tree
    }

    private fun generateNodePath(node: DefaultMutableTreeNode): String {
        val path = mutableListOf<String>()
        var currentNode: DefaultMutableTreeNode? = node

        while (currentNode != null) {
            val userObject = currentNode.userObject
            val identifier = when (userObject) {
                is JsonProperty -> userObject.name // Usa il nome della proprietà come identificativo
                is JsonObject -> "Object"
                is JsonArray -> "Array"
                else -> userObject.toString()
            }
            path.add(0, identifier) // Aggiungi all'inizio per costruire il percorso dal nodo radice
            currentNode = currentNode.parent as? DefaultMutableTreeNode
        }

        return path.joinToString("/") // Percorso unico separato da /
    }

    private fun saveExpandedPaths(): Set<String> {
        val expandedPaths = mutableSetOf<String>()
        for (i in 0 until tree.rowCount) {
            val path = tree.getPathForRow(i) ?: continue
            if (tree.isExpanded(path)) {
                val node = path.lastPathComponent as? DefaultMutableTreeNode ?: continue
                expandedPaths.add(generateNodePath(node)) // Salva il percorso unico
            }
        }
        return expandedPaths
    }


    private fun restoreExpandedPaths(expandedPaths: Set<String>) {
        val rootNode = tree.model.root as? DefaultMutableTreeNode ?: return
        expandMatchingNodes(expandedPaths, rootNode)
    }

    private fun expandMatchingNodes(expandedPaths: Set<String>, node: DefaultMutableTreeNode) {
        val nodePath = generateNodePath(node)
        if (nodePath in expandedPaths) {
            tree.expandPath(TreePath(node.path))
        }
        for (i in 0 until node.childCount) {
            expandMatchingNodes(expandedPaths, node.getChildAt(i) as DefaultMutableTreeNode)
        }
    }

}