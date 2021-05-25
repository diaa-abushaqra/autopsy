/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011-2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.corecomponents;

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.core.UserPreferences;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContent;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Data content panel.
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
public class DataContentPanel extends javax.swing.JPanel implements DataContent, ChangeListener {

    private static Logger logger = Logger.getLogger(DataContentPanel.class.getName());
    private final List<UpdateWrapper> viewers = new ArrayList<>();
    private Node currentNode;
    private final boolean isMain;
    private boolean listeningToTabbedPane = false;

    private DataContentPanelWorker workerThread;

    /**
     * Creates new DataContentPanel panel The main data content panel can only
     * be created by the data content top component, thus this constructor is
     * not public.
     *
     * Use the createInstance factory method to create an external viewer data
     * content panel.
     *
     */
    DataContentPanel(boolean isMain) {
        this.isMain = isMain;
        initComponents();

        // add all implementors of DataContentViewer and put them in the tabbed pane
        Collection<? extends DataContentViewer> dcvs = Lookup.getDefault().lookupAll(DataContentViewer.class);
        for (DataContentViewer factory : dcvs) {
            DataContentViewer dcv;
            if (isMain) {
                //use the instance from Lookup for the main viewer
                dcv = factory;
            } else {
                dcv = factory.createInstance();
            }
            viewers.add(new UpdateWrapper(dcv));
            javax.swing.JScrollPane scrollTab = new javax.swing.JScrollPane(dcv.getComponent());
            scrollTab.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            jTabbedPane1.addTab(dcv.getTitle(), null,
                    scrollTab, dcv.getToolTip());
        }

        // disable the tabs
        int numTabs = jTabbedPane1.getTabCount();
        for (int tab = 0; tab < numTabs; ++tab) {
            jTabbedPane1.setEnabledAt(tab, false);
        }
    }

    /**
     * Factory method to create an external (not main window) data content panel
     * to be used in an external window
     *
     * @return a new instance of a data content panel
     */
    public static DataContentPanel createInstance() {
        return new DataContentPanel(false);
    }

    public JTabbedPane getTabPanels() {
        return jTabbedPane1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setMinimumSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setNode(Node selectedNode) {
        
        if (workerThread != null) {
            workerThread.cancel(true);
            workerThread = null;
        }
        
        // Reset everything
        for (int index = 0; index < jTabbedPane1.getTabCount(); index++) {
            jTabbedPane1.setEnabledAt(index, false);
            viewers.get(index).resetComponent();
        }

        if (selectedNode != null) {
            workerThread = new DataContentPanelWorker(selectedNode);
            workerThread.execute();
        }
    }

    /**
     * Update the state of the tabs based on the given data.
     *
     * @param selectedNode     The currently selected node.
     * @param supportedIndices The indices of the tabs that are supported by
     *                         this node type.
     * @param preferredIndex   The index of the tab which is preferred.
     */
    private void updateTabs(Node selectedNode, List<Integer> supportedIndices, int preferredIndex) {
        // Deferring becoming a listener to the tabbed pane until this point
        // eliminates handling a superfluous stateChanged event during construction.
        if (listeningToTabbedPane == false) {
            jTabbedPane1.addChangeListener(this);
            listeningToTabbedPane = true;
        }

        for (Integer index : supportedIndices) {
            jTabbedPane1.setEnabledAt(index, true);
        }

        // let the user decide if we should stay with the current viewer
        int tabIndex = UserPreferences.keepPreferredContentViewer() ? jTabbedPane1.getSelectedIndex() : preferredIndex;

        UpdateWrapper dcv = viewers.get(tabIndex);
        // this is really only needed if no tabs were enabled 
        if (jTabbedPane1.isEnabledAt(tabIndex) == false) {
            dcv.resetComponent();
        } else {
            dcv.setNode(selectedNode);
        }

        // set the tab to the one the user wants, then set that viewer's node.
        jTabbedPane1.setSelectedIndex(tabIndex);
        jTabbedPane1.getSelectedComponent().repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        JTabbedPane pane = (JTabbedPane) evt.getSource();

        // Get and set current selected tab
        int currentTab = pane.getSelectedIndex();
        if (currentTab != -1) {
            UpdateWrapper dcv = viewers.get(currentTab);
            if (dcv.isOutdated()) {
                // change the cursor to "waiting cursor" for this operation
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    dcv.setNode(currentNode);
                } finally {
                    this.setCursor(null);
                }
            }
        }
    }

    private static class UpdateWrapper {

        private final DataContentViewer wrapped;
        private boolean outdated;

        UpdateWrapper(DataContentViewer wrapped) {
            this.wrapped = wrapped;
            this.outdated = true;
        }

        void setNode(Node selectedNode) {
            this.wrapped.setNode(selectedNode);
            this.outdated = false;
        }

        void resetComponent() {
            this.wrapped.resetComponent();
            this.outdated = true;
        }

        boolean isOutdated() {
            return this.outdated;
        }

        boolean isSupported(Node node) {
            return this.wrapped.isSupported(node);
        }

        int isPreferred(Node node) {
            return this.wrapped.isPreferred(node);
        }
    }

    /**
     * SwingWorker class to determine which tabs should be enabled for the given
     * node.
     */
    private class DataContentPanelWorker extends SwingWorker<WorkerResults, Void> {

        private final Node node;

        /**
         * Worker constructor.
         *
         * @param node
         */
        DataContentPanelWorker(Node node) {
            this.node = node;
        }

        @Override
        protected WorkerResults doInBackground() throws Exception {

            List<Integer> supportedViewers = new ArrayList<>();
            int preferredViewerIndex = 0;
            int maxPreferred = 0;

            for (int index = 0; index < viewers.size(); index++) {
                UpdateWrapper dcv = viewers.get(index);
                if (dcv.isSupported(node)) {
                    supportedViewers.add(index);

                    int currentPreferred = dcv.isPreferred(node);
                    if (currentPreferred > maxPreferred) {
                        preferredViewerIndex = index;
                        maxPreferred = currentPreferred;
                    }
                }

                if (this.isCancelled()) {
                    return null;
                }

            }
            
            return new WorkerResults(node, supportedViewers, preferredViewerIndex);
        }

        @Override
        protected void done() {
            // Do nothing if the thread was cancelled.
            if (isCancelled()) {
                return;
            }

            try {
                WorkerResults results = get();

                if (results != null) {
                    updateTabs(results.getNode(), results.getSupportedIndices(), results.getPreferredViewerIndex());
                }

            } catch (InterruptedException | ExecutionException ex) {
                logger.log(Level.SEVERE, "Failed to updated data content panel for node " + node.getName(), ex);
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * Utility class to store all of the data the SwingWorker collected.
     */
    private class WorkerResults {

        private final Node node;
        private final List<Integer> supportedViewerIndices;
        private final int preferredViewerIndex;

        WorkerResults(Node node, List<Integer> supportedViewerIndices, int preferredViewerIndex) {
            this.node = node;
            this.supportedViewerIndices = supportedViewerIndices;
            this.preferredViewerIndex = preferredViewerIndex;
        }

        /**
         * Returns the selected node.
         *
         * @return
         */
        Node getNode() {
            return node;
        }

        /**
         * A list of tab indices that are supported by this node type.
         *
         * @return A list of indices.
         */
        List<Integer> getSupportedIndices() {
            return supportedViewerIndices;
        }

        /**
         * Returns the preferred tab index for the given node type.
         *
         * @return A valid tab index.
         */
        int getPreferredViewerIndex() {
            return preferredViewerIndex;
        }
    }
}
