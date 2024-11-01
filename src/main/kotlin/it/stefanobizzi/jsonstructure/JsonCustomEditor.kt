package it.stefanobizzi.jsonstructure

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.PsiTreeChangeListener
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.JBUI
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI
import javax.swing.tree.DefaultTreeModel

private const val SHOW_JSON_STRUCTURE = "Show JSON Structure"

class JsonCustomEditor(project: Project, private val file: VirtualFile) : FileEditor, Disposable {
    private val mainPanel = JPanel(BorderLayout())
    private var toolbarPanel = JPanel(BorderLayout())
    private var tree = Tree()
    private val propertyChangeSupport = PropertyChangeSupport(this)

    private lateinit var editor: Editor

    init {
        ApplicationManager.getApplication().invokeLater {
            if (isJsonFile()) {
                setupDefaultJsonEditor(project)
                setupUI()
            }
        }
    }

    private fun isJsonFile() = FileTypeManager.getInstance().getFileTypeByFile(file).defaultExtension == "json"

    private fun setupDefaultJsonEditor(project: Project) {
        val psiFile = PsiManager.getInstance(project).findFile(file)
        editor = EditorFactory.getInstance().createEditor(
            FileDocumentManager.getInstance().getDocument(psiFile!!.virtualFile)!!,
            project,
            file,
            false
        )

        val treeProcessor = JsonPsiTreeProcessor(psiFile)
        editor.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                rebuildTree(project, treeProcessor)
            }
        })

        val psiTreeChangeListener = createPsiChangeListener(psiFile, project, treeProcessor)

        // Aggiungi il listener alla gestione PSI
        PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener, this)


        val treeRoot = treeProcessor.createTreeRoot()
        tree = Tree(treeRoot)

    }

    private fun createPsiChangeListener(
        psiFile: PsiFile?,
        project: Project,
        treeProcessor: JsonPsiTreeProcessor
    ) = object : PsiTreeChangeListener {
        override fun childrenChanged(event: PsiTreeChangeEvent) {
            if (event.file == psiFile) {
                rebuildTree(project, treeProcessor)
            }
        }

        override fun beforeChildAddition(event: PsiTreeChangeEvent) {}
        override fun beforeChildRemoval(event: PsiTreeChangeEvent) {}
        override fun beforeChildReplacement(event: PsiTreeChangeEvent) {}
        override fun beforeChildMovement(event: PsiTreeChangeEvent) {}
        override fun beforeChildrenChange(event: PsiTreeChangeEvent) {}
        override fun beforePropertyChange(event: PsiTreeChangeEvent) {}
        override fun childAdded(event: PsiTreeChangeEvent) {}
        override fun childRemoved(event: PsiTreeChangeEvent) {}
        override fun childReplaced(event: PsiTreeChangeEvent) {}
        override fun childMoved(event: PsiTreeChangeEvent) {}
        override fun propertyChanged(event: PsiTreeChangeEvent) {}
    }

    private fun rebuildTree(project: Project, treeProcessor: JsonPsiTreeProcessor) {
        // Ottieni di nuovo il PsiFile per assicurarsi che rifletta le modifiche attuali
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return

        // Ricrea la radice dell'albero con il contenuto aggiornato
        val newRootNode = treeProcessor.createTreeRoot()

        // Imposta la nuova radice sull'albero esistente
        (tree.model as? DefaultTreeModel)?.setRoot(newRootNode)
        (tree.model as? DefaultTreeModel)?.reload()
    }

    private fun setupUI() {
        val toggleButton = createToggleStructureButton()
        val treePanel = JBScrollPane(tree)
        val splitPane = createSplitPane(treePanel)

        toggleButton.addActionListener {
            splitPane.leftComponent = if (splitPane.leftComponent == null) treePanel else null
            mainPanel.revalidate()
            mainPanel.repaint()
        }

        toolbarPanel = createToolbar(toggleButton)
        mainPanel.add(toolbarPanel, BorderLayout.NORTH)
        mainPanel.add(splitPane, BorderLayout.CENTER)
    }

    private fun createToggleStructureButton(): JButton {
        val toggleButton = JButton(SHOW_JSON_STRUCTURE, AllIcons.Toolwindows.ToolWindowStructure)
        toggleButton.toolTipText = SHOW_JSON_STRUCTURE
        toggleButton.isContentAreaFilled = false
        return toggleButton
    }

    private fun createToolbar(vararg buttons: JButton): JPanel {
        val toolbarPanel = JPanel(BorderLayout())
        toolbarPanel.border = JBUI.Borders.empty(5)
        val buttonContainer = Box.createHorizontalBox()
        buttons.forEach { buttonContainer.add(it) }
        buttonContainer.add(Box.createHorizontalGlue())
        toolbarPanel.add(buttonContainer, BorderLayout.WEST)
        return toolbarPanel
    }


    private fun createSplitPane(treePanel: JBScrollPane): JSplitPane {
        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, editor.component)
        splitPane.dividerLocation = 200
        splitPane.resizeWeight = 0.3
        splitPane.setUI(BasicSplitPaneUI())
        splitPane.border = JBUI.Borders.empty()
        splitPane.background = JBColor.PanelBackground
        splitPane.dividerSize = 3
        return splitPane
    }

    override fun getComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent? = toolbarPanel

    override fun getName(): String = "JSON Structure Editor"

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = file.isValid

    override fun dispose() {
        if (::editor.isInitialized) {
            EditorFactory.getInstance().releaseEditor(editor)
        }
    }

    override fun <T : Any?> getUserData(key: Key<T>): T? = null

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {}

    override fun getFile(): VirtualFile = file

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun selectNotify() {
        // Metodo chiamato quando l'editor viene selezionato
    }

    override fun deselectNotify() {
        // Metodo chiamato quando l'editor viene deselezionato
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(listener)
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        propertyChangeSupport.removePropertyChangeListener(listener)
    }
}
