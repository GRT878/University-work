package com.helper;

public class ErrorHandler {
	//[Sybase][ODBC Driver][SQL Anywhere]Column 'Kod_m' in table 'Mater' cannot be NULL
	//[Sybase][ODBC Driver][SQL Anywhere]Constraint 'ASA98' violated: Invalid value for column 'Kod_m' in table 'Mater'
	//[Sybase][ODBC Driver][SQL Anywhere]Constraint 'ASA99' violated: Invalid value for column 'Cena_ed' in table 'Mater'
	
	//[Sybase][ODBC Driver][SQL Anywhere]Column 'Kod_dol' in table 'Dolj' cannot be NULL
	//[Sybase][ODBC Driver][SQL Anywhere]Constraint 'ASA77' violated: Invalid value for column 'Kod_dol' in table 'Dolj'
	
	//[Sybase][ODBC Driver][SQL Anywhere]Primary key for table 'Sotrud' is not unique : Primary key value ('1')
	//[Sybase][ODBC Driver][SQL Anywhere]Column 'Kod_sotr' in table 'Sotrud' cannot be NULL
	//[Sybase][ODBC Driver][SQL Anywhere]Column 'Kod_dol' in table 'Sotrud' cannot be NULL
	//[Sybase][ODBC Driver][SQL Anywhere]Constraint 'ASA79' violated: Invalid value for column 'Kod_sotr' in table 'Sotrud'
	
	//[Sybase][ODBC Driver][SQL Anywhere]Primary key for table 'Vid_rab' is not unique : Primary key value (''Ёлектроника'')
	//[Sybase][ODBC Driver][SQL Anywhere]Column 'Vid_rab' in table 'Vid_rab' cannot be NULL
	
	
	
	public static String getError(String strIn){
		String strOut = null;
		if (strIn.contains("cannot be NULL"))
			strOut = "ќдно из об€зательных полей не заполнено";
		else if (strIn.contains("Invalid value"))
			strOut = "¬ведены некорректные данные";
		else if (strIn.contains("is not unique"))
			strOut = "«начение первичного ключа должно быть уникальным";
		else
			strOut = strIn;
		
		return strOut;
	}
}
