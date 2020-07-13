package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.upf.taln.welcome.dms.commons.output.Address;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "city", "street", "number", "building" })
public class BuildingAddress extends Address {
    private String building;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    
}
