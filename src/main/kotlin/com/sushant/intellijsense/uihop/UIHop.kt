package com.sushant.intellijsense.uihop

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Container
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JLayeredPane

class UIHop : DumbAwareAction() {
    private var active = false
    private val highlightedComponents = mutableSetOf<JComponent>()
    private var panel: JLayeredPane? = null

    override fun actionPerformed(e: AnActionEvent) {
        val projectFrame = WindowManager.getInstance().getFrame(e.project) ?: return
        val rootPane = projectFrame.rootPane

        if (active) {
            removeHighlights()
        } else {
            if (panel == null) {
                panel = JLayeredPane()
                panel!!.layout = null
                panel!!.bounds = rootPane.bounds
                panel!!.size = rootPane.size
                panel!!.isVisible = true
                panel!!.isOpaque = false
            }
            if (panel!!.parent == null) {
                rootPane.add(panel)
            }
            rootPane.setComponentZOrder(panel, 0)
            highlightComponents(rootPane.layeredPane, panel!!)
        }
        panel?.repaint()
        active = !active
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        e.presentation.isEnabledAndVisible = project != null
    }

    private fun highlightComponents(component: Container, rootPane: Container) {
        component.components.forEach {
            if (it is JComponent && it.isVisible) {
                if (it is AbstractButton) {
                    val label = JLabel("Aa")
                    label.foreground = JBColor.RED
                    label.bounds = it.bounds
                    rootPane.add(label)
                    highlightedComponents.add(label)
                }
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
}