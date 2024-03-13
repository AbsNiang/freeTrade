package com.example.freetradewip;

import com.example.freetradewip.Data.DatabaseHandling;

public class Main {
    public static void main(String[] args){
        DatabaseHandling.initializeDatabase();
        HelloApplication.main(args);
    }
}
