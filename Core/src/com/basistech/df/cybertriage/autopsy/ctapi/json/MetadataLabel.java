/** *************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp. It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2023 Basis Technology Corp. All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ************************************************************************** */
package com.basistech.df.cybertriage.autopsy.ctapi.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata entry.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataLabel {

    private final String key;
    private final String value;
    private final String extendedInfo;
    
    @JsonCreator
    public MetadataLabel(
            @JsonProperty("key") String key, 
            @JsonProperty("value") String value, 
            @JsonProperty("info") String extendedInfo
    ) {
        this.key = key;
        this.value = value;
        this.extendedInfo = extendedInfo;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
    public String getExtendedInfo() {
        return extendedInfo;
    }
    
}
