package ru.praktikum.services.qa.scooter.data;

import ru.praktikum.services.qa.scooter.model.Courier;
import java.util.Random;

public class DataSetCourier {
    private static String[] logins = new String[] { "Ellen1", "Ellen4", "Ellen7", "Ellen10", "Ellen13", "Ellen16", "Ellen19", "Ellen22", "Ellen25", "Ellen28" };
    private static String[] passwords = new String[] { "01012025", "02012025", "03012025", "04012025", "05012025", "06012025", "07012025", "08012025", "09012025", "10012025"};
    private static String[] firstNames = new String[] {"Meggi", "William", "Gregory", "Polly", "Peter", "Joseph", "Molly", "Thomas", "Barbara", "Robin"};


    public static Courier dataSetValid(){
        int number = new Random().nextInt(10);
        return new Courier(logins[number], passwords[number], firstNames[number]);
    }

    public static Courier dataSetWithoutLogin(){
        int number = new Random().nextInt(10);
        return new Courier("", passwords[number], firstNames[number]);
    }

    public static Courier dataSetWithoutPassword(){
        int number = new Random().nextInt(10);
        return new Courier(logins[number], "", firstNames[number]);
    }

    public static Courier dataSetWithoutFirstName(){
        int number = new Random().nextInt(10);
        return new Courier(logins[number], passwords[number], "");
    }
}
