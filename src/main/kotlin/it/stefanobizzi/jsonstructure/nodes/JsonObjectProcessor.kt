package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import it.stefanobizzi.jsonstructure.tree.JsonPsiTreeProcessor
import javax.swing.tree.DefaultMutableTreeNode

class JsonObjectProcessor : JsonElementProcessor {
    /**
     * Processes a JSON object element and adds its properties to the specified tree node.
     *
     * @param node The tree node to which the processed elements will be added.
     * @param element The PSI element representing the JSON object to be processed.
     */
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonObject = element as JsonObject
        node.userObject = jsonObject // Imposta PsiElement come userObject

        for (property in jsonObject.propertyList) {
            val childNode = DefaultMutableTreeNode(property) // Usa PsiElement per ogni nodo
            childNode.userObject = property // Imposta il PsiElement come userObject per il nodo figlio
            node.add(childNode)
            property.value?.let { JsonPsiTreeProcessor.processNode(childNode, it) }
        }
    }
}
