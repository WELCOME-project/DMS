package edu.upf.taln.welcome.dms.commons.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "name", "address", "hours" })
public class TemplateHoursData extends TemplatePersonalData implements TemplateData {

    private List<TimePeriod> hours;

    public List<TimePeriod> getHours() {
        return hours;
    }

    public void setHours(List<TimePeriod> hours) {
        this.hours = hours;
    }
    
}
