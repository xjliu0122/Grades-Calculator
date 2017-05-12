package edu.gatech.seclass.gradescalc;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

///////////////////////
// NO CHANGES STARTS //
///////////////////////

public class CourseTestExtras {

    Students students = null;
    Grades grades = null;
    Course course = null;
    static final String GRADES_DB = "DB" + File.separator + "GradesDatabase6300-grades.xlsx";
    static final String GRADES_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-grades-goldenversion.xlsx";
    static final String STUDENTS_DB = "DB" + File.separator + "GradesDatabase6300-students.xlsx";
    static final String STUDENTS_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-students-goldenversion.xlsx";
    
    @Before
    public void setUp() throws Exception {
        FileSystem fs = FileSystems.getDefault();
        Path gradesdbfilegolden = fs.getPath(GRADES_DB_GOLDEN);
        Path gradesdbfile = fs.getPath(GRADES_DB);
        Files.copy(gradesdbfilegolden, gradesdbfile, REPLACE_EXISTING);
        Path studentsdbfilegolden = fs.getPath(STUDENTS_DB_GOLDEN);
        Path studentsdbfile = fs.getPath(STUDENTS_DB);
        Files.copy(studentsdbfilegolden, studentsdbfile, REPLACE_EXISTING);    	
    	students = new Students(STUDENTS_DB);
        grades = new Grades(GRADES_DB);
        course = new Course(students, grades);
    }

    @After
    public void tearDown() throws Exception {
        students = null;
        grades = null;
        course = null;
    }

    @Test
    public void testAddStudents() {
    	Student student1 = new Student("Jethro Tull", "901234517", course);
    	course.addStudent(student1);
    	course.updateStudents(new Students(STUDENTS_DB));
    	course.updateGrades(new Grades(GRADES_DB));
    	
        HashSet<Student> studentsRoster = null;
        studentsRoster = course.getStudents();
        Student student = null;
        for (Student s : studentsRoster) {
            if (s.getName().compareTo("Jethro Tull") == 0) {
                student = s;
                break;
            }
        }
        assertNotNull(student);      
        assertEquals(17, studentsRoster.size());
    }
    
    @Test
    public void testAddProject() {
        course.addProject("Project: StallManager");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(4, course.getNumProjects());
        course.addProject("Project: AvgSentenceLength");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(5, course.getNumProjects());
    }
    
    @Test
    public void testAddGradesForProject() {
        String projectName = "Project: StallManager";
        course.addProject(projectName);
        course.updateGrades(new Grades(GRADES_DB));
        HashMap<String, Integer> grades = new HashMap<String, Integer>();
        grades.put("Team 1", 87);
        grades.put("Team 2", 94);
        grades.put("Team 3", 100);
        grades.put("Team 4", 100);
        course.addGradesForProject(projectName, grades);
        course.updateGrades(new Grades(GRADES_DB));
        
        Student student1 = new Student("Kym Hiles", "901234507", course);
        Student student2 = new Student("Sheree Gadow", "901234513", course);
        HashMap<Student, Integer> contributions1 = new HashMap<Student, Integer>();
        contributions1.put(student1, 96);
        contributions1.put(student2, 87);       
        course.addIndividualContributions(projectName, contributions1);
        course.updateGrades(new Grades(GRADES_DB));

        assertEquals(89, course.getAverageProjectsGrade(student1));
        assertEquals(88, course.getAverageProjectsGrade(student2));
    }
    
}
    
