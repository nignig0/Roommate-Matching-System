import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MatchingSystem {
    private ArrayList<Student> students; //we don't know how many students are in the file
    private Hostel[] hostels;

    public MatchingSystem(){
        getStudents();
        initHostels();
    }

    public void matchStudents(){
        //match students 
    }

    public void displayMatches(){

    }

    private void initHostels(){
        //do something here to initialize
        //the hostels 
    }

    private void getStudents(){
         File file = new File("OOP_Project_Form.txt");
         try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                //read the student data from the csv file
                String[] data = sc.nextLine().split(",");

                //create the student
                Student student = new Student(
                    data[1].substring(1, data[1].length()-1),
                    data[3].substring(1, data[3].length()-1),
                    data[2].substring(1, data[2].length()-1),
                    Integer.parseInt(data[4].substring(1, data[4].length()-1)), 
                    Integer.parseInt(data[5].substring(1, data[5].length()-1)),
                    Integer.parseInt(data[6].substring(1, data[6].length()-1)),
                    Integer.parseInt(data[7].substring(1, data[7].length()-1)),
                    Integer.parseInt(data[8].substring(1, data[8].length()-1)),
                    Integer.parseInt(data[9].substring(1, data[9].length()-1)),
                    Integer.parseInt(data[10].substring(1, data[10].length()-1))
                );
                students.add(student);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
    }
}
