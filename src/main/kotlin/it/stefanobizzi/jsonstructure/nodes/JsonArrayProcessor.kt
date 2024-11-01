package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonArray
import com.intellij.psi.PsiElement
import it.stefanobizzi.jsonstructure.JsonPsiTreeProcessor
import javax.swing.tree.DefaultMutableTreeNode

class JsonArrayProcessor : JsonElementProcessor {
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
