package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode

class JsonValueProcessor : JsonElementProcessor {
    /**
     * Processes a PSI element and sets the user object of the specified tree node.
     *
     * @param node The tree node to which the processed element will be added.
     * @param element The PSI element to be processed.
     */
    override fun process(node: DefaultMutableTreeNode, element: PsiElement) {
        val parentProperty = element.parent as? JsonProperty
        if (parentProperty != null) {
            node.userObject = "${parentProperty.name}: ${element.text}"
        } else {
            node.userObject = element.text
        }
    }
}
