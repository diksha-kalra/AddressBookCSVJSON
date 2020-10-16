package com.addressbook.opencsv.gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

public class writeAddressBookDataIntoCSVFile {
	private static String csvFilePath = "D:\\eclipse\\AddressBookCSVJSON\\src\\test\\addressBook.csv";

	public static void main(String[] args)
			throws CsvValidationException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		writeAddressBookDataIntoCSVFile addressbook = new writeAddressBookDataIntoCSVFile();
		try {
			addressbook.writeDataIntoCSV();
			addressbook.readDatatFromCSV();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void writeDataIntoCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Writer csvWriter = Files.newBufferedWriter(Paths.get(csvFilePath));
		List<PersonInfo> listOfContacts = new ArrayList<>();
		listOfContacts.add(new PersonInfo("diksha", "kalra", "Kalkaji", "Delhi", "Delhi", "110019", "9899151876",
				"kalradiksha109@gmail.com"));
		listOfContacts.add(new PersonInfo("kavita", "mehta", "Pamposh Enclave", "Delhi", "Delhi", "110017",
				"9899121213", "kavita@gmail.com"));
		ColumnPositionMappingStrategy<Object> mappingStrategy = new ColumnPositionMappingStrategy<Object>();
		mappingStrategy.setType(PersonInfo.class);
		String[] columns = new String[] { "first_name", "last_name", "address", "city", "state", "zip", "phone_no",
				"email" };
		mappingStrategy.setColumnMapping(columns);
		StatefulBeanToCsvBuilder<PersonInfo> builder = new StatefulBeanToCsvBuilder<PersonInfo>(csvWriter);
		StatefulBeanToCsv<PersonInfo> beanWriter = builder.build();
		beanWriter.write(listOfContacts);
		csvWriter.close();
	}

	public void readDatatFromCSV() throws IOException, CsvValidationException {
		Reader r = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVReader csv_reader = new CSVReader(r);
		String[] record;
		while ((record = csv_reader.readNext()) != null) {
			System.out.println(record[0]);
			System.out.println(record[1]);
			System.out.println(record[2]);
			System.out.println(record[3]);
			System.out.println(record[4]);
			System.out.println(record[5]);
			System.out.println(record[6]);
			System.out.println(record[7]);
		}
		csv_reader.close();
	}
}
