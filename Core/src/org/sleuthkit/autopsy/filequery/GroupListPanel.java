/*
 * Autopsy
 *
 * Copyright 2019 Basis Technology Corp.
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
package org.sleuthkit.autopsy.filequery;

import com.google.common.eventbus.Subscribe;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.sleuthkit.autopsy.filequery.FileSearch.GroupKey;
import org.sleuthkit.autopsy.filequery.FileSearchData.FileType;

/**
 * Panel to display the list of groups which are provided by a search
 */
class GroupListPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private FileType resultType = null;
    private Map<GroupKey, Integer> groupMap = null;
    private List<FileSearchFiltering.FileFilter> searchfilters;
    private FileSearch.AttributeType groupingAttribute;
    private FileGroup.GroupSortingAlgorithm groupSort;
    private FileSorter.SortingMethod fileSortMethod;
    private GroupKey selectedGroupKey;

    /**
     * Creates new form GroupListPanel
     */
    GroupListPanel() {
        initComponents();
    }

    /**
     * Subscribe to and reset the panel in response to SearchStartedEvents
     *
     * @param searchStartedEvent the SearchStartedEvent which was received
     */
    @Subscribe
    void handleSearchStartedEvent(DiscoveryEvents.SearchStartedEvent searchStartedEvent) {
        resultType = searchStartedEvent.getType();
        groupKeyList.setListData(new GroupKey[0]);
    }

    /**
     * Subscribe to and update list of groups in response to
     * SearchCompleteEvents
     *
     * @param searchCompleteEvent the SearchCompleteEvent which was recieved
     */
    @Subscribe
    void handleSearchCompleteEvent(DiscoveryEvents.SearchCompleteEvent searchCompleteEvent) {
        groupMap = searchCompleteEvent.getGroupMap();
        searchfilters = searchCompleteEvent.getFilters();
        groupingAttribute = searchCompleteEvent.getGroupingAttr();
        groupSort = searchCompleteEvent.getGroupSort();
        fileSortMethod = searchCompleteEvent.getFileSort();
        groupKeyList.setListData(groupMap.keySet().toArray(new GroupKey[groupMap.keySet().size()]));
        if (groupKeyList.getModel().getSize() > 0) {
            groupKeyList.setSelectedIndex(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupListScrollPane = new javax.swing.JScrollPane();
        groupKeyList = new javax.swing.JList<>();

        groupKeyList.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GroupListPanel.class, "GroupListPanel.groupKeyList.border.title"))); // NOI18N
        groupKeyList.setModel(new DefaultListModel<GroupKey>());
        groupKeyList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        groupKeyList.setCellRenderer(new GroupListRenderer());
        groupKeyList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                groupSelected(evt);
            }
        });
        groupListScrollPane.setViewportView(groupKeyList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(groupListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(groupListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reset the group list to be empty.
     */
    void resetGroupList() {
        groupKeyList.setListData(new GroupKey[0]);
    }

    /**
     * Respond to a group being selected by sending a PageRetrievedEvent
     *
     * @param evt the event which indicates a selection occurs in the list
     */
    private void groupSelected(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_groupSelected
        if (!evt.getValueIsAdjusting()) {
            if (groupKeyList.getSelectedValue() != null) {
                GroupKey selectedGroup = groupKeyList.getSelectedValue();
                for (GroupKey groupKey : groupMap.keySet()) {
                    if (selectedGroup.equals(groupKey)) {
                        selectedGroupKey = groupKey;
                        DiscoveryEvents.getDiscoveryEventBus().post(new DiscoveryEvents.GroupSelectedEvent(
                                searchfilters, groupingAttribute, groupSort, fileSortMethod, selectedGroupKey, groupMap.get(selectedGroupKey), resultType));
                        break;
                    }
                }
            } else {
                DiscoveryEvents.getDiscoveryEventBus().post(new DiscoveryEvents.NoResultsEvent());
            }
        }
    }//GEN-LAST:event_groupSelected

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<GroupKey> groupKeyList;
    private javax.swing.JScrollPane groupListScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * GroupListCellRenderer displays GroupKeys as their String value followed
     * by the number of items in the group.
     */
    private class GroupListRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public java.awt.Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            Object newValue = value;
            if (newValue instanceof GroupKey) {
                newValue = newValue.toString() + " (" + groupMap.get(newValue) + ")";
            }
            super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
            return this;
        }
    }

}
