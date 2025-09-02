package com.khumbu.dailyplanner.utils;

public class EncryptionUtil {

    private static String key="abc#1989";
    public static String encrypt(String plainText){
        return plainText+key;

    }

    public static String decrypt(String cipher){

        return cipher.substring(0,(cipher.length()-key.length()));
    }
}
