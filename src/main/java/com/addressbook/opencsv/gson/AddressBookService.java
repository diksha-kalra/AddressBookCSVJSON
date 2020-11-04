package com.addressbook.opencsv.gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AddressBookService {
	private static Logger log = Logger.getLogger(AddressBookService.class.getName());
	private List<PersonInfo> contactList;
	private AddressBookDBService addressBookDBService;
	private AddressBookDBServiceNew addressBookDBServiceNew;
	private Map<String, Integer> contactByCity;

	public enum IOService {
		FILE_IO, DB_IO, REST_IO
	}

	public AddressBookService(List<PersonInfo> contactList) {
		this();
		this.contactList = new ArrayList<>(contactList);
	}

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
		addressBookDBServiceNew = AddressBookDBServiceNew.getInstance();
	}

	public List<PersonInfo> readContactData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO)) {
			this.contactList = (List<PersonInfo>) new AddressBookFileIO().readData();
			return contactList;
		}
		if (ioService.equals(IOService.DB_IO)) {
			this.contactList = addressBookDBService.readData();
			return contactList;
		}
		return null;
	}

	public void updateContactDetails(String name, String address) {
		int result = addressBookDBService.updateContactData(name, address);
		if (result == 0)
			return;
		PersonInfo personInfo = this.getContactData(name);
		if (personInfo != null)
			personInfo.address = address;
	}

	public PersonInfo getContactData(String name) {
		return this.contactList.stream().filter(contact -> contact.firstName.equals(name)).findFirst().orElse(null);
	}

	public boolean checkConatctDetailsInSyncWithDB(String name) {
		List<PersonInfo> contactList = addressBookDBService.getcontactData(name);
		return contactList.get(0).equals(getContactData(name));
	}

	public List<PersonInfo> readContactDataForDateRange(LocalDate startDate, LocalDate endDate) {
		this.contactList = addressBookDBService.getContactForDateRange(startDate, endDate);
		return contactList;
	}

	public Map<String, Integer> readContactByCityOrState() {
		this.contactByCity = addressBookDBService.getContactByCity();
		return contactByCity;
	}

	public void addContactToDB(List<PersonInfo> contactDataList) {
		contactDataList.forEach(personInfo -> {
			this.addContactToAddressBook(personInfo.firstName, personInfo.lastName, personInfo.address, personInfo.city,
					personInfo.state, personInfo.zip, personInfo.phoneNo, personInfo.email, personInfo.addressBookName,
					personInfo.addressBookType, personInfo.date);
		});
	}

	public void addContactToAddressBook(String firstName, String lastName, String address, String city, String state,
			String zip, String phoneNumber, String email, String addressBookName, String addressBookType,
			LocalDate date) {
		contactList.add(addressBookDBServiceNew.addContact(firstName, lastName, address, city, state, zip, phoneNumber,
				email, addressBookName, addressBookType, date));
	}

	public void addContactsWithThreads(List<PersonInfo> contactDataList) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<>();
		contactDataList.forEach(personInfo -> {
			Runnable task = () -> {
				contactAdditionStatus.put(personInfo.hashCode(), false);
				log.info("Contact being added : " + Thread.currentThread().getName());
				this.addContactToAddressBook(personInfo.firstName, personInfo.lastName, personInfo.address,
						personInfo.city, personInfo.state, personInfo.zip, personInfo.phoneNo, personInfo.email,
						personInfo.addressBookName, personInfo.addressBookType, personInfo.date);
				contactAdditionStatus.put(personInfo.hashCode(), true);
				log.info("Contact added : " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, personInfo.firstName);
			thread.start();
		});
		while (contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {}
		}
		log.info("" + this.contactList);
	}

	public void addContactToAddressBook(PersonInfo personInfo) {
		contactList.add(personInfo);
	}

	public long countEntries() {
		return contactList.size();
	}

	public void deleteContact(String firstName) {
		PersonInfo personInfo = this.getContactData(firstName);
		contactList.remove(personInfo);
	}

	public void writeAddressBookData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO)) {
			try {
				new AddressBookFileIO().writeDataFileIO(contactList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}