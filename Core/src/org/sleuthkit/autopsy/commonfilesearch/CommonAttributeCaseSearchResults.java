/*
 *
 * Autopsy Forensic Browser
 *
 * Copyright 2018 Basis Technology Corp.
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
package org.sleuthkit.autopsy.commonfilesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttributeInstance;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttributeNormalizationException;
import org.sleuthkit.autopsy.centralrepository.datamodel.EamDb;
import org.sleuthkit.autopsy.centralrepository.datamodel.EamDbException;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.AbstractFile;

/**
 * Stores the results from the various types of common attribute searching
 * Stores results based on how they are currently displayed in the UI
 */
final public class CommonAttributeCaseSearchResults {

    private static final Logger LOGGER = Logger.getLogger(CommonAttributeCaseSearchResults.class.getName());

    // maps instance count to list of attribute values. 
    private Map<String, Map<String, CommonAttributeValueList>> caseNameToDataSources;
    private final Set<String> mimeTypesToInclude;
    private final int percentageThreshold;
    private final int resultTypeId;

    /**
     * Create a values object which can be handed off to the node factories.
     *
     * @param metadata            list of CommonAttributeValue indexed by size
     *                            of CommonAttributeValue
     * @param percentageThreshold threshold to filter out files which are too
     *                            common, value of 0 is disabled
     * @param resultType          The type of Correlation Attribute being
     *                            searched for
     * @param mimeTypesToFilterOn Set of mime types to include for intercase
     *                            searches
     */
    CommonAttributeCaseSearchResults(Map<String, Map<String, CommonAttributeValueList>> metadata, int percentageThreshold, CorrelationAttributeInstance.Type resultType, Set<String> mimeTypesToFilterOn) {
        //wrap in a new object in case any client code has used an unmodifiable collection
        this.caseNameToDataSources = new HashMap<>(metadata);
        this.percentageThreshold = percentageThreshold;
        this.resultTypeId = resultType.getId();
        this.mimeTypesToInclude = mimeTypesToFilterOn;
    }

    /**
     * Create a values object which can be handed off to the node factories.
     *
     * @param metadata            list of CommonAttributeValue indexed by size
     *                            of CommonAttributeValue
     * @param percentageThreshold threshold to filter out files which are too
     *                            common, value of 0 is disabled
     */
    CommonAttributeCaseSearchResults(Map<String, Map<String, CommonAttributeValueList>> metadata, int percentageThreshold) {
        //wrap in a new object in case any client code has used an unmodifiable collection
        this.caseNameToDataSources = new HashMap<>(metadata);
        this.percentageThreshold = percentageThreshold;
        this.resultTypeId = CorrelationAttributeInstance.FILES_TYPE_ID;
        this.mimeTypesToInclude = new HashSet<>(); //don't filter on mimetypes
    }

    /**
     * Find the child node whose children have the specified number of children.
     *
     * This is a convenience method - you can also iterate over
     * <code>getValues()</code>.
     *
     * @param caseName caseNameKey
     *
     * @return list of values which represent matches
     */
    Map<String, CommonAttributeValueList> getAttributeValuesForCaseName(String caseName) {
        return this.caseNameToDataSources.get(caseName);
    }

    /**
     * Get an unmodifiable collection of values, indexed by number of
     * grandchildren, which represents the common attributes found in the
     * search.
     *
     * @return map of sizes of children to list of matches
     */
    public Map<String, Map<String, CommonAttributeValueList>> getMetadata() throws EamDbException {
        if (this.percentageThreshold == 0 && mimeTypesToInclude.isEmpty()) {
            return Collections.unmodifiableMap(this.caseNameToDataSources);
        } else {
            return this.getMetadata(this.percentageThreshold);
        }
    }

