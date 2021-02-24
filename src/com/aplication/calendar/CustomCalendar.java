package com.aplication.calendar;

/**
 * This class checks date and format and return if it is valid or no. 
 * 
 * @author Jhon Eder mosquer
 * @version 1.1
 * @since 10-14-2020
 */


public class CustomCalendar {
	
	/**
	 * check format of date yyyy-mm-dd
	 * @param String date
	 * @return if the date is valid
	 */
	public static boolean checkDate(String date) {
		//fortmat {yyyy-mm-dd}
		boolean check = false;
		try { //extract day, month and year
			int  year = Integer.parseInt(date.substring(0, 4));
			int  month = Integer.parseInt(date.substring(5, 7));
			int  day = Integer.parseInt(date.substring(8,10 ));
			check = checkDate(year, month, day);
			//System.out.print(year +"-"+month + "-"+ day+ " :"+check);
		}catch (NumberFormatException e) {
			//e.printStackTrace();
			System.out.println("\nyou enter wrong date format! Example: 20 june of 2010 is 2010-06-20 ");
			return false;
		}catch (StringIndexOutOfBoundsException e) {
			//e.printStackTrace();
			System.out.println("\nyou enter wrong date format! Example: 20 june of 2010 is 2010-06-20 ");
			return false;
		}		
	
		return check;
	}
	
	/**
	 * if the date is a valid day of the year. 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean checkDate(int year, int month, int day) {
		if ((year > 1600 && year <= 2020) == false ) {
			System.out.println("Year must be between 1600 and 2020");
			return false;
		}
		
		if ((month >= 1 && month<=12) == false) {
			System.out.println("Month must be between 1 and 12");
			return false;
		}
		//the number of days in the month of the year
		if (day>=1 && (day <= Calendar.daysInMonth(month, year)) == false) {
			System.out.println("Wrong day in this month of year");
			return false;
		}
		return true;
	}	
	
}

/*//from  w  w  w  .j  av  a  2  s  . c  o m
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

  class  Calendar {
	private final static int[][] daysInMonth = { { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
            { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };

    /**
     * return the number of days in the month of the year.  Note the 
     * result is not valid for years less than 1600 or so.  
     * @param month the month (1,2,...,12)
     * @param year the year (valid years are 1000-8999)
     * @return the number of days in the month.
     */
    public static int daysInMonth(int month, int year) {
        return daysInMonth[isLeapYear(year) ? 1 : 0][month];
    }

    /**
     * return the leap year for years 1581-8999.
     * @param year the four-digit year.
     * @return true if the year is a leap year.
     */
    public static boolean isLeapYear(int year) {
        if (year < 1000)
            throw new IllegalArgumentException("year must be four-digits");
        return (year % 4) == 0 && (year % 100 != 0 || year % 400 == 0);
    }

}