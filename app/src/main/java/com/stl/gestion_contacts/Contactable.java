package com.stl.gestion_contacts;

import java.util.Date;

interface Contactable {

    public static Date NO_DATE = new Date(01/01/1970);
    Date getLastMsgDate();

}
