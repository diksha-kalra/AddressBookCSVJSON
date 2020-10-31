package com.addressbook.opencsv.gson;

import java.util.List;

public class AddressBookService {

	private List<PersonInfo> contactList;
	private AddressBookDBService addressBookDBService;

	public AddressBookService(List<PersonInfo> contactList) {
		this();
		this.contactList = contactList;
	}

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public List<PersonInfo> readContactData() {
		this.contactList = addressBookDBService.readData();
		return contactList;
	}
}
