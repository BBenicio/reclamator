package com.beno.reclamator;

import android.os.Parcel;
import android.os.Parcelable;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseReader;

import java.util.ArrayList;

public class Company implements Parcelable {
	public static ArrayList<Company> companies = new ArrayList<>();

	public static void reloadCompanies() {
		DatabaseReader reader = new DatabaseReader(new DatabaseHelper(MainActivity.context));
		companies.clear();
		companies.addAll(reader.queryCompanies());
		reader.close();
	}

	public static Company getFromName(String name) {
		for (Company c : companies) {
			if (c.name.toLowerCase().equals(name.toLowerCase())) {
				return c;
			}
		}
		return null;
	}

	public String name;
	public String phoneNumber;
	public String email;
	public String address;
	public String website;

	public Company(String name, String phoneNumber, String email, String address, String website) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.website = website;
	}

	// Parcelable methods

	protected Company(Parcel in) {
		name = in.readString();
		phoneNumber = in.readString();
		email = in.readString();
		address = in.readString();
		website = in.readString();
	}

	public static final Creator<Company> CREATOR = new Creator<Company>() {
		@Override
		public Company createFromParcel(Parcel in) {
			return new Company(in);
		}

		@Override
		public Company[] newArray(int size) {
			return new Company[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(phoneNumber);
		dest.writeString(email);
		dest.writeString(address);
		dest.writeString(website);
	}
}
