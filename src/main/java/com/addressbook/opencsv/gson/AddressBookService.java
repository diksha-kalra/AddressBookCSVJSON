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

	public void updateContactDetails(String name, String address) {
		int result = addressBookDBService.updateEmployeeData(name, address);
		if (result == 0)
			return;
		PersonInfo personInfo = this.getContactData(name);
		if (personInfo != null)
			personInfo.address = address;
	}

	private PersonInfo getContactData(String name) {
		return this.contactList.stream().filter(contact -> contact.firstName.equals(name)).findFirst().orElse(null);
	}

	public boolean checkConatctDetailsInSyncWithDB(String name) {
		List<PersonInfo> contactList = addressBookDBService.getcontactData(name);
		return contactList.get(0).equals(getContactData(name));
	}
}
