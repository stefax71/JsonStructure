package it.stefanobizzi.jsonstructure

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonValue
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

class JsonTreeCellRenderer : DefaultTreeCellRenderer() {

    /**
     * Returns the component used for drawing the cell. This method is called each time a node in the tree must be drawn.
     *
     * @param tree The JTree that is asking the renderer to draw.
     * @param value The value of the current tree cell to be rendered.
     * @param sel True if the cell is selected.
     * @param expanded True if the cell is expanded.
     * @param leaf True if the cell is a leaf node.
     * @param row The row index of the cell.
     * @param hasFocus True if the cell has focus.
     * @return The component used for drawing the cell.
     */
    override fun getTreeCellRendererComponent(
        tree: JTree, value: Any, sel: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean
    ): java.awt.Component {
        val component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        val node = value as? DefaultMutableTreeNode
        val userObject = node?.userObject

        text = when (userObject) {
            is JsonProperty -> "${userObject.name}: ${userObject.value?.text ?: "null"}"
            is JsonObject -> userObject.parent?.let { (it as? JsonProperty)?.name } ?: "Anonymous Object"
            is JsonArray -> userObject.parent?.let { (it as? JsonProperty)?.name } ?: "Anonymous Array"
            is JsonValue -> {
                val parentProperty = userObject.parent as? JsonProperty
                if (parentProperty != null) {
                    "${parentProperty.name}: ${userObject.text}" //
                } else {
                    userObject.text
                }
            }
            else -> userObject?.toString() ?: ""
        }

        return component
    }
}