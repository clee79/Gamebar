package com.example.a4322_term_project;

public class QRWorker {
    private String restaurant_location;
    private String table_location;

    void QRWorker (String res, String tab ){
        restaurant_location = res;
        table_location = tab;

    }

    public String GetLocation(){
        return restaurant_location;
    }

    public String GetTable(){
        return table_location;
    }
}