    /**
     * Get an unmodifiable collection of values, indexed by number of
     * grandchildren, which represents the common attributes found in the
     * search.
     *
     * Remove results which are not found in the portion of available data
     * sources described by maximumPercentageThreshold.
     *
     * @return metadata
     */
    private Map<String, Map<String, CommonAttributeValueList>> getMetadata(int maximumPercentageThreshold) throws EamDbException {
        CorrelationAttributeInstance.Type attributeType = CorrelationAttributeInstance
                .getDefaultCorrelationTypes()
                .stream()
                .filter(filterType -> filterType.getId() == this.resultTypeId)
                .findFirst().get();

        EamDb eamDb = EamDb.getInstance();
        //Call countUniqueDataSources once to reduce the number of DB queries needed to get
        //the frequencyPercentage
        Double uniqueCaseDataSourceTuples = eamDb.getCountUniqueDataSources().doubleValue();
        Set<String> valuesToRemove = new HashSet<>();
        for (Entry<String, Map<String, CommonAttributeValueList>> mapOfDataSources : Collections.unmodifiableMap(this.caseNameToDataSources).entrySet()) {
            for (Entry<String, CommonAttributeValueList> mapOfValueLists : Collections.unmodifiableMap(mapOfDataSources.getValue()).entrySet()) {
                for (CommonAttributeValue value : mapOfValueLists.getValue().getDelayedMetadataList()) {
                    //Intracase common attribute searches will have been created with an empty mimeTypesToInclude list 
                    //because when performing intra case search this filtering will have been done during the query of the case database 
                    boolean mimeTypeToRemove = false;  //allow code to be more efficient by not attempting to remove the same value multiple times
                    if (!mimeTypesToInclude.isEmpty()) { //only do the mime type filtering when mime types aren't empty
                        for (AbstractCommonAttributeInstance commonAttr : value.getInstances()) {
                            AbstractFile abstractFile = commonAttr.getAbstractFile();
                            if (abstractFile != null) {
                                String mimeType = commonAttr.getAbstractFile().getMIMEType();
                                if (mimeType != null && !mimeTypesToInclude.contains(mimeType)) {
                                    valuesToRemove.add(value.getValue());
                                    //value will be removed as the mime type existed and was not in the set to be included
                                    //because value is removed this value does not need to be checked further
                                    mimeTypeToRemove = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!mimeTypeToRemove && maximumPercentageThreshold != 0) {  //only do the frequency filtering when a max % was set
                        try {
                            Double uniqueTypeValueTuples = eamDb.getCountUniqueCaseDataSourceTuplesHavingTypeValue(
                                    attributeType, value.getValue()).doubleValue();
                            Double commonalityPercentage = uniqueTypeValueTuples / uniqueCaseDataSourceTuples * 100;
                            int frequencyPercentage = commonalityPercentage.intValue();
                            if (frequencyPercentage > maximumPercentageThreshold) {
                                valuesToRemove.add(value.getValue());
                            }
                        } catch (CorrelationAttributeNormalizationException ex) {
                            LOGGER.log(Level.WARNING, "Unable to determine frequency percentage attribute - frequency filter may not be accurate for these results.", ex);
                        }
                    }
                }
            }
        }
        if (!valuesToRemove.isEmpty()) {
            Map<String, Map<String, CommonAttributeValueList>> filteredCaseNameToDataSources = new HashMap<>();
            for (Entry<String, Map<String, CommonAttributeValueList>> mapOfDataSources : Collections.unmodifiableMap(this.caseNameToDataSources).entrySet()) {
                for (Entry<String, CommonAttributeValueList> mapOfValueLists : Collections.unmodifiableMap(mapOfDataSources.getValue()).entrySet()) {
                    for (CommonAttributeValue value : mapOfValueLists.getValue().getDelayedMetadataList()) {
                        //rebuild collection with values 
                        if (!valuesToRemove.contains(value.getValue())) {
                            Map<String, CommonAttributeValueList> filteredMapOfDataSources;
                            CommonAttributeValueList filteredCommonAttributeValueList;
                            final String caseName = mapOfDataSources.getKey();
                            final String dataSourceKey = mapOfValueLists.getKey();
                            if (filteredCaseNameToDataSources.containsKey(caseName)) {
                                filteredMapOfDataSources = filteredCaseNameToDataSources.get(caseName);
                            } else {
                                filteredMapOfDataSources = new HashMap<>();
                            }
                            if (filteredMapOfDataSources.containsKey(dataSourceKey)) {
                                filteredCommonAttributeValueList = filteredMapOfDataSources.get(dataSourceKey);
                            } else {
                                filteredCommonAttributeValueList = new CommonAttributeValueList();
                            }
                            filteredCommonAttributeValueList.addMetadataToList(value);
                            filteredMapOfDataSources.put(dataSourceKey, filteredCommonAttributeValueList);
                            filteredCaseNameToDataSources.put(caseName, filteredMapOfDataSources);
                        }
                    }
                }
            }
            caseNameToDataSources = filteredCaseNameToDataSources;
        }

        return Collections.unmodifiableMap(caseNameToDataSources);
    }
}
