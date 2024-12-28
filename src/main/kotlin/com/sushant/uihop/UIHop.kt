package com.sushant.uihop

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Container
import javax.swing.*

class UIHop : DumbAwareAction() {
    private var highlightingActive = false
    private val highlightedComponents = HashMap<String, JComponent>()
    private var highlightPane: JLayeredPane? = null

    override fun actionPerformed(e: AnActionEvent) {
        val projectFrame = WindowManager.getInstance().getFrame(e.project) ?: return
        val rootPane = projectFrame.rootPane

        // Create pane for all highlights to live on
        if (highlightPane == null) createPanel(rootPane)

        if (highlightingActive) {
            removeHighlights()
        } else {
            highlightComponents(rootPane.layeredPane, highlightPane!!)
        }

        // Update and redraw elements of highlight pane
        highlightPane?.repaint()
        highlightingActive = !highlightingActive
    }

    private fun highlightComponents(component: Container, rootPane: Container) {
        component.components.forEach {
            if (it is JComponent && it.isShowing && it is ActionButton) {
                val combination = generateUniqueCombination()
                val label = JLabel(" $combination ")
                highlightedComponents[combination] = it
                rootPane.add(label)

                val location = SwingUtilities.convertPoint(it.parent, it.location, rootPane)
                label.size = label.preferredSize

                label.location = location
                label.isOpaque = true
                label.background = JBColor.YELLOW
                label.foreground = JBColor.WHITE
            }
            if (it is Container) {
                highlightComponents(it, rootPane)
            }
        }
    }

    // Basic function to generate unique combination of 2 letters
    // TODO: Better algorithm to generate unique combinations
    private fun generateUniqueCombination(): String {
        val letters = ('a'..'z')
        // Pick 2 random letters
        var combination: String = (1..2).map { letters.random() }.joinToString("")
        while (combination in highlightedComponents.keys) {
            combination = (1..2).map { letters.random() }.joinToString("")
        }
        return combination
    }

    private fun removeHighlights() {
        highlightPane!!.removeAll()
        highlightedComponents.clear()
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        e.presentation.isEnabledAndVisible = project != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

    private fun createPanel(rootPane: JRootPane) {
        highlightPane = JLayeredPane()
        highlightPane!!.layout = null
        highlightPane!!.bounds = rootPane.bounds
        highlightPane!!.isVisible = true
        highlightPane!!.isOpaque = false
        rootPane.add(highlightPane)
        rootPane.setComponentZOrder(highlightPane, 0)
    }
}