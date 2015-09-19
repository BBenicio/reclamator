package com.beno.reclamator;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.beno.reclamator.database.DatabaseHelper;
import com.beno.reclamator.database.DatabaseReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

//Parcelable is implemented so the class can be passed between intents
public class Entry implements Parcelable {
    public static final ArrayList<Entry> entries = new ArrayList<>();

    public static void reloadEntries() {
        DatabaseReader reader = new DatabaseReader(new DatabaseHelper(MainActivity.context));
        entries.clear();
        entries.addAll(reader.queryEntries());
        reader.close();
    }

    private long time;
    public String company;
    public String problem;
    public String operator;
    public String protocol;
    public String observations;

    public Entry(String company, String problem, String operator, String protocol, String observations) {
        this(company, problem, operator, protocol, observations, 0);
    }

    public Entry(String company, String problem, String operator, String protocol, String observations, long ms) {
        if (ms != 0)
            time = ms;
        else {
            time = GregorianCalendar.getInstance().getTimeInMillis();
        }

        this.company = company;
        this.problem = problem;
        this.operator = operator;
        this.protocol = protocol;
        this.observations = observations;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        Date date = new Date(time);
        return DateFormat.getDateFormat(MainActivity.context).format(date) + " - " + DateFormat.getTimeFormat(MainActivity.context).format(date);
    }

    // Parcelable methods

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel source) {
            return new Entry(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readLong());
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(company);
        dest.writeString(problem);
        dest.writeString(operator);
        dest.writeString(protocol);
        dest.writeString(observations);
        dest.writeLong(time);
    }
}
