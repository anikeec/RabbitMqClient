/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author apu
 */
public class SymbolCounter {
    
    public Map<Character,Integer> symbolMap = new HashMap<>();
    
    public Map<Character,Integer> countSymbols(String text) {
        
        symbolMap.clear();
        
        int fullLength = text.length();
       
        Character symbol;
        int amount = 0;
        for(int i=0; i<text.length(); i++) {
            symbol = text.charAt(i);            
            if(symbolMap.containsKey(symbol)) {
                amount = symbolMap.get(symbol);                              
            } else {
                amount = 0;
            }
            amount++;  
            symbolMap.put(symbol, amount);
        }
        
        return symbolMap;
    }
    
    public String getStringResults(Map<Character,Integer> symbolMap) {
        Set<Map.Entry<Character, Integer>> set = symbolMap.entrySet();
        Iterator<Map.Entry<Character, Integer>> it = set.iterator();
        Map.Entry<Character, Integer> entry;
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append("\n");            
        }
        return sb.toString();
    }
    
}
