package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import it.stefanobizzi.jsonstructure.JsonPsiTreeProcessor
import javax.swing.tree.DefaultMutableTreeNode

class JsonObjectProcessor : JsonElementProcessor {
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val jsonObject = element as JsonObject

        // Modifica `userObject` per visualizzare correttamente il nome della proprietà
        jsonObject.parent?.let {
            if (it is JsonProperty) {
                node.userObject = it.name // Usa il nome della proprietà se disponibile
            }
        }

        for (property in jsonObject.propertyList) {
            val childNode = DefaultMutableTreeNode(property.name)
            node.add(childNode)
            property.value?.let { JsonPsiTreeProcessor.processNode(childNode, it) }
        }
    }
}
