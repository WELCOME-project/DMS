package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author rcarlini
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TemplateHandshakeData.class, name = "TemplateHandshakeData"),
        @JsonSubTypes.Type(value = TemplatePersonalData.class, name = "TemplatePersonalData"),
        @JsonSubTypes.Type(value = TemplateRequestData.class, name = "TemplateRequestData"),
        @JsonSubTypes.Type(value = TemplateHoursData.class, name = "TemplateHoursData")
})
public abstract class TemplateData {
}
