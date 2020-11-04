package com.addressbook.opencsv.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.addressbook.opencsv.gson.AddressBookService.IOService;

public class AddressBookTest {

	@Test
	public void givenAddressBookWhenWrittenToFileShouldMatchEnteries() throws IOException{
		ArrayList<PersonInfo> person = new ArrayList<>();
		person.add(new PersonInfo("diksha", "kalra", "Kalkaji", "Delhi", "Delhi", "110019", "9899151876",
				"kalradiksha109@gmail.com"));
		person.add(new PersonInfo("kavita", "mehta", "Pamposh Enclave", "Delhi", "Delhi", "110017", "9899121213",
				"kavita@gmail.com"));
		AddressBookService addressBookService = new AddressBookService(person);
		addressBookService.writeAddressBookData(IOService.FILE_IO);
	}

	@Test
	public void readingAddressBook() {
		AddressBookService addressBookService = new AddressBookService();
		List<PersonInfo> entries = addressBookService.readContactData(IOService.FILE_IO);
	}
}
