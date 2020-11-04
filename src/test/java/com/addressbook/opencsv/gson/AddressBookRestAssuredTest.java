package com.addressbook.opencsv.gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddressBookRestAssuredTest {
	Logger log = Logger.getLogger(AddressBookRestAssuredTest.class.getName());

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public PersonInfo[] getContactList() {
		Response response = RestAssured.get("/contacts");
		log.info("Contacts entries in JSON Server :\n" + response.asString());
		PersonInfo[] arrayOfContacts = new Gson().fromJson(response.asString(), PersonInfo[].class);
		return arrayOfContacts;
	}

	public Response addContactToJsonServer(PersonInfo personInfo) {
		String contactJson = new Gson().toJson(personInfo);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}

	@Test
	public void givenContactDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		PersonInfo[] arrayOfContacts = getContactList();
		AddressBookService addressBookService;
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		long entries = addressBookService.countEntries();
		Assert.assertEquals(1, entries);
	}

	@Test
	public void givenContact_WhenAdded__ShouldMatch() {
		AddressBookService addressBookService;
		PersonInfo[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		PersonInfo personInfo = null;
		personInfo = new PersonInfo("naman", "kalra", "delhi", "delhi", "delhi", "110019", "9899787878",
				"naman@gmail.com", "contacts", "family", LocalDate.now());
		Response response = addContactToJsonServer(personInfo);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		personInfo = new Gson().fromJson(response.asString(), PersonInfo.class);
		addressBookService.addContactToAddressBook(personInfo);
		long entries = addressBookService.countEntries();
		Assert.assertEquals(2, entries);
	}

	@Test
	public void givenNewContactArray_WhenAdded_ShouldMatch() {
		AddressBookService addressBookService;
		PersonInfo[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		PersonInfo[] contactArrays = {
				new PersonInfo("rishabh", "singh", "agra", "agra", "agra", "110091", "9099121213", "singh@gmail.com",
						"mycontacts", "colleague", LocalDate.now()),
				new PersonInfo("yash", "sharma", "dwarka", "delhi", "delhi", "110119", "9899797878", "yash@gmail.com",
						"mycontacts", "colleague", LocalDate.now()) };
		for (PersonInfo personInfo : contactArrays) {
			Response response = addContactToJsonServer(personInfo);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			personInfo = new Gson().fromJson(response.asString(), PersonInfo.class);
			addressBookService.addContactToAddressBook(personInfo);
		}
		long entries = addressBookService.countEntries();
		Assert.assertEquals(4, entries);
	}

	@Test
	public void givenNewContact_WhenUpdated_ShouldMatch200Response() {
		AddressBookService addressBookService;
		PersonInfo[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		addressBookService.updateContactDetails("naman", "pamposh enclave");
		PersonInfo personInfo = addressBookService.getContactData("naman");
		String contactJson = new Gson().toJson(personInfo);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.put("/contacts/" + personInfo.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}
	
	@Test
	public void givenNewContactName_WhenRemoved_ShouldMatch200ResponseAndCount() {
		AddressBookService addressBookService;
		PersonInfo[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		PersonInfo personInfo = addressBookService.getContactData("Deepali");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/contacts/" + personInfo.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		addressBookService.deleteContact(personInfo.firstName);
		long entries = addressBookService.countEntries();
		Assert.assertEquals(3, entries);
	}
}