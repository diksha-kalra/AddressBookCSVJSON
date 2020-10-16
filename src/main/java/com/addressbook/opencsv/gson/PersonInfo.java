package com.addressbook.opencsv.gson;

public class PersonInfo {

	public String first_name;
	public String last_name;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String phone_no;
	public String email;

	public PersonInfo(String first_name, String last_name, String address, String city, String state, String zip,
			String phone_no, String email) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone_no = phone_no;
		this.email = email;
	}

	public PersonInfo() {
	}

	public String toString() {
		return "First Name: " + this.first_name + " Last Name: " + this.last_name + " Address: " + this.address
				+ " City: " + this.city + " State: " + this.state + " Zip: " + this.zip + " Phone Number: "
				+ this.phone_no + " Email: " + this.email;
	}
}