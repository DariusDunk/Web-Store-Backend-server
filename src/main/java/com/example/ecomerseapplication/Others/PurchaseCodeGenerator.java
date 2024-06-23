package com.example.ecomerseapplication.Others;


import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

public class PurchaseCodeGenerator {

    public static String generateCode(LocalDateTime timeStamp)  {

        int year = timeStamp.getYear();
        int month = timeStamp.getMonthValue();
        int day = timeStamp.getDayOfMonth();
        int hour = timeStamp.getHour();
        int minute =timeStamp.getMinute();

        String randomDigits = new Random().ints(3, 1, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
        // Combine timestamp and random digits with formatting
        return String.format("%d%02d%s%02d",hour, month*7+minute+ day*3,randomDigits,year);
    }


}