package edu.upf.taln.welcome.dms.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.upf.taln.welcome.dms.commons.output.TemplateData;
import edu.upf.taln.welcome.dms.commons.output.TimePeriod;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "name", "address", "hours" })
public class TemplateHoursData extends TemplateData {

    private List<TimePeriod> hours;

    public List<TimePeriod> getHours() {
        return hours;
    }

    public void setHours(List<TimePeriod> hours) {
        this.hours = hours;
    }
    
}
