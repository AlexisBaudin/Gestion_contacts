package com.stl.gestion_contacts;

 public enum GroupComparator {
     Alphabetic("Nom"),
     NumberContacts("Nb contacts"),
     LastMsg("Dernier msg");

    private String text;

    GroupComparator(String text) {
        this.text = text;
    }

    public static String[] getTextValues() {
        String [] textValues = new String[values().length];
        int i = 0;
        for (GroupComparator comp : values()) {
            textValues[i] = comp.text;
            i++;
        }
        return textValues;
    }

     public String toString() {
         return text;
     }

}
