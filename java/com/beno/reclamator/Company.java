package com.beno.reclamator;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseReader;

import java.util.ArrayList;

/**
 * Created by Bruno on 08/09/2015.
 */
public class Company {
	public static ArrayList<Company> companies = new ArrayList<>();

	public static void reloadCompanies() {
		DatabaseReader reader = new DatabaseReader(new DatabaseHelper(MainActivity.context));
		companies.clear();
		companies.addAll(reader.queryCompanies());
		reader.close();
	}

	public String name;
	public String phoneNumber;
	public String email;
	public String address;
	public String website;

	public Company() { }

	public Company(String name, String phoneNumber, String email, String address, String website) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.website = website;
	}
}
