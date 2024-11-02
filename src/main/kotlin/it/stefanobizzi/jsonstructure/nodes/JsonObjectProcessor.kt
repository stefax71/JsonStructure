package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import it.stefanobizzi.jsonstructure.JsonPsiTreeProcessor
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

        // If the parent of the JSON object is a JSON property, use the property's name as the user object of the node.
        jsonObject.parent?.let {
            if (it is JsonProperty) {
                node.userObject = it.name // Use the name of the property if available
            }
        }

        // Iterate through the properties of the JSON object and add them as child nodes.
        for (property in jsonObject.propertyList) {
            val childNode = DefaultMutableTreeNode(property.name)
            node.add(childNode)
            property.value?.let { JsonPsiTreeProcessor.processNode(childNode, it) }
        }
    }
}
