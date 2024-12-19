package com.sushant.intellijsense.uihop

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JLayeredPane
import javax.swing.JRootPane

class UIHop : DumbAwareAction() {
    private var highlightingActive = false
    private val highlightedComponents = mutableSetOf<JComponent>()
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
            if (it is JComponent && it.isVisible && it is ActionButton) {
                val label = JLabel("Aa")
                label.foreground = JBColor.RED
                label.bounds = it.bounds

                rootPane.add(label)
                highlightedComponents.add(label)
            }
            if (it is Container) {
                highlightComponents(it, rootPane)
            }
        }
    }

    private fun removeHighlights() {
        highlightedComponents.forEach {
            it.parent.remove(it)
        }
        highlightedComponents.clear()
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        e.presentation.isEnabledAndVisible = project != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

    private fun createPanel(rootPane: JRootPane) {
        highlightPane = JLayeredPane()
        highlightPane!!.bounds = rootPane.bounds
        highlightPane!!.layout = null
        highlightPane!!.isVisible = true
        highlightPane!!.isOpaque = false
        rootPane.add(highlightPane)
        rootPane.setComponentZOrder(highlightPane, 0)
    }
}