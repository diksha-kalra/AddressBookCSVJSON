package com.addressbook.opencsv.gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AddressBookDBServiceNew {
	private static AddressBookDBServiceNew addressBookDBServiceNew;

	private AddressBookDBServiceNew() {
	}

	static AddressBookDBServiceNew getInstance() {
		if (addressBookDBServiceNew == null) {
			addressBookDBServiceNew = new AddressBookDBServiceNew();
		}
		return addressBookDBServiceNew;
	}

	public PersonInfo addContact(String firstName, String lastName, String address, String city, String state,
			String zip, String phoneNumber, String email, String addressBookName, String addressBookType,
			LocalDate date) {
		Connection connection = null;
		PersonInfo personInfo = null;
		try {
			connection = AddressBookDBService.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO address_book_dict (address_book_name,address_book_type) VALUES('%s','%s')",
					addressBookName, addressBookType);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 0)
				throw new SQLException();
		} catch (SQLException e) {
			System.out.println(e + "cannot insert into address_book");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO contact_details (first_name,last_name,address_book_name,address,city,state,zip,"
							+ "phone_number,email,date_added) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					firstName, lastName, addressBookName, address, city, state, zip, phoneNumber, email, date);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 0)
				throw new SQLException();
			if (rowAffected == 1)
				personInfo = new PersonInfo(firstName, lastName, address, city, state, zip, phoneNumber, email,
						addressBookName, addressBookType, date);
		} catch (SQLException e) {
			System.out.println(e + "cannot insert into contact details");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return personInfo;
	}
}
