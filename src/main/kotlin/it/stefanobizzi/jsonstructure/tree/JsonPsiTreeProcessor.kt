package it.stefanobizzi.jsonstructure.tree

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
    /**
     * Creates the root node of the tree structure representing the JSON file.
     *
     * @return The root node of the tree.
     */

    fun createTreeRoot(): DefaultMutableTreeNode {
        val rootNode = DefaultMutableTreeNode("Root")
        val firstElement = psiFile.firstChild
        if (firstElement is JsonObject || firstElement is JsonArray) {
            processNode(rootNode, firstElement)
        } else {
            println("Error: Invalid JSON structure.")
        }
        return rootNode
    }

    companion object {
        // List of strategies for processing different types of JSON elements.
        // Each pair consists of a KClass representing the JSON element type and a corresponding JsonElementProcessor.
        private val strategies = listOf<Pair<KClass<*>, JsonElementProcessor>>(
            JsonObject::class to JsonObjectProcessor(),
            JsonArray::class to JsonArrayProcessor(),
            JsonProperty::class to JsonPropertyProcessor(),
            JsonValue::class to JsonValueProcessor()
        )

        /**
         * Retrieves the appropriate JsonElementProcessor for the given PSI element.
         *
         * @param element The PSI element to be processed.
         * @return The corresponding JsonElementProcessor, or null if no matching processor is found.
         */
        private fun getStrategy(element: PsiElement): JsonElementProcessor? {
            return strategies.firstOrNull { (clazz, _) -> clazz.isInstance(element) }?.second
        }

        /**
         * Processes a given PSI element and adds it to the specified tree node.
         *
         * @param node The tree node to which the processed element will be added.
         * @param element The PSI element to be processed.
         */
        fun processNode(node: DefaultMutableTreeNode, element: PsiElement) {
            val strategy = getStrategy(element)
            strategy?.process(node, element)
        }
    }
}
