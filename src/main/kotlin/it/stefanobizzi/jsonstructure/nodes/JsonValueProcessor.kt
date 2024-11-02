package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode

class JsonValueProcessor : JsonElementProcessor {
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val parentProperty = element.parent as? JsonProperty
        if (parentProperty != null) {
            node.userObject = "${parentProperty.name}: ${element.text}"
        } else {
            node.userObject = element.text
        }
    }
}
