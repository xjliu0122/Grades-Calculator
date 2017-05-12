/**
 * 
 */
package edu.gatech.seclass.gradescalc;

public class Student {
	private String name;
	private String gtID;
	private String email;
	private int years_of_C;
	private int years_of_cPlusPlus;
	private int years_of_java;
	private boolean job_experience;
	private String team;
	private int attendance;

	public Student () {
		
	}	

	/**
	 * constructor
	 * sets all of the student fields based on the course's student roster data
	 * 
	 * @param name string
	 * @param id string
	 * @param Course object
	 * @return student object
	 */	
	public Student (String name, String id, Course course) {
		this.setName(name);
		this.setGtid(id);
		
		this.setEmail(course.getStudentByName(name).getEmail());
		this.setYears_of_C(course.getStudentByName(name).getYears_of_C());
		this.setYears_of_cPlusPlus(course.getStudentByName(name).getYears_of_cPlusPlus());
		this.setYears_of_java(course.getStudentByName(name).getYears_of_java());
		this.setJob_experience(course.getStudentByName(name).hasJob_experience());
		this.setTeam(course.getStudentByName(name).getTeam());
		this.setAttendance(course.getStudentByName(name).getAttendance());
	}

	/**
	 * constructor
	 * sets on the name and id
	 * 
	 * @param name string
	 * @param id string
	 * @return student object
	 */	
	public Student (String name, String id) {
		this.setName(name);
		this.setGtid(id);
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGtid() {
		return gtID;
	}
	public void setGtid(String gtID) {
		this.gtID = gtID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getYears_of_C() {
		return years_of_C;
	}
	public void setYears_of_C(int years_of_C) {
		this.years_of_C = years_of_C;
	}
	public int getYears_of_cPlusPlus() {
		return years_of_cPlusPlus;
	}
	public void setYears_of_cPlusPlus(int years_of_cPlusPlus) {
		this.years_of_cPlusPlus = years_of_cPlusPlus;
	}
	public int getYears_of_java() {
		return years_of_java;
	}
	public void setYears_of_java(int years_of_java) {
		this.years_of_java = years_of_java;
	}
	public boolean hasJob_experience() {
		return job_experience;
	}
	public void setJob_experience(boolean job_experience) {
		this.job_experience = job_experience;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public int getAttendance() {
		return attendance;
	}
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}	

}
