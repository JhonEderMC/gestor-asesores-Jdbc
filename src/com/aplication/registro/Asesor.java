package com.aplication.registro;

import com.aplication.calendar.*;
import com.aplication.preferences.*;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.prefs.Preferences;




public class Asesor {
	
	public Asesor() {
		loadDriverSQL();
		menu();
	}
	
	
	/** The static value initialization block here is used as:
	 * <p>
	 * separator print line, menu message and others constants messages.
	 * </p>	 
	 */	
	private static final String SEPARATOR_LINE, MENU, ADD_NEW_ASESOR, SEARCH_ASESOR, LIST_ASESORES, UPDATE_ASESOR, DELETE_ASESOR;
	
	static {
		SEPARATOR_LINE = "\n------------------------------------------------------------------";
		MENU = "\nMenu de opciones \n\t1) Agregar nuevo asesor "
				+ "\n\t2) Buscar asesor \n\t3) Listar asesores"
				+ "\n\t4) Actualizar asesor \n\t5) Eliminar asesor"
				+ "\n\t6) Salir"; ADD_NEW_ASESOR = "\n\t Add New Asesor:\n";
				SEARCH_ASESOR= "\n\tSearch an Asesor:\n"; LIST_ASESORES = "\n\tPrint list Asesores:\n";
				UPDATE_ASESOR = "\n\tUpdate an Asesor:\n"; DELETE_ASESOR = "\n\tDelete an Asesor:\n";
			}
	
	/**
	 * Print menu of options and choose one. 
	 */
	private void menu() {
		boolean condition = true;
		int option = 99;
		do {
			System.out.println(SEPARATOR_LINE);
			System.out.print(MENU);
			try {
				System.out.print("\nEnter a option: ");
				option = inputInteger();
			} catch (InputMismatchException e) {
				System.out.println("Please enter a integer number");
				continue; //next iteration
			}			
			 condition = menuSwitch(option);
		} while (condition);		
	}
	
	/**
	 * Choose a option of menu and call to method 
	 * @param integer number that is a option
	 * @return true or false (continue or exit)
	 */
	private boolean menuSwitch(int option) {
		System.out.println(SEPARATOR_LINE);
		switch (option) {
		case 1:
			System.out.print(ADD_NEW_ASESOR);
			addAsesor();
			return true;
		case 2:
			System.out.print(SEARCH_ASESOR);
			searchAsesor();
			return true;
		case 3:
			System.out.print(LIST_ASESORES);
			printListAsesores();
			return true;
		case 4:
			System.out.print(UPDATE_ASESOR);
			updateAsesor();
			return true;
		case 5:
			System.out.print(DELETE_ASESOR);
			deleteAsesor();
			return true;
		case 6: //exit
			System.out.print("\nGoodBye!");
			return false;

		default:
			System.out.print("\nPlease enter a valid option");
			return true;
		}		
	}
	
	
	/**
	 * insert new asesor into database
	 * it asks asesor's data: name, phone, bithdate...
	 */
	private void addAsesor() {
		//input data
		String name, birthDate, gender, client, heardQuarter;
		int cedula, phone;
		System.out.print("Enter Asesor's name: ");
		name = inputString();
		if (name.isBlank() || name == null) {
			System.out.print("Please Enter a name");
			return;//back to menu
		}
		
		System.out.print("Enter "+name+" cedula: ");		
		try {
			cedula = inputInteger();
		}catch (InputMismatchException e) {
			System.out.println("Please enter only  integer numbers");
			cedula = 0;
			return; //back to menu
		}		
				
		System.out.print("Enter "+ name +" phone: ");		
		try{
			phone = inputInteger();
		}catch (InputMismatchException e) {
			System.out.println("Please enter only  integer numbers");
			phone =0;
			return; //back to menu
		}		
		
		System.out.print("Enter "+ name +" birth date(yyyy-mm-dd):  ");
		birthDate = inputString();
		// Check format of date and if is a calendar day or no.
		if (CustomCalendar.checkDate(birthDate) == false) return; //back to menu
		
		System.out.print("Enter "+ name +" gender (masculino, femenino, otro): ");
		gender = inputString();		
		System.out.print("Enter client name "+ name +" works for: " );
		client = inputString();
		System.out.print("Enter headquarters where  does "+ name+" work: " );
		heardQuarter = inputString();
		
		// format sql
		String sql = "INSERT INTO asesor (NOMBRE, CEDULA, TELEFONO, FECHA_DE_NACIMIENTO, GENERO, CLIENTE_DONDE_TRABAJA, "
				+ "SEDE_DONDE_TRABAJA) VALUES (? ,? ,? ,? ,? ,? ,? )";
		//try with attributes will automatically handle closes (finally)		
		try(Connection connection = connectToDataBase();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, name.toUpperCase());
			ps.setInt(2, cedula);
			ps.setInt(3, phone);
			ps.setString(4, birthDate);
			ps.setString(5, gender.toUpperCase());
			ps.setString(6, client.toUpperCase());
			ps.setString(7, heardQuarter.toUpperCase());
			//System.out.print(ps); //looks as is the sql
			int response = ps.executeUpdate();
			if(response>0) System.out.println("\nThe Asesor was saved");
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("\nThe Asesor could not be saved in the database");
		}		
	}
	
	/**
	 * Search an asesor with an  identification (cedula)
	 * after prints.
	 */
	private void searchAsesor() {
		int cedula =0;
		System.out.print("Enter a cedula: ");		
		try {
			cedula = inputInteger();
		}catch (InputMismatchException e) {
			System.out.println("Please enter only  integer numbers");			
			return; //back to menu
		}		
		
		boolean foundit = searchCedula(cedula); //search by ID
		String str = (foundit == true) ? "" : "\nThe asesor(cedula) was not found";
		System.out.println(str);				
	}
	
	/**
	 * print all data from database table
	 */
	private void printListAsesores() {
		boolean foundit = false;
		String sql = "SELECT * FROM asesor";
		try(Connection connection = connectToDataBase(); //connect to data
				PreparedStatement ps = connection.prepareStatement(sql)){
			ResultSet rs = ps.executeQuery();
			foundit = (printData(rs) == true) ? true : false; //print data if exits
		} catch (SQLException e) {			
			//e.printStackTrace();
		}
		String str = (foundit == true) ? "": "\nThe table is empty";
		System.out.print(str);				
	}
	
	/**
	 * Update asesor data from database
	 */
	private void updateAsesor() {		
		System.out.print("Enter asesor's cedula to search: ");
		String name, birthDate, gender, client, heardQuarter;
		int cedula, phone;
		try {
			cedula = inputInteger();
		}catch (InputMismatchException e) {
			System.out.println("Please enter only  integer numbers");
			cedula = 0;
			return; //back to menu
		}
		//search if this identification exits 
		boolean found = searchCedula(cedula);
		
		if (found) { //update 
			System.out.print("\nEnter new name: ");
			name = inputString();
			if (name.isBlank() || name == null) {
				System.out.print("Please Enter  a name");
				return;//back to menu
			}						
					
			System.out.print("Enter new phone: ");
			
			try{
				phone = inputInteger();
			}catch (InputMismatchException e) {
				System.out.println("Please enter only  integer numbers");
				phone =0;
				return; //back to menu
			}		
			
			System.out.print("Enter new birth date(yyyy-mm-dd):  ");
			birthDate = inputString();
			// Check format of date and if is a calendar day or no.
			if (CustomCalendar.checkDate(birthDate) == false) return; //back to menu
			
			System.out.print("Enter  new gender (masculino, femenino, otro): ");
			gender = inputString();		
			System.out.print("Enter client new  name works for: " );
			client = inputString();
			System.out.print("Enter new name headquarters where  does you work: " );
			heardQuarter = inputString();
			
			String sql = "UPDATE asesor SET NOMBRE = ? , TELEFONO = ?,  FECHA_DE_NACIMIENTO = ?, GENERO = ?, CLIENTE_DONDE_TRABAJA = ?,"
					+ "SEDE_DONDE_TRABAJA = ?  WHERE CEDULA = ?";
			try (Connection connection = connectToDataBase();
					PreparedStatement ps = connection.prepareStatement(sql)){
				ps.setString(1, name.toUpperCase());				
				ps.setInt(2, phone);
				ps.setString(3, birthDate);
				ps.setString(4, gender.toUpperCase());
				ps.setString(5, client.toUpperCase());
				ps.setString(6, heardQuarter.toUpperCase());
				ps.setInt(7, cedula);				
				//System.out.print(ps); //looks as is the sql
				int response = ps.executeUpdate();
				if(response>0) System.out.println("\nThe Asesor was update");
				
			} catch (SQLException e) {				
				//e.printStackTrace();
				System.out.println("\nThe Asesor was not update");
			}
		}else { //not found
			System.out.print("The cedula was not found: "+cedula);
		}
		
	}
	
	/**
	 * delete asesor from database for this uses the identification of asesor.	 * 
	 */
	private void deleteAsesor() {
		System.out.print("Enter asesor's cedula to search: ");
		int cedula;
		try {
			cedula = inputInteger();
		}catch (InputMismatchException e) {
			System.out.println("Please enter only  integer numbers");
			cedula = 0;
			return; //back to menu
		}
		//search if this identification exits 
		boolean found = searchCedula(cedula);
		
		if (found) {
			String sql = "DELETE FROM asesor WHERE CEDULA = ?";
			try(Connection connection = connectToDataBase();
					PreparedStatement ps = connection.prepareStatement(sql)){
				ps.setInt(1, cedula);
				int response = ps.executeUpdate();
				if (response>0) System.out.print("The asesor was deleted!");
			}catch (SQLException e) {
				//e.printStackTrace();
				System.out.println("The asesor was not deleted. SQL failed!");
			}
		}else {	//not found
			System.out.print("The cedula was not found: "+cedula);
		}		
		
	}
	
	
	/**
	 * Search asesor( cedula) from database
	 * @param cedula
	 * @return if found it or not
	 */
	private boolean searchCedula(int cedula) {
		boolean foundit = false;
		String sql = "SELECT * FROM asesor WHERE CEDULA = ? ";
		try(Connection connection = connectToDataBase();
				PreparedStatement ps = connection.prepareStatement(sql);){
			ps.setInt(1, cedula);
			ResultSet rs = ps.executeQuery();
			foundit = printData(rs); //print data from query
		} catch (SQLException e) {			
			//e.printStackTrace();
		} return foundit;
	}
	
	/**
	 * Print data from query
	 * @param rs 
	 * @throws SQLException 
	 * @return if print or not
	 */
	private boolean printData(ResultSet rs) throws SQLException {
		boolean m = false;
		//format to print
		String format = "\n\tNombre: %s \tCedula: %d \tTelefono: %d \tFecha de nacimiento: %s "
				+ "\tGenero: %s \tCliente donde trabaja: %s \tSede donde trabaja: %s \tEdad: %d ";
		while (rs.next() == true) {
			String name = rs.getString("NOMBRE");
			int cedula = rs.getInt("CEDULA");
			int phone = rs.getInt("TELEFONO");
			String birthDate = rs.getString("FECHA_DE_NACIMIENTO");
			String gender = rs.getString("GENERO");
			String client = rs.getString("CLIENTE_DONDE_TRABAJA");
			String heardQuarter = rs.getString("SEDE_DONDE_TRABAJA");
			int age = rs.getInt("EDAD");
			System.out.print(String.format(format, name, cedula, phone, birthDate, gender, client, heardQuarter, age));
			m = true;
		}
		return m;
	}
	
	
	/**@return connection with DBMS 	 */
	private Connection connectToDataBase() {
		PreferencesDBMS dbms = new PreferencesDBMS();
		try{
			//don't close connection before tries sql query 
			Connection connection = DriverManager.getConnection(dbms.getUrl(), dbms.getUser(), dbms.getPassword());		
			return connection;
		} catch (SQLException e) {		
			e.printStackTrace();
			System.out.println("\nThe connection to database has failed!");
			return null;			
		}
	}

	/*Load driver name in memory */
	private void loadDriverSQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//package name where driver is
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error to load driver");
		}
	}
	
	/*@return String number of keyboard input	 */
	private String inputString() {
		Scanner k = new Scanner(System.in);		
		return k.nextLine();
	}
	
	/*@return integer number of keyboard input	 */
	private int inputInteger() {
		Scanner k = new Scanner(System.in);
		return k.nextInt();		
	}	
	

}
