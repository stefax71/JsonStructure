package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonArray
import it.stefanobizzi.jsonstructure.JsonPsiTreeProcessor

class JsonPropertyProcessor : JsonElementProcessor {
    /**
     * Processes a JSON property element and adds it to the specified tree node.
     *
     * @param node The tree node to which the processed element will be added.
     * @param element The PSI element representing the JSON property to be processed.
     */
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonProperty = element as JsonProperty
        val value = jsonProperty.value

        // Create a tree node for the JSON property with its name and value.
        val propertyNode = DefaultMutableTreeNode("${jsonProperty.name}: ${value?.text ?: "null"}")
        node.add(propertyNode)

        // If the value of the JSON property is a JSON object or array, process it recursively.
        if (value is JsonObject || value is JsonArray) {
            JsonPsiTreeProcessor.processNode(propertyNode, value)
        }
    }
}