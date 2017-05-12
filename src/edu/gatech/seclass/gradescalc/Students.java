/**
 * 
 */
package edu.gatech.seclass.gradescalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Students {
	private FileInputStream file;
	private XSSFWorkbook workbook;
	
	private HashSet<Student> studentsRoster = new HashSet<Student>();
	
	private static final int STUDENTS_INFO_SHEET = 0;
	private static final int TEAMS_SHEET = 1;
	
	private static final int NAME_CELL      = 0;
	private static final int GTID_CELL      = 1;
	private static final int EMAIL_CELL     = 2;
	private static final int C_CELL         = 3;
	private static final int CPLUSPLUS_CELL = 4;
	private static final int JAVA_CELL      = 5;
	private static final int JOB_CELL       = 6;
	

	/**
	 * Students constructor opens the passed in name of the xlsx file and populates
	 * the local hashmaps
	 * 
	 * @param xlsx file name string
	 */
	public Students(String arg) {
      try {
		file = new FileInputStream(new File(arg));
		
		//Get the workbook instance for XLS file 
		workbook = new XSSFWorkbook (file);
		getStudentInfo();
	  } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
	}
	             
	/**
	 * getStudentInfo populates the student roster hashset
	 * 
	 */	
	public void getStudentInfo() {
  
	//Get first sheet from the workbook
	XSSFSheet sheet = workbook.getSheetAt(STUDENTS_INFO_SHEET);
	
	//Get iterator to all the rows in current sheet
	Iterator<Row> rowIterator = sheet.iterator();

	//Get iterator to all cells of current row	
    while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        Student current_student = new Student();
        
        // skip the first 2 rows
        // row 1 is the column letters
        // row 2 is the column headings
        if (row.getRowNum() < 1) {
        	continue;
        }
        
        //assume cell count per row is a fixed quantity
        // the first cell is the row number
        current_student.setName(row.getCell(NAME_CELL).getStringCellValue());        
        current_student.setGtid(String.valueOf((int)row.getCell(GTID_CELL).getNumericCellValue()));        
        current_student.setEmail(row.getCell(EMAIL_CELL).getStringCellValue());        
        current_student.setYears_of_C((int)row.getCell(C_CELL).getNumericCellValue());       
        current_student.setYears_of_cPlusPlus((int)row.getCell(CPLUSPLUS_CELL).getNumericCellValue());       
        current_student.setYears_of_java((int)row.getCell(JAVA_CELL).getNumericCellValue()); 
        
        switch(row.getCell(JOB_CELL).getStringCellValue()) {
          case ("Y"):
        	  current_student.setJob_experience(true);
            break;
          case ("N"):
        	  current_student.setJob_experience(false);
            break;
         }   
        
        current_student.setTeam(getTeamInfo(current_student.getName()));
        studentsRoster.add(current_student);  
        
      }
      try {
		file.close();
	  } catch (IOException e) {
		e.printStackTrace();
	  }
      	
	}

	/**
	 * getTeamInfo adds the team name to each student
	 * 
	 * @param student name
	 * @return team name
	 */
  public String getTeamInfo(String name) {
	String team = ""; 
	boolean name_found = false;
	  
	//Get first sheet from the workbook
	XSSFSheet sheet = workbook.getSheetAt(TEAMS_SHEET);
	
	//Get iterator to all the rows in current sheet
	Iterator<Row> rowIterator = sheet.iterator();

	//Get iterator to all cells of current row	
    while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        Iterator <Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();
          if(cell.getStringCellValue().contains("Team")) {
        	  if(!name_found) {
        		  team = cell.getStringCellValue();
        	  }
          }
          if(cell.getStringCellValue().contains(name)) {
        	  name_found = true;
        	  break;
          }
        }
      }
      try {
		file.close();
	  } catch (IOException e) {
		e.printStackTrace();
	  }
      
    if(name_found)  {
	 return team;
	} else {
	 return "NO TEAM";
	}
  }

	public HashSet<Student> getStudentsRoster() {
		return studentsRoster;
	}

  
  
}  

