package com.stl.gestion_contacts;

public enum ContactComparator {
    Alphabetic("Nom"),
    LastMsg("Dernier msg");

    private String text;

    ContactComparator(String text) {
        this.text = text;
    }
    public static String[] getTextValues() {
        String [] textValues = new String[values().length];
        int i = 0;
        for (ContactComparator comp : values()) {
            textValues[i] = comp.text;
            i++;
        }
        return textValues;
    }

    public String toString() {
        return text;
    }
}