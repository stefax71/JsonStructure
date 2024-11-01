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
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.JBUI
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI

private const val SHOW_JSON_STRUCTURE = "Show JSON Structure"

class JsonCustomEditor(project: Project, private val file: VirtualFile) : FileEditor, Disposable {
    private val mainPanel = JPanel(BorderLayout())
    private var toolbarPanel = JPanel(BorderLayout())
    private val tree = Tree()
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

    override fun getName(): String = "JSON Custom Editor"

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = file.isValid

    override fun dispose() {
        // Libera risorse se necessario
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
