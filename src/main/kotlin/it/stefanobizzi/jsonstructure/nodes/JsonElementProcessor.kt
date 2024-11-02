package it.stefanobizzi.jsonstructure.nodes

import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode

fun interface JsonElementProcessor {
    fun process(node: DefaultMutableTreeNode, element: PsiElement)
}