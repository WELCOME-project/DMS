package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Frame {

	@JsonProperty("welcome:Name")
	private String name;

	@JsonProperty("welcome:Age")
	private String age;

	@JsonProperty("welcome:Address")
	private String address;

	@JsonProperty("welcome:CountryOrigin")
	private String countryOrigin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountryOrigin() {
		return countryOrigin;
	}

	public void setCountryOrigin(String countryOrigin) {
		this.countryOrigin = countryOrigin;
	}
}
