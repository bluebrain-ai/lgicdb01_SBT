package com.bluescript.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import java.util.*;

import org.springframework.stereotype.Component;

@Data
@Component

public class Db2Customer {
    private String db2Firstname;
    private String db2Lastname;
    private String db2Dateofbirth;
    private String db2Housename;
    private String db2Housenumber;
    private String db2Postcode;
    private String db2PhoneMobile;
    private String db2PhoneHome;
    private String db2EmailAddress;

    public String toFixedWidthString() {
        return db2Firstname + db2Lastname + db2Dateofbirth + db2Housename + db2Housenumber + db2Postcode
                + db2PhoneMobile + db2PhoneHome + db2EmailAddress;
    }

}