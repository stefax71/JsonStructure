package it.stefanobizzi.jsonstructure.nodes

import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode

fun interface JsonElementProcessor {
    /**
     * Processes a given PSI element and adds it to the specified tree node.
     * This interface is used to implement different classes for different
     * json nodes, to be resolved by a strategy.
     *
     * @param node The tree node to which the processed element will be added.
     * @param element The PSI element to be processed.
     */
    fun process(node: DefaultMutableTreeNode, element: PsiElement)
}