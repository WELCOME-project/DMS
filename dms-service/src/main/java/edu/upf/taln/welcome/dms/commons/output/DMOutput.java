package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author rcarlini
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DMOutput {
    
    private DMOutputMetadata metadata;
    private DMOutputData data;

    public DMOutputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DMOutputMetadata metadata) {
        this.metadata = metadata;
    }

    public DMOutputData getData() {
        return data;
    }

    public void setData(DMOutputData data) {
        this.data = data;
    }}
