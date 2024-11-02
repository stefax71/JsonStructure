package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonArray
import it.stefanobizzi.jsonstructure.tree.JsonPsiTreeProcessor

class JsonPropertyProcessor : JsonElementProcessor {
    /**
     * Processes a JSON property element and adds it to the specified tree node.
     *
     * @param node The tree node to which the processed element will be added.
     * @param element The PSI element representing the JSON property to be processed.
     */
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonProperty = element as JsonProperty
        node.userObject = jsonProperty // Imposta il PsiElement come userObject

        val value = jsonProperty.value
        if (value != null) {
            val propertyNode = DefaultMutableTreeNode(value)
            propertyNode.userObject = value // Imposta PsiElement per il valore
            node.add(propertyNode)

            if (value is JsonObject || value is JsonArray) {
                JsonPsiTreeProcessor.processNode(propertyNode, value)
            }
        }
    }
}