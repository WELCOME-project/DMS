package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */

@JsonPropertyOrder({ "name", "address" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplatePersonalData implements TemplateData {
    
    private String name;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
