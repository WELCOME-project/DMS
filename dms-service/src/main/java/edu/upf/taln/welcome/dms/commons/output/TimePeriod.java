package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "day", "opening", "closing" })
public class TimePeriod {
    private String day;
    private String opening;
    private String closing;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }
}
