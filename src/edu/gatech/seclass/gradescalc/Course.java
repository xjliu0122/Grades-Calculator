/**
 * 
 */
package edu.gatech.seclass.gradescalc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

public class Course {	
	private Students students;
	private Grades grades;
	
	private String formula;

    HashSet<Student> studentsRoster = new HashSet<Student>();
    
	private static final int GRADES_ATTENDANCE_SHEET = 0;
	private static final int GRADES_STUDENT_GRADES_SHEET = 1;
	private static final int GRADES_CONTRIBS_SHEET = 2;
	private static final int GRADES_TEAM_GRADES_SHEET = 3;

	public Course(Students students, Grades grades) {
        this.students = students;
        this.grades = grades;	
        this.formula = "ATT * 0.2 + AAG * 0.3 + APG * 0.5";
        
        studentsRoster = students.getStudentsRoster();
        updateAttendance();
	}

	/**
	 * getStudents returns the student roster
	 * 
	 * @return student roster hashset
	 */
	public HashSet<Student> getStudents() {
		return studentsRoster;
	}	

	/**
	 * getNumStudents returns the number of students in roster
	 * 
	 * @return number of students
	 */
	public int getNumStudents() {
		return studentsRoster.size();
	}

	/**
	 * getStudentByName returns the student object for the given student name
	 * 
	 * @return student object
	 */
	public Student getStudentByName (String name) {
		Student student = null;
		
	    Iterator<Student> iter = studentsRoster.iterator();
	    while (iter.hasNext()) {
	      student = iter.next();
	      if(student.getName().equals(name)) {
	    	  break;
	      }	      
	    }
	    
        return student;	   
	}


	/**
	 * getStudentByID returns the student object for the given student id
	 * 
	 * @return student object
	 */
	public Student getStudentByID (String id) {
		Student theStudent = null;
	    
	    for (Student student : studentsRoster) {
	      if (student.getGtid().equals(id)) 
	    	  theStudent = student;
	    } 
		return theStudent;
	}

	/**
	 * getNumProjects returns the number of projects listed in the xlsx
	 * 
	 * @return project count
	 */
	public int getNumProjects()	{
		return grades.getProjects().size();
	}

	/**
	 * getNumAssignments returns the number of assignment listed in the xlsx
	 * 
	 * @return assignment count
	 */
	public int getNumAssignments() {
		return grades.getAssignments().size();
	}

	/**
	 * updateAttendance adds attendance data to the students class
	 * 
	 */
  public void updateAttendance() {
	    for (Student student : studentsRoster) {
	    	student.setAttendance(grades.getStudentAttendance(student.getGtid()));
	    }
	  }

  
	/**
	 * getAverageAssignmentsGrade gets the average grade
	 * for a given student
	 * 
	 * @param student
	 * @return (sum of all grades) / number of assignments
	 */  
  public int getAverageAssignmentsGrade(Student student){
	  double gradeSum = sumMap(grades.getIndividualGrades(student.getGtid()));
	  return (int) Math.round(gradeSum/getNumAssignments());
  }

	/**
	 * getAverageProjectsGrade gets the average team grade
	 * for a given student
	 * 
	 * @param student
	 * @return (sum of ((each project contribution / 100) * project grade)) / number of projects
	 */ 
  public int getAverageProjectsGrade(Student student){      
	  Map<String, Integer> contribMap = grades.getIndividualContributions(student.getGtid());
	  Map<String, Integer> teamMap = grades.getStudentTeamGrades(this.getTeam(student));

	  double doubleSum = 0;
	  
	  for (String key : contribMap.keySet()) {
	      float contrib = (float)contribMap.get(key);
	      Integer projGrade = teamMap.get(key);
	      doubleSum += (double)((contrib/100) * projGrade);
	  }
	  
	  return (int) Math.round(doubleSum/getNumProjects());
  }
 
	/**
	 * addAssignment adds a new assignment column to the Students Grades Sheet of Grades DB
	 * 
	 * @param assignment name
	 */
  public void addAssignment(String arg){
	  grades.addColumn(GRADES_STUDENT_GRADES_SHEET, arg);
	  
  }
  
	/**
	 * updateGrades overwrites the existing grades object with the passed object
	 * 
	 * @param Grades object
	 */
  public void updateGrades(Grades newGrades) {
	  grades = null;
	  grades = newGrades;		  
  }
  
	/**
	 * addIndividualContributions update a students contribution ratio for a given team project
	 * 
	 * @param project name string
	 * @param hashmap of student object / int value pairs
	 */ 
  public void addIndividualContributions(String project, HashMap<Student, Integer> contribution) {
      Iterator<Student> iterator = contribution.keySet().iterator();
      while (iterator.hasNext()) {
        Student key = (Student) iterator.next();
	    grades.addGrade(GRADES_CONTRIBS_SHEET, project, key.getGtid(), contribution.get(key));        
      }
  }
 
	/**
	 * addGradesForAssignment update a students grade for a given assignment
	 * 
	 * @param assignment name string
	 * @param hashmap of student object / int value pairs
	 */ 
  public void addGradesForAssignment(String assignmentName, HashMap<Student, Integer> gradesMap) {	  
      Iterator<Student> iterator = gradesMap.keySet().iterator();
      while (iterator.hasNext()) {
        Student key = (Student) iterator.next();
	    grades.addGrade(GRADES_STUDENT_GRADES_SHEET, assignmentName, key.getGtid(), gradesMap.get(key));        
      }
  }

