package com.example.korisnik.newsproject.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static String currentDate(){
//        Date c = Calendar.getInstance().getTime();
//
//        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        String formattedDate = df.format(c);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = df.format(currentTime.getTime());

        return formattedDate;
    }
}
