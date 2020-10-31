package com.addressbook.opencsv.gson;

public class PersonInfo {
	public String firstName;
	public String lastName;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String phoneNo;
	public String email;
	public String addressBookName;
	public String addressBookType;

	public PersonInfo(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
	}

	public PersonInfo(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email, String addressBookName, String addressBookType) {
		this(firstName, lastName, address, city, state, zip, phoneNo, email);
		this.addressBookName = addressBookName;
		this.addressBookType = addressBookType;
	}

	public PersonInfo() {
	}

	@Override
	public String toString() {
		return "First Name: " + this.firstName + " Last Name: " + this.lastName + " Address: " + this.address
				+ " City: " + this.city + " State: " + this.state + " Zip: " + this.zip + " Phone Number: "
				+ this.phoneNo + " Email: " + this.email + " Address book name" + this.addressBookName + " type"
				+ addressBookType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PersonInfo that = (PersonInfo) o;
		return firstName.equals(that.firstName) && address.equals(that.address);
	}
}