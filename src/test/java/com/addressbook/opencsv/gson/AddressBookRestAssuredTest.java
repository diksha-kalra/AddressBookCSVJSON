package com.addressbook.opencsv.gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Logger;

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
	public void givenNewContact_WhenAdded__ShouldMatch(){
		AddressBookService addressBookService;
		PersonInfo[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		PersonInfo personInfo = null;
		personInfo = new PersonInfo("naman","kalra","delhi","delhi","delhi","110019","9899787878","naman@gmail.com","contacts","family",LocalDate.now());
		Response response = addContactToJsonServer(personInfo);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		personInfo = new Gson().fromJson(response.asString(), PersonInfo.class);
		addressBookService.addContactToAddressBook(personInfo);
		long entries = addressBookService.countEntries();
		Assert.assertEquals(2, entries);
	}
	
	@Test
	public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		PersonInfo[] arrayOfContacts = getContactList();
		AddressBookService addressBookService;
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		long entries = addressBookService.countEntries();
		Assert.assertEquals(2, entries);
	}
}
