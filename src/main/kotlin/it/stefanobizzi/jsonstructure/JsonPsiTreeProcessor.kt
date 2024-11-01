package it.stefanobizzi.jsonstructure

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import it.stefanobizzi.jsonstructure.nodes.*
import javax.swing.tree.DefaultMutableTreeNode
import kotlin.reflect.KClass

class JsonPsiTreeProcessor(private val psiFile: PsiFile) {
    fun createTreeRoot(): DefaultMutableTreeNode {
        val rootNode = DefaultMutableTreeNode("Root")
        val firstElement = psiFile.firstChild
        if (firstElement is JsonObject || firstElement is JsonArray) {
            processNode(rootNode, firstElement)
        } else {
            println("Errore: Il file JSON non contiene una struttura valida.")
        }
        return rootNode
    }

    companion object {
        private val strategies = listOf<Pair<KClass<*>, JsonElementProcessor>>(
            JsonObject::class to JsonObjectProcessor(),
            JsonArray::class to JsonArrayProcessor(),
            JsonProperty::class to JsonPropertyProcessor(),
            JsonValue::class to JsonValueProcessor()
        )

        private fun getStrategy(element: PsiElement): JsonElementProcessor? {
            return strategies.firstOrNull { (clazz, _) -> clazz.isInstance(element) }?.second
        }

        fun processNode(node: DefaultMutableTreeNode, element: PsiElement) {
            val strategy = getStrategy(element)
            strategy?.process(node, element)
        }
    }
}
