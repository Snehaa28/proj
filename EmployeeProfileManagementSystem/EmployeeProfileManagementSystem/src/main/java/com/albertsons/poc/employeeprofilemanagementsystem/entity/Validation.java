package com.albertsons.poc.employeeprofilemanagementsystem.entity;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.util.HashMap;
import java.util.List;



public class Validation {
    int counter;
    HashMap<String, List<String>> hashMap;

    public HashMap<String, List<String>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, List<String>> hashMap) {
        this.hashMap = hashMap;
    }

    public int getCounter() {
        return counter;
    }



    public void setCounter(int counter) {
        this.counter = counter;
    }
}
