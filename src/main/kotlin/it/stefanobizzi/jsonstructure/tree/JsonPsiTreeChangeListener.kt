package it.stefanobizzi.jsonstructure.tree

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.PsiTreeChangeListener

class JsonPsiTreeChangeListener(
    private val psiFile: PsiFile?,
    private val rebuildTreeCallback: () -> Unit
) : PsiTreeChangeListener {
    override fun childrenChanged(event: PsiTreeChangeEvent) {
        if (event.file == psiFile) {
            rebuildTreeCallback()
        }
    }

    override fun beforeChildAddition(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun beforeChildRemoval(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun beforeChildReplacement(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun beforeChildMovement(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun beforeChildrenChange(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun beforePropertyChange(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun childAdded(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }

    override fun propertyChanged(event: PsiTreeChangeEvent) {
        // (SONAR) Can be left empty
    }
}