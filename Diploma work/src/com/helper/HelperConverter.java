package com.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.MaskFormatter;

public class HelperConverter {
	private static SimpleDateFormat formatterD = new SimpleDateFormat("dd-MM-yyyy");
	//  Преобразование из формата БД в формат Java
	public static String getDateToJava(java.sql.Date dat){
		if (dat==null)
			return null;
		else
			return formatterD.format(dat);
	}
	
	//  Преобразование из формата Java в формат БД
	public static String getDateToSQL(java.util.Date dat){
		if (dat==null)
			return null;
		else
			return formatterD.format(dat);
	}
	
	// Преобразование даты SQL в дату Java
	public static java.util.Date convertFromSQLDateToJAVADate(java.sql.Date sqlDate) {
		java.util.Date javaDate = null;
		if (sqlDate != null) 
			javaDate = new Date(sqlDate.getTime());
		return javaDate;
	}
	
	// Преобразование даты Java в дату SQL
	public static java.sql.Date convertFromJavaDateToSQLDate(java.util.Date dat) {
		java.sql.Date sqlDate = null;
		if (dat!=null)
			sqlDate=new java.sql.Date(dat.getTime());
		return sqlDate;  
	}
    
	// Метод формирования маски ввода даты
	public static MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
			System.exit(-1);
		}
		return formatter;
	}
}

