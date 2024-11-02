package it.stefanobizzi.jsonstructure.tree

import com.intellij.psi.PsiElement

fun interface TreeNavigationCallback {
    fun navigateTo(element:PsiElement)
}