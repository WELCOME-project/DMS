package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "name", "age", "country_of_origin", "time_arrival_current_residence", "address", "hours" })
public class TemplateRequestData extends TemplatePersonalData implements TemplateData {

    private String age;
    @JsonProperty("country_of_origin")
    private String countryOfOrigin;
    @JsonProperty("time_arrival_current_residence")
    private String arrivalTime;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
