package com.tanhua.commons.utils;

import java.util.Random;

public class RandomStringUtils {

    public static String randomNumeric(int size) {
        String[] arr = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"
        ,"g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F"
                ,"G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < size; j++) {
            int i = random.nextInt(62);
            s.append(arr[i]);
        }
        return s.toString();
    }

}
