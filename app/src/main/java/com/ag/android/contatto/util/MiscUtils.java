package com.ag.android.contatto.util;

import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ag.android.contatto.Gender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class MiscUtils {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate date2LocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isOver18(Date birthday) {
        return (Period.between(date2LocalDate(birthday), date2LocalDate(new Date())).getYears() >= 18);
    }

    public static Date str2date(String dateStr) throws ParseException {
        return DATE_FORMAT.parse(dateStr);

    }

    public static String date2str(Date date) {
        return DATE_FORMAT.format(date);
    }


    public static Gender str2gender(String genderStr) {
        String genderStrLower = genderStr.toLowerCase();

        if ("man".equals(genderStrLower) || "men".equals(genderStrLower))
            return Gender.MALE;

        if ("woman".equals(genderStrLower) || "women".equals(genderStrLower))
            return Gender.FEMALE;

        if ("nonbinary".equals(genderStrLower) || "non-binary".equals(genderStrLower))
            return Gender.NONBINARY;

        //try with enum constant names
        String genderStrUpper = genderStr.toUpperCase();
        try {
            return Gender.valueOf(genderStrUpper);
        } catch (IllegalArgumentException iae) {
            //Log.e(TAG, "Unknown gender: " + genderStr);
            return null;
        }
    }

    public static String gender2str(Gender gender, boolean isPlural) {
        switch (gender) {
            case MALE:
                return (isPlural ? "men" : "man");
            case FEMALE:
                return (isPlural ? "women" : "woman");
            case NONBINARY:
                return (isPlural ? "nonbinary people" : "nonbinary person");
            default:
                return "???";
        }
    }

    public static String readTextFile(AssetManager am, String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(am.open(path)));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            return sb.toString();

        } finally {
            br.close();
        }
    }
}
