/*
 * Autopsy Forensic Browser
 *
 * Copyright 2013-2016 Basis Technology Corp.
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
package org.sleuthkit.autopsy.experimental.enterpriseartifactmanager.optionspanel;

import org.sleuthkit.autopsy.coreutils.Logger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.corecomponents.OptionsPanel;
import org.sleuthkit.autopsy.coreutils.ModuleSettings;
import org.sleuthkit.autopsy.events.AutopsyEvent;
import org.sleuthkit.autopsy.ingest.IngestManager;
import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import org.sleuthkit.autopsy.experimental.enterpriseartifactmanager.datamodel.EamDbPlatformEnum;
import org.sleuthkit.autopsy.experimental.enterpriseartifactmanager.datamodel.EamDb;

/**
 * Main settings panel for the enterprise artifact manager
 */
public final class EamGlobalSettingsPanel extends IngestModuleGlobalSettingsPanel implements OptionsPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EamGlobalSettingsPanel.class.getName());

    private final IngestJobEventPropertyChangeListener ingestJobEventListener;

    private boolean dbConfigured;
    private boolean initiallyEnabled;
    private boolean requireReboot;
    private boolean comboboxSelectDatabaseTypeActionListenerActive;

    /**
     * Creates new form EnterpriseArtifactManagerOptionsPanel
     */
    public EamGlobalSettingsPanel() {
        ingestJobEventListener = new IngestJobEventPropertyChangeListener();

        initComponents();
        customizeComponents();
        addIngestJobEventsListener();
    }

    @Messages({"EnterpriseArtifactManagerGlobalSettingsPanel.title=Global Enterprise Artifact Manager Settings"})
    private void customizeComponents() {
        setName(Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_title());
        comboboxSelectDatabaseTypeActionListenerActive = false; // don't fire action listener while loading combobox content
        comboboxSelectDatabaseType.removeAllItems();
        for (EamDbPlatformEnum p : EamDbPlatformEnum.values()) {
            comboboxSelectDatabaseType.addItem(p.toString());
        }
        comboboxSelectDatabaseTypeActionListenerActive = true;
    }

    private void addIngestJobEventsListener() {
        IngestManager.getInstance().addIngestJobEventListener(ingestJobEventListener);
        ingestStateUpdated();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        pnOverallPanel = new javax.swing.JPanel();
        pnSettings = new javax.swing.JPanel();
        bnImportDatabase = new javax.swing.JButton();
        pnDatabaseConnectionSettings = new javax.swing.JPanel();
        comboboxSelectDatabaseType = new javax.swing.JComboBox<>();
        lbDatabasePlatform = new javax.swing.JLabel();
        bnConfigureDatabaseSettings = new javax.swing.JButton();
        tbOops = new javax.swing.JTextField();
        bnManageTags = new javax.swing.JButton();
        bnManageTypes = new javax.swing.JButton();
        cbEnableEnterpriseArtifactManager = new javax.swing.JCheckBox();

        setName(""); // NOI18N

        jScrollPane.setBorder(null);

        bnImportDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/experimental/images/import16.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bnImportDatabase, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EnterpriseArtifactManagerGlobalSettingsPanel.bnImportDatabase.label")); // NOI18N
        bnImportDatabase.setActionCommand(org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnImportDatabase.actionCommand")); // NOI18N
        bnImportDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnImportDatabaseActionPerformed(evt);
            }
        });

        pnDatabaseConnectionSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EnterpriseArtifactManagerGlobalSettingsPanel.lbDatabaseSettings.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnDatabaseConnectionSettings.setName(""); // NOI18N

        comboboxSelectDatabaseType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "test 1", "test 2" }));
        comboboxSelectDatabaseType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxSelectDatabaseTypeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbDatabasePlatform, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.lbDatabasePlatform.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(bnConfigureDatabaseSettings, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnConfigureDatabaseSettings.text")); // NOI18N
        bnConfigureDatabaseSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnConfigureDatabaseSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDatabaseConnectionSettingsLayout = new javax.swing.GroupLayout(pnDatabaseConnectionSettings);
        pnDatabaseConnectionSettings.setLayout(pnDatabaseConnectionSettingsLayout);
        pnDatabaseConnectionSettingsLayout.setHorizontalGroup(
            pnDatabaseConnectionSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDatabaseConnectionSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbDatabasePlatform)
                .addGap(18, 18, 18)
                .addComponent(comboboxSelectDatabaseType, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bnConfigureDatabaseSettings)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnDatabaseConnectionSettingsLayout.setVerticalGroup(
            pnDatabaseConnectionSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDatabaseConnectionSettingsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(pnDatabaseConnectionSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDatabasePlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboboxSelectDatabaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bnConfigureDatabaseSettings)))
        );

        tbOops.setEditable(false);
        tbOops.setFont(tbOops.getFont().deriveFont(tbOops.getFont().getStyle() | java.awt.Font.BOLD, 12));
        tbOops.setForeground(new java.awt.Color(255, 0, 0));
        tbOops.setText(org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.tbOops.text")); // NOI18N
        tbOops.setBorder(null);

        org.openide.awt.Mnemonics.setLocalizedText(bnManageTags, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnManageTags.text")); // NOI18N
        bnManageTags.setToolTipText(org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnManageTags.toolTipText")); // NOI18N
        bnManageTags.setActionCommand(org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnManageTags.actionCommand")); // NOI18N
        bnManageTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnManageTagsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bnManageTypes, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.bnManageTypes.text")); // NOI18N
        bnManageTypes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnManageTypesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnSettingsLayout = new javax.swing.GroupLayout(pnSettings);
        pnSettings.setLayout(pnSettingsLayout);
        pnSettingsLayout.setHorizontalGroup(
            pnSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbOops)
                    .addGroup(pnSettingsLayout.createSequentialGroup()
                        .addGroup(pnSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnDatabaseConnectionSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnSettingsLayout.createSequentialGroup()
                                .addComponent(bnImportDatabase)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bnManageTags)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bnManageTypes)))
                        .addGap(0, 188, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnSettingsLayout.setVerticalGroup(
            pnSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnSettingsLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(tbOops, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnDatabaseConnectionSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                .addGroup(pnSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bnImportDatabase)
                    .addComponent(bnManageTags, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bnManageTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        bnImportDatabase.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EnterpriseArtifactManagerGlobalSettingsPanel.bnImportDatabase.label")); // NOI18N

        cbEnableEnterpriseArtifactManager.setFont(cbEnableEnterpriseArtifactManager.getFont().deriveFont(cbEnableEnterpriseArtifactManager.getFont().getStyle() & ~java.awt.Font.BOLD, 11));
        org.openide.awt.Mnemonics.setLocalizedText(cbEnableEnterpriseArtifactManager, org.openide.util.NbBundle.getMessage(EamGlobalSettingsPanel.class, "EamGlobalSettingsPanel.cbEnableEnterpriseArtifactManager.text")); // NOI18N
        cbEnableEnterpriseArtifactManager.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbEnableEnterpriseArtifactManagerItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnOverallPanelLayout = new javax.swing.GroupLayout(pnOverallPanel);
        pnOverallPanel.setLayout(pnOverallPanelLayout);
        pnOverallPanelLayout.setHorizontalGroup(
            pnOverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnOverallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnOverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnOverallPanelLayout.createSequentialGroup()
                        .addComponent(cbEnableEnterpriseArtifactManager, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pnSettings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnOverallPanelLayout.setVerticalGroup(
            pnOverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnOverallPanelLayout.createSequentialGroup()
                .addComponent(cbEnableEnterpriseArtifactManager)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane.setViewportView(pnOverallPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane)
                .addGap(2, 2, 2))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbEnableEnterpriseArtifactManagerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbEnableEnterpriseArtifactManagerItemStateChanged
        tbOops.setText("");
        if (!cbEnableEnterpriseArtifactManager.isSelected()) {
            enableAllSubComponents(false);
            firePropertyChange(OptionsPanelController.PROP_CHANGED, null, null);
        } else {
            enableDatabaseSubComponents(true);
            validateDatabaseSettings();
        }
    }//GEN-LAST:event_cbEnableEnterpriseArtifactManagerItemStateChanged

    private void bnImportDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnImportDatabaseActionPerformed
        EamImportDatabaseDialog dialog = new EamImportDatabaseDialog();
        firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }//GEN-LAST:event_bnImportDatabaseActionPerformed

    /**
     * When the "Configure" button is clicked, open the proper dialog.
     *
     * @param evt Button event
     */
    @Messages({"EnterpriseArtifactManagerGlobalSettingsPanel.configureButton.errorLabel=You must select a valid platform in the drop down box.",
        "EnterpriseArtifactManagerGlobalSettingsPanel.configureButton.errorTitle=Invalid platform selection."})
    private void bnConfigureDatabaseSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnConfigureDatabaseSettingsActionPerformed
        EamDbPlatformEnum selectedPlatform = EamDbPlatformEnum.getSelectedPlatform();
        Boolean dbConfigChanged = false;

        switch (selectedPlatform) {
            case SQLITE:
                EamSqliteSettingsDialog dialogS = new EamSqliteSettingsDialog();
                dbConfigChanged = dialogS.isChanged();
                break;

            case POSTGRESQL:
                EamPostgresSettingsDialog dialogP = new EamPostgresSettingsDialog();
                dbConfigChanged = dialogP.isChanged();
                break;

            default:
                JOptionPane.showMessageDialog(null, Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_configureButton_errorLabel(),
                        Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_configureButton_errorTitle(),
                        JOptionPane.ERROR_MESSAGE);
                break;
        }

        if (dbConfigChanged) {
            if (initiallyEnabled || dbConfigured) {
                requireReboot = true;
                enableButtonSubComponents(false);
            }
            dbConfigured = true;
            firePropertyChange(OptionsPanelController.PROP_CHANGED, null, null);
        }
    }//GEN-LAST:event_bnConfigureDatabaseSettingsActionPerformed

    /**
     * When there is a change to the combobox, update the selectedPlatform.
     *
     * @param evt
     */
    @SuppressWarnings({"unchecked cast", "unchecked"})
    private void comboboxSelectDatabaseTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxSelectDatabaseTypeActionPerformed
        if (comboboxSelectDatabaseTypeActionListenerActive) {
            JComboBox<String> cb = (JComboBox<String>) evt.getSource();
            String platformName = (String) cb.getSelectedItem();
            EamDbPlatformEnum.setSelectedPlatform(platformName);
        }
    }//GEN-LAST:event_comboboxSelectDatabaseTypeActionPerformed

    private void bnManageTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnManageTagsActionPerformed
        EamManageTagDialog dialog = new EamManageTagDialog();
        firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }//GEN-LAST:event_bnManageTagsActionPerformed

    private void bnManageTypesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnManageTypesActionPerformed
        EamTypesSelectionDialog dialogT = new EamTypesSelectionDialog();
        firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }//GEN-LAST:event_bnManageTypesActionPerformed

    @Override
    public void load() {
        tbOops.setText("");

        enableAllSubComponents(false);

        initiallyEnabled = Boolean.valueOf(ModuleSettings.getConfigSetting("EnterpriseArtifactManager", "db.enabled")); // NON-NLS
        cbEnableEnterpriseArtifactManager.setSelected(initiallyEnabled); // NON-NLS
        String selectedPlatformString = ModuleSettings.getConfigSetting("EnterpriseArtifactManager", "db.selectedPlatform"); // NON-NLS
        dbConfigured = selectedPlatformString != null;
        requireReboot = false;

        if (dbConfigured) {
            comboboxSelectDatabaseTypeActionListenerActive = false; // don't fire action listener while configuring combobox content
            comboboxSelectDatabaseType.setSelectedIndex(EamDbPlatformEnum.getSelectedPlatform().ordinal());
            comboboxSelectDatabaseTypeActionListenerActive = true; // don't fire action listener while loading combobox content
        }
        if (this.valid() && initiallyEnabled) {
            enableButtonSubComponents(true);
        }
        this.ingestStateUpdated();
    }

    @Override
    public void store() { // Click OK or Apply on Options Panel
        saveSettings();
    }

    /**
     * Validates that the form is filled out correctly for our usage.
     *
     * @return true if it's okay, false otherwise.
     */
    boolean valid() {
        tbOops.setText("");

        if (cbEnableEnterpriseArtifactManager.isSelected()) {
            return validateDatabaseSettings();
        } else {
            return true;
        }
    }

    /**
     * Validate the Database Settings panel
     *
     * @return true or false
     */
    @Messages({"EnterpriseArtifactManagerGlobalSettingsPanel.validate.mustConfigureDb.text=You must configure the database."})
    private boolean validateDatabaseSettings() {
        if (!dbConfigured) {
            tbOops.setText(Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_validate_mustConfigureDb_text());
            return false;
        }

        return true;
    }

    @Messages({"EnterpriseArtifactManagerGlobalSettingsPanel.restartRequiredTitle.text=Application restart required",
        "EnterpriseArtifactManagerGlobalSettingsPanel.restartRequiredLabel.text=Autopsy must be restarted for new configuration to take effect."})
    @Override
    public void saveSettings() { // Click OK on Global Settings Panel
        ModuleSettings.setConfigSetting("EnterpriseArtifactManager", "db.enabled", Boolean.toString(cbEnableEnterpriseArtifactManager.isSelected())); // NON-NLS
        if (cbEnableEnterpriseArtifactManager.isSelected()) {
            EamDbPlatformEnum.saveSelectedPlatform();

            if (requireReboot) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_restartRequiredLabel_text(),
                            Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_restartRequiredTitle_text(),
                            JOptionPane.WARNING_MESSAGE);
                });
            } else {
                EamDb dbManager = EamDb.getInstance();
                dbManager.updateSettings();
                enableButtonSubComponents(true);
            }
        }
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        IngestManager.getInstance().removeIngestJobEventListener(ingestJobEventListener);
        super.finalize();
    }

    /**
     * An ingest job event listener that disables the options panel while an
     * ingest job is running.
     */
    private class IngestJobEventPropertyChangeListener implements PropertyChangeListener {

        /**
         * Listens for local ingest job started, completed or cancel events and
         * enables/disables the options panel according to the job state.
         *
         * @param event
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (AutopsyEvent.SourceType.LOCAL == ((AutopsyEvent) event).getSourceType()) {
                ingestStateUpdated();
            }
        }
    };

    @Messages({"EnterpriseArtifactManagerGlobalSettingsPanel.validationErrMsg.ingestRunning=Cannot change settings while ingest is running."})
    private void ingestStateUpdated() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                ingestStateUpdated();
            });

            return;
        }

        if (IngestManager.getInstance().isIngestRunning()) {
            cbEnableEnterpriseArtifactManager.setEnabled(false);
            tbOops.setText(Bundle.EnterpriseArtifactManagerGlobalSettingsPanel_validationErrMsg_ingestRunning());
            enableAllSubComponents(false);
        } else {
            cbEnableEnterpriseArtifactManager.setEnabled(true);
            tbOops.setText("");
            enableAllSubComponents(cbEnableEnterpriseArtifactManager.isSelected());
        }
    }

    /**
     * Wrapper around each of the enableComponentXYZ methods to enable/disable
     * them all at the same time.
     *
     * @param enable
     *
     * @return True
     */
    private boolean enableAllSubComponents(Boolean enable) {
        enableDatabaseSubComponents(enable);
        enableButtonSubComponents(enable);
        return true;
    }

    /**
     * Wrapper around each of the enableXYZ methods that configure the database
     * to enable/disable them all at the same time.
     *
     * @param enable
     *
     * @return True
     */
    private boolean enableDatabaseSubComponents(Boolean enable) {
        enableDatabasePlatformComboBox(enable);
        enableConfigureDatabasePlatformButton(enable);
        return true;
    }

    /**
     * Wrapper around each of the enableComponentXYZButton methods to
     * enable/disable them all at the same time.
     *
     * @param enable
     *
     * @return True
     */
    private boolean enableButtonSubComponents(Boolean enable) {
        enableManageCorrelationTypesButton(enable);
        enableImportGloballyKnownArtifactsButton(enable);
        enableManageTagsButton(enable);
        return true;
    }

    /**
     * Enables the ComboBox used to select the database platform.
     *
     * @param enable
     *
     * @return True or False
     */
    private boolean enableDatabasePlatformComboBox(Boolean enable) {
        comboboxSelectDatabaseType.setEnabled(enable);
        return enable;
    }

    /**
     * Enables the "Configure" button used to configure the database platform.
     *
     * @param enable
     *
     * @return True or False
     */
    private boolean enableConfigureDatabasePlatformButton(Boolean enable) {
        bnConfigureDatabaseSettings.setEnabled(enable);
        return enable;
    }

    /**
     * Enables the "Import Globally Known Artifacts" button.
     *
     * @param enable
     *
     * @return True or False
     */
    private boolean enableImportGloballyKnownArtifactsButton(Boolean enable) {
        bnImportDatabase.setEnabled(enable);
        return enable;
    }

    /**
     * Enables the "Manage Correlation Types" button.
     *
     * @param enable
     *
     * @return True or False
     */
    private boolean enableManageCorrelationTypesButton(Boolean enable) {
        bnManageTypes.setEnabled(enable);
        return enable;
    }

    /**
     * Enables the "Manage Tags" button.
     *
     * @param enable
     *
     * @return True or False
     */
    private boolean enableManageTagsButton(Boolean enable) {
        bnManageTags.setEnabled(enable);
        return enable;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bnConfigureDatabaseSettings;
    private javax.swing.JButton bnImportDatabase;
    private javax.swing.JButton bnManageTags;
    private javax.swing.JButton bnManageTypes;
    private javax.swing.JCheckBox cbEnableEnterpriseArtifactManager;
    private javax.swing.JComboBox<String> comboboxSelectDatabaseType;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JLabel lbDatabasePlatform;
    private javax.swing.JPanel pnDatabaseConnectionSettings;
    private javax.swing.JPanel pnOverallPanel;
    private javax.swing.JPanel pnSettings;
    private javax.swing.JTextField tbOops;
    // End of variables declaration//GEN-END:variables
}
