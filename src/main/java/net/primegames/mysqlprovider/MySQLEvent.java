/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.mysqlprovider;

public class MySQLEvent {

    /**
     * Create an event query that runs the provided perform SQL when the schedule SQL condition is met.
     */
    public static String event(String name, String schedule, String perform) {
        return String.format("CREATE EVENT IF NOT EXITS %s ON SCHEDULE %2s DO BEGIN %3s END;", name, schedule, perform);
    }

    public static String daily(String name, String perform) {
        return event(
                name + "_daily",
                "EVERY 1 DAY STARTS CURRENT_DATE + INTERVAL 1 DAY",
                perform
        );
    }

    public static String weekly(String name, String perform) {
        return event(name + "_weekly",
                "EVERY 1 WEEK STARTS CURRENT_DATE + INTERVAL 6 - WEEKDAY(CURRENT_DATE) DAY",
                perform);
    }

    public static String monthly(String name, String perform) {
        return event(name + "_monthly",
                "EVERY 1 MONTH STARTS CURRENT_DATE + INTERVAL 1 MONTH - INTERVAL (DAYOFMONTH(CURDATE()) - 1) DAY",
                perform);
    }

}
