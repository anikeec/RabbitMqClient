/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class TextUtils {
    
    public static List<String> sliceText(String text, int amount) {
        int fullLength = text.length();
        int fragmentLength = fullLength/amount;
        List<String> list = new ArrayList<>();
        for(int i=0; i<amount; i++) {
            list.add(text.substring(i*fragmentLength, (i+1)*fragmentLength));
        }
        return list;
    }
    
}
