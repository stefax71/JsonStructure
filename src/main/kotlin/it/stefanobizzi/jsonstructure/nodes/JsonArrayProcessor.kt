package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonArray
import com.intellij.psi.PsiElement
import it.stefanobizzi.jsonstructure.tree.JsonPsiTreeProcessor
import javax.swing.tree.DefaultMutableTreeNode

class JsonArrayProcessor : JsonElementProcessor {
    /**
     * Processes a JSON array element and adds its children to the specified tree node.
     *
     * @param node The tree node to which the processed elements will be added.
     * @param element The PSI element representing the JSON array to be processed.
     */
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonArray = element as JsonArray
        jsonArray.valueList.forEachIndexed { index, arrayElement ->
            val childNode = DefaultMutableTreeNode("[$index]")
            node.add(childNode)
            JsonPsiTreeProcessor.processNode(childNode, arrayElement)
        }
        node.userObject = jsonArray
    }
}
