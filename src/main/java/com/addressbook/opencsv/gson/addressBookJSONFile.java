package com.addressbook.opencsv.gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;

public class addressBookJSONFile {
	private static String jsonFile = "D:\\eclipse\\AddressBookCSVJSON\\src\\test\\addressBook.json";

	public static void main(String[] args) throws ParseException {
		addressBookJSONFile addressBook = new addressBookJSONFile();
		try {
			addressBook.writeDataIntoJSON();
			addressBook.readDataFromJSON();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void writeDataIntoJSON() throws IOException {
		Writer writer = Files.newBufferedWriter(Paths.get(jsonFile));
		List<PersonInfo> listOfContacts = new ArrayList<>();
		listOfContacts.add(new PersonInfo("diksha", "kalra", "Kalkaji", "Delhi", "Delhi", "110019", "9899151876",
				"kalradiksha109@gmail.com"));
		listOfContacts.add(new PersonInfo("kavita", "mehta", "Pamposh Enclave", "Delhi", "Delhi", "110017",
				"9899121213", "kavita@gmail.com"));
		listOfContacts.add(
				new PersonInfo("asfar", "hussain", "chitrol", "pak", "pak", "546868", "9899123412", "asfar@gmail.com"));
		Gson gson = new Gson();
		String json = gson.toJson(listOfContacts);
		writer.write(json);
		writer.close();
	}

	private void readDataFromJSON() throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new FileReader(jsonFile));
		Gson gson = new Gson();
		PersonInfo[] personInfoArray = gson.fromJson(br, PersonInfo[].class);
		for (PersonInfo p : personInfoArray) {
			System.out.println(p);
		}
	}
}
