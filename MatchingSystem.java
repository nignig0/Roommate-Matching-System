import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MatchingSystem {
    private ArrayList<Student> students; // we don't know how many students are in the file
    private Hostel[] hostels;

    public MatchingSystem() {
        getStudents();
        initHostels();
    }

    public void matchStudents() {
        // match students
    }

    public void displayMatches() {

    }

    private void initHostels() {
        // do something here to initialize
        // the hostels
        hostels = new Hostel[4];

        // Off campus hostels
        final int N_HOS_SINGLE_ROOMS = 4;
        final int N_HOS_DOUBLE_ROOMS = 4;
        final int N_HOS_QUAD_ROOMS = 4;
        final int N_HOS_TOTAL_ROOMS = N_HOS_SINGLE_ROOMS + N_HOS_DOUBLE_ROOMS + N_HOS_QUAD_ROOMS;
        final int N_HOS_FLOORS = 2;
        final int N_HOS_ROOMS_PER_FLOOR = N_HOS_TOTAL_ROOMS / N_HOS_FLOORS;
        Room newHosannaRooms[][] = new Room[N_HOS_FLOORS][N_HOS_ROOMS_PER_FLOOR];
        {
            int singles = 0;
            int doubles = 0;
            int quads = 0;
            char roomL = 'A';
            int roomN = 1;
            for (int i = 0; i < newHosannaRooms.length; i++) {
                for (int j = 0; j < newHosannaRooms[0].length; j++) {
                    if (roomN >= 5) {
                        roomL++;
                        roomN = 1;
                    }
                    if (singles < N_HOS_SINGLE_ROOMS) {
                        newHosannaRooms[i][j] = new SingleRoom(String.format("%s%d", roomL, roomN));
                        singles++;
                    } else if (doubles < N_HOS_DOUBLE_ROOMS) {
                        newHosannaRooms[i][j] = new DoubleRoom(String.format("%s%d", roomL, roomN));
                        doubles++;
                    } else if (quads < N_HOS_QUAD_ROOMS) {
                        newHosannaRooms[i][j] = new QuadrupleRoom(String.format("%s%d", roomL, roomN));
                        quads++;
                    }
                    roomN++;
                }
            }

        }

        hostels[0] = new Hostel("New Hosanna", N_HOS_TOTAL_ROOMS, newHosannaRooms, true, true, true, N_HOS_FLOORS);
    }

    private void getStudents() {
        students = new ArrayList<>();
        File file = new File("OOP_Project_Form.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                // read the student data from the csv file
                String[] data = sc.nextLine().split(",");

                // create the student
                Student student = new Student(
                        data[1].substring(1, data[1].length() - 1),
                        data[3].substring(1, data[3].length() - 1),
                        data[2].substring(1, data[2].length() - 1),
                        Integer.parseInt(data[4].substring(1, data[4].length() - 1)),
                        Integer.parseInt(data[5].substring(1, data[5].length() - 1)),
                        Integer.parseInt(data[6].substring(1, data[6].length() - 1)),
                        Integer.parseInt(data[7].substring(1, data[7].length() - 1)),
                        Integer.parseInt(data[8].substring(1, data[8].length() - 1)),
                        Integer.parseInt(data[9].substring(1, data[9].length() - 1)),
                        Integer.parseInt(data[10].substring(1, data[10].length() - 1)));
                students.add(student);
            }
            sc.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }
}
