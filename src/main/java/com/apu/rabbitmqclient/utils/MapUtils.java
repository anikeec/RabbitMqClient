/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author apu
 */
public class MapUtils {
    
    private final Map<Character,Integer> symbolMap = new HashMap<>();
    
    public Map<Character, Integer> addMap(Map<Character, Integer> addMap) {
        
        Set<Map.Entry<Character, Integer>> set = addMap.entrySet();
        Iterator<Map.Entry<Character, Integer>> it = set.iterator();
        Map.Entry<Character, Integer> entry;
        Character symbol;
        int amount;
        while(it.hasNext()) {
            entry = it.next();
            symbol = entry.getKey();
            amount = entry.getValue();
            if(symbolMap.containsKey(symbol)) {                
                symbolMap.put(symbol, symbolMap.get(symbol) + amount);
            } else {
                symbolMap.put(symbol, amount);
            }            
        }
        
        return symbolMap;
    }
    
}
