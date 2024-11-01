package it.stefanobizzi.jsonstructure.nodes

import com.intellij.psi.PsiElement
import javax.swing.tree.DefaultMutableTreeNode

interface JsonElementProcessor {
    fun process(node: DefaultMutableTreeNode, element: PsiElement)
}