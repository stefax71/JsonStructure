package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonArray
import it.stefanobizzi.jsonstructure.JsonPsiTreeProcessor

class JsonPropertyProcessor : JsonElementProcessor {
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonProperty = element as JsonProperty
        val value = jsonProperty.value

        val propertyNode = DefaultMutableTreeNode("${jsonProperty.name}: ${value?.text ?: "null"}")
        node.add(propertyNode)

        if (value is JsonObject || value is JsonArray) {
            JsonPsiTreeProcessor.processNode(propertyNode, value)
        }
    }
}