package it.stefanobizzi.jsonstructure.nodes

import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonValue
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
        // Imposta il PsiElement stesso come userObject per garantire che sia accessibile al doppio click
        node.userObject = element

        // Imposta una rappresentazione leggibile del valore per il nodo
        if (element is JsonValue) {
            node.userObject = element // Usa il PsiElement stesso
        } else {
            node.userObject = element.text
        }
    }
}
