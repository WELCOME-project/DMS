package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "id", "type", "dialogueTurn", "template", "data" })
public class SpeechAct {
    @NotNull
    private String id;
    @NotNull
    private String type;
    private String template;
    private Map<String, TemplateData> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}
