package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable, Printable{

    private String name;

    public Group(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getText() {
        return name;
    }

}