	/**
	 * addStudent adds a new student to the Students DB
	 * 
	 * @param Student object
	 */
	public void addStudent(Student student) {
		// TODO: add new student to Students DB
		// TODO: add student row to each relevant sheet of Grades db
	}

	/**
	 * updateStudents overwrites the existing Students object with the passed object
	 * 
	 * @param Students object
	 */
	public void updateStudents(Students newStudents) {
		  students = null;
		  students = newStudents;	
    }

	/**
	 * addProject add a project column to the TEAM_GRADES_SHEET
	 * 
	 * @param String project name
	 */
    public void addProject(String project) {
    	// TODO: add project column to GRADES_TEAM_GRADES_SHEET
    }

	/**
	 * addGradesForProject adds a list of grades to the TEAM_GRADES_SHEET in the selected project column
	 * 
	 * @param project name string
	 * @param hashmap of id string / int value pairs
	 */ 
    public void addGradesForProject(String projectName, HashMap<String, Integer> gradesMap) {
  	// TODO: add team grades to GRADES_TEAM_GRADES_SHEET
    }    
    
	/**
	 * sumMap: helper function to sum the values in a hashmap of string/int pairs
	 * 
	 */ 
  public int sumMap (HashMap<String, Integer> map) {
	  int sum = 0;
	  for (String key : map.keySet()) {
	      Integer value = map.get(key);
	      sum += value;
	  }
	  return sum;
  }

	/**
	 * getAttendance returns a student's attendance
	 * 
	 * @param Students object
	 * @return int attendance
	 */ 
	public int getAttendance(Student student) {
		return grades.getStudentAttendance(student.getGtid());
	}
 
	/**
	 * getTeam returns a student's team name
	 * 
	 * @param Students object
	 * @return String team name
	 */ 
	public String getTeam(Student student) {
		String team = "NO TEAM";
	    for (Student curr_student : studentsRoster) {
		      if (curr_student.getGtid().equals(student.getGtid())) 
		    	  team = curr_student.getTeam();
		    }
		return team;
	}
	
	/**
	 * getEmail returns a student's email
	 * 
	 * @param Students object
	 * @return String email
	 */ 
	public String getEmail(Student student) {
		String email = "NO EMAIL";
	    for (Student obj : studentsRoster) {
	        if (obj.getGtid() == student.getGtid()) 
	          email = obj.getEmail();
	    }
		return email;
	}

	/**
	 * getOverallGrade returns a student's overall grade based on the set formula
	 * 
	 * @param Students object
	 * @return int grade
	 */ 
	public int getOverallGrade(Student student) {
		int att = getAttendance(student);
		int aag = getAverageAssignmentsGrade(student);
		int apg = getAverageProjectsGrade(student);
		
		double attGrade = 0;
		double aagGrade = 0;
		double apgGrade = 0;
		
		String[] gradeFormulas = this.formula.split("\\+");
		for(int i=0; i<gradeFormulas.length; i++) {
			String[] formulaParts = gradeFormulas[i].trim().split("\\*");
			if(gradeFormulas[i].contains("ATT")) {
				attGrade = (double) (att * Double.parseDouble(formulaParts[formulaParts.length-1]));
				//System.out.println("ATT = " + att + " value = " + formulaParts[formulaParts.length-1]);
			} else if(gradeFormulas[i].contains("AAG")) {
				aagGrade = (double) (aag * Double.parseDouble(formulaParts[formulaParts.length-1]));								
				//System.out.println("AAG = " + aag + " value = " + formulaParts[formulaParts.length-1]);
			} else if(gradeFormulas[i].contains("APG")) {
				apgGrade = (double) (apg * Double.parseDouble(formulaParts[formulaParts.length-1]));												
				//System.out.println("APG = " + apg + " value = " + formulaParts[formulaParts.length-1]);
			}
			
		}
		
		return (int) (attGrade + aagGrade + apgGrade);
	}

	/**
	 * setFormula updates the formula from its default
	 * it expects a combination of strings in the form of "A** * #[.#]" joined by " + " as needed
	 * 
	 * @param String formula
	 * @throws GradeFormulaException if the new formula has too may formulae or each sub-formula does not adhere to syntax
	 */ 
	public void setFormula(String newFormula) {		
		String[] gradeFormulas = newFormula.split("\\+");
		if(gradeFormulas.length > 3) {
			throw new GradeFormulaException("Too many calculations in argument");
		}
		
		String formPatternB = "A\\w+\\s*\\*\\s*\\d+(?:\\.\\d+)?\\s*";		
		for(int i=0; i<gradeFormulas.length; i++) {
			String formulaB = gradeFormulas[i].trim();
			if(!formulaB.matches(formPatternB)) {
				throw new GradeFormulaException("Formula pattern " + i + " is invalid: " + formulaB);
			}
		}
		this.formula = newFormula;
		
	}

	/**
	 * getFormula returns the current formula string
	 * 
	 * @return String formula
	 */ 
	public String getFormula() {
		return this.formula;
	}
	
}
