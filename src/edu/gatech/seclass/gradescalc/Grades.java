/**
 * 
 */
package edu.gatech.seclass.gradescalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Grades {
	private FileInputStream file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private String dbName;

	private static final int ATTENDANCE_SHEET = 0;
	private static final int STUDENT_GRADES_SHEET = 1;
	private static final int CONTRIBS_SHEET = 2;
	private static final int TEAM_GRADES_SHEET = 3;

	private static final int ID_CELL = 0;
	private static final int ATTENDANCE_CELL = 1;

	private HashMap<String, Integer> attendance = new HashMap<String, Integer>();
	private HashMap<String, HashMap<String, Integer>> studentGrades = new HashMap<String, HashMap<String, Integer>>();
	private HashMap<String, HashMap<String, Integer>> studentContribs = new HashMap<String, HashMap<String, Integer>>();
	private HashMap<String, HashMap<String, Integer>> teamGrades = new HashMap<String, HashMap<String, Integer>>();

	ArrayList<String> assignmentList;
	ArrayList<String> projectList;

	/**
	 * Grades constructor opens the passed in name of the xlsx file and populates
	 * the local hashmaps
	 * 
	 * @param xlsx file name string
	 */
	public Grades(String arg) {
		this.dbName = arg;
			// populate the attendance record
			getAttendanceRecord();

			// populate assignmentList and studentGrades
			getAllStudentGrades();

			// populate projectList and studentContribs
			getAllStudentContributions();
			
			// populate team grades
			getAllTeamGrades();

	}

	/**
	 * openDBforRead opens the object FileInputStream for the DB
	 *
	 */
  public void openDBforRead	(int sheet) {
		try {
			this.file = new FileInputStream(new File(this.dbName));

			// Get the workbook instance for XLS file
			this.workbook = new XSSFWorkbook(file);
			
			// Get first sheet from the workbook
			this.sheet = workbook.getSheetAt(sheet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * closeDBforRead closes the object FileInputStream for the DB
	 *
	 */
  public void closeDBforRead (FileInputStream file) {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	  
  }

	/**
	 * writeDB creates, writes, and closes the  FileOutputStream to the DB
	 *
	 */
  public void writeDB() {
	  try {
	        FileOutputStream outfile = 
	                new FileOutputStream(new File(this.dbName));
	        workbook.write(outfile);
	        outfile.close();
	         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
  }
  
	/**
	 * getAttendanceRecord populates the attendance record
	 * 
	 */
	public void getAttendanceRecord() {
		String studentID;
		int student_attendance;
		
		openDBforRead(ATTENDANCE_SHEET);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		// Get iterator to all cells of current row
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// skip the first 2 rows
			// row 1 is the column letters
			// row 2 is the column headings
			if (row.getRowNum() < 1) {
				continue;
			}

			// assume cell count per row is a fixed quantity
			studentID = String.valueOf((int) row.getCell(ID_CELL)
					.getNumericCellValue());
			student_attendance = (int) row.getCell(ATTENDANCE_CELL)
					.getNumericCellValue();

			attendance.put(studentID, student_attendance);

		}
		
		closeDBforRead(file);
	}


	/**
	 * getStudentAttendance returns the attendance value for a specific studentID from the attendance
	 * record
	 * 
	 * @param student id string
	 * @return attendance int
	 */
	public int getStudentAttendance(String id) {
		return (attendance.get(id));
	}

	// populate the student grades record
	public void getAllStudentGrades() {
		String studentID;
		assignmentList = new ArrayList<String>();
		
		openDBforRead(STUDENT_GRADES_SHEET);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		// Get iterator to all cells of current row
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// skip the first row
			// but use it to determine the number of assignments
			if (row.getRowNum() < 1) {
				Iterator<Cell> firstCellIterator = row.cellIterator();
				while (firstCellIterator.hasNext()) {
					Cell cell = firstCellIterator.next();
					if(cell.getColumnIndex() < 1) {
						continue;
					}
					assignmentList.add(cell.getStringCellValue());
					
				}
				continue;
			}

			// for each row, create a 2D hash with studentID as key
			// and the 2nd dimension is assignment/grade pairs
			// {studentID:
			// { Assignment 1:99,
			// Assignment 2:100 }
			studentID = String.valueOf((int) row.getCell(ID_CELL)
					.getNumericCellValue());
			studentGrades.put(studentID, new HashMap<String, Integer>());
			for (int i = 0; i < assignmentList.size(); i++) {
				studentGrades.get(studentID).put(assignmentList.get(i),
						(int) row.getCell(i + 1).getNumericCellValue());
			}

		}
		
		closeDBforRead(file);


	}

	/**
	 * getAssignments return assignment list
	 * 
	 * @return the list of assignments
	 */
	public ArrayList<String> getAssignments() {
		return assignmentList;
	}

	/**
	 * getAllStudentContributions populates the contributions record
	 * 
	 */
	public void getAllStudentContributions() {
		String studentID;
		projectList = new ArrayList<String>();
		
		openDBforRead(CONTRIBS_SHEET);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		// Get iterator to all cells of current row
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// skip the first row
			// but use it to determine the number of assignments
			if (row.getRowNum() < 1) {
				Iterator<Cell> firstCellIterator = row.cellIterator();
				while (firstCellIterator.hasNext()) {
					Cell cell = firstCellIterator.next();
					if(cell.getColumnIndex() < 1) {
						continue;
					}
					projectList.add(cell.getStringCellValue());
				}
				continue;
			}

			// for each row, create a 2D hash with studentID as key
			// and the 2nd dimension is assignment/grade pairs
			// {studentID:
			// { Project 1:99,
			// Project 2:100 }
			studentID = String.valueOf((int) row.getCell(ID_CELL)
					.getNumericCellValue());
			studentContribs.put(studentID, new HashMap<String, Integer>());
			for (int i = 0; i < projectList.size(); i++) {
				studentContribs.get(studentID).put(projectList.get(i),
						(int) row.getCell(i + 1).getNumericCellValue());
			}

		}
		
		closeDBforRead(file);


	}

	/**
	 * getProjects return assignment list
	 * 
	 * @return the list of projects
	 */
	public ArrayList<String> getProjects() {
		return projectList;
	}

	// populate the student grades record
	public void getAllTeamGrades() {
		String teamName;

		openDBforRead(TEAM_GRADES_SHEET);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		// Get iterator to all cells of current row
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// skip the first row
			// but use it to determine the number of assignments
			if (row.getRowNum() < 1) {
				continue;
			}

			// for each row, create a 2D hash with studentID as key
			// and the 2nd dimension is assignment/grade pairs
			// {studentID:
			// { Assignment 1:99,
			// Assignment 2:100 }
			teamName = row.getCell(ID_CELL).getStringCellValue();
			teamGrades.put(teamName, new HashMap<String, Integer>());
			for (int i = 0; i < projectList.size(); i++) {
				teamGrades.get(teamName).put(projectList.get(i),
						(int) row.getCell(i + 1).getNumericCellValue());
			}

		}
		
		closeDBforRead(file);
	}	
	
	/**
	 * getIndividualGrades return grades for a selected student
	 * 
	 * @param id string
	 * @return hashmap of assignment name string / int grade pairs
	 */	
	public HashMap<String, Integer> getIndividualGrades (String id) {
		return studentGrades.get(id);
	}

	/**
	 * getIndividualContributions return team grade ratios for a selected student
	 * 
	 * @param id string
	 * @return hashmap of project name string / int contribution pairs
	 */	
	public HashMap<String, Integer> getIndividualContributions (String id) {
		return studentContribs.get(id);
	}


	/**
	 * getStudentTeamGrades return team grades for a selected team
	 * 
	 * @param team name string
	 * @return hashmap of project name string / int grade pairs
	 */	
	public HashMap<String, Integer> getStudentTeamGrades (String teamName) {
		return teamGrades.get(teamName);
	}	


	/**
	 * addColumn adds a new column to a selected sheet in the Grades DB
	 * row0 will hold the column heading, then al cells of the column will
	 * be set to 0
	 * 
	 * @param sheet number (or index)
	 * @return name of column (column heading string)
	 */		  
	public void addColumn(int sheetNum, String heading){
		  
	      openDBforRead(sheetNum);
	  
		  // determine the next cell to update		  
		  Row firstRow = sheet.getRow(sheet.getFirstRowNum());
		  int newCellNum = firstRow.getLastCellNum();
		  		  
		    //Update the value of cell
		    Cell cell = sheet.getRow(sheet.getFirstRowNum()).getCell(newCellNum);
		    if (cell == null) {
		        cell = sheet.getRow(sheet.getFirstRowNum()).createCell(newCellNum);
		    }
		    cell.setCellType(Cell.CELL_TYPE_STRING);
		    cell.setCellValue(heading);
		    
			Iterator<Row> rowIterator = sheet.iterator();
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        
		        // skip the first row
		        // row 1 is the column headings
		        if (row.getRowNum() < 1) {
		        	continue;
		        }
			    cell = row.getCell(newCellNum);
			    if (cell == null) {
			        cell = row.createCell(newCellNum);
			    }
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			    cell.setCellValue(0);
			}
	  
		  closeDBforRead(file);
		 		  
		  writeDB();

	  }

	/**
	 * addGrade adds a new column to a selected sheet in the Grades DB
	 * 
	 * @param sheet number (or index)
	 * @param column heading
	 * @param student id
	 * @param grade
	 */		
	  public void addGrade(int sheetNum, String category, String id, int grade) {
	      
	      openDBforRead(sheetNum);
	      Cell cell;
	      
	      int colIndex = getHeadingIndex(category);
	      int rowIndex = getRowIndex(id);
	      
		  cell = sheet.getRow(rowIndex).getCell(colIndex);
		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		  cell.setCellValue(grade);	     		    

		  closeDBforRead(file);
		  
		  writeDB();
 		      		    
	  }
	  
	  
		/**
		 * getHeadingIndex returns the cell index of the matching header value
		 * return -1 if not found
		 * 
		 * @param heading string
		 * @return column number
		 */	
	  private int getHeadingIndex(String cellTitle) {
	      Row row = this.sheet.getRow(sheet.getFirstRowNum());
	      int colIndex = -1;
		    
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.getStringCellValue().equals(cellTitle)){
					colIndex = cell.getColumnIndex();
					break;
				}
			}		    
          return colIndex;
	  }
	  
		/**
		 * getRowIndex returns the row index of the matching header value
		 * return -1 if not found
		 * 
		 * @param id string
		 * @return row number
		 */	
	  private int getRowIndex(String id) {
		  int rowIndex = -1;

			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = this.sheet.iterator();

			// Get iterator to all cells of current row
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// skip the first row
				// but use it to determine the number of assignments
				if (row.getRowNum() < 1) {
					continue;
				}
				
				if (String.valueOf((int) row.getCell(ID_CELL).getNumericCellValue()).equals(id)){
					rowIndex = row.getRowNum();
					break;
				}
			}
						    
          return rowIndex;
	  }	  
}
