import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class MatchingSystem {
    private ArrayList<Student> students; // we don't know how many students are in the file
    private Hostel[] hostels;
    ArrayList<Map.Entry<Student, Student>> pairs;
    String studentFilename;

    public MatchingSystem(String filename) throws IOException {
        studentFilename = filename;
        students = new ArrayList<>();
        pairs = new ArrayList<>();
        getStudents();
        System.out.println("Student objects created!");
        initHostels();
        System.out.println("Hostel objects created!");
    }

    /**
     * Matches single students 
     * Single students are students who want to stay off campus
     * And require lots of personal space
     * We assign them to single rooms off campus
     */
    private void matchSingleStudents() {
        ArrayList<Student> singleStudents = new ArrayList<Student>();

        for (int i = 0; i < students.size(); ++i) {
            Student s = students.get(i);

            if ((s.getHowMuchPersonalSpace().ordinal() == 2
                    || s.getHowMuchPersonalSpace().ordinal() == 1)
                    &&
                    (s.getOffCampusOrOn().ordinal() == 2
                            || s.getOffCampusOrOn().ordinal() == 1)) {
                
                /*
                 * If the student wants has a medium to high preference for personal space 
                 * And the student wants to be off campus or is indifferent
                 */

                singleStudents.add(s);
            }
        }

        /*
         * The rest of the code is basically looping through all single students
         * and assigning them to the available single rooms
         */

        int studCounter = 0; 
        for (int i = 0; i < hostels.length; ++i) {
            if (studCounter == singleStudents.size())
                return;

            if (!hostels[i].isOffCampus())
                continue;

            Room[][] rooms = hostels[i].getRooms();

            // System.out.println(rooms.length);

            for (int j = 0; j < rooms.length; ++j) {
                for (int k = 0; k < rooms[j].length; ++k) {

                    /*
                     * loop all the rooms in the off campus hostel
                     * assign the students who want to be off campus and valye
                     * personal space to the free rooms
                     */
                    if (rooms[j][k] instanceof SingleRoom) {
                        if (rooms[j][k].addOccupant(singleStudents.get(studCounter))) {
                            singleStudents.get(studCounter).setMatched();
                            singleStudents.get(studCounter).setRoomed();
                        }

                        studCounter++;
                        if (studCounter == singleStudents.size())
                            return;
                    }
                }
            }

        }
    }

    /**
     * Calculates the match score for two students 
     * The match score is how well two students fit each other
     * A low match score means two students are well suited to each other 
     * @param s student 1
     * @param t student 2
     * @return returns the match score
     */
    private int calculateMatchScore(Student s, Student t) {
        int[] sPrefArray = s.getPreferenceArray();
        int[] tPrefArray = t.getPreferenceArray();
        int score = 0;

        //if one of the students wants to be off campus and the other wants to be on
        //then they can't be roommates 
        //Incompatible!

        if (s.getOffCampusOrOn().ordinal() == 2 & t.getOffCampusOrOn().ordinal() == 0) {
            return 1000;
        }

        if (t.getOffCampusOrOn().ordinal() == 2 & s.getOffCampusOrOn().ordinal() == 0) {
            return 1000;
        }

        for (int i = 0; i < sPrefArray.length - 1; ++i) {
            score += Math.abs(sPrefArray[i] - tPrefArray[i]);
        }
        return score;
    }


    /**
     * Creates pairs (x,y) of every student x and their best available match y
     * Important to note that there could be a pair (x, null)
     * If we have an odd number of students
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void matchDoubleStudents() {
        // calculate everyone's best matches first
        for (int i = 0; i < students.size(); ++i) {
            int score = 100;
            Student s = students.get(i);
            Student bestMatch = null;

            //if this student has a room or has been matched already, ignore them. Move on.
            if (s.isMatched() || s.hasRoom()) continue;

            for (int j = i + 1; j < students.size(); ++j) {
                Student t = students.get(j);

                if (t.isMatched() || t.hasRoom()) continue;

                if (!s.getGender().equals(t.getGender())) continue;// opposite genders init

                int matchScore = calculateMatchScore(s, t);
                if (matchScore < score) {
                    bestMatch = t;
                    score = matchScore;
                }
            }

            // at this point we have the best match
            if (bestMatch != null) {
                bestMatch.setMatched();
                s.setMatched();
            }
            pairs.add(new AbstractMap.SimpleEntry(s, bestMatch));
            //adding the pairs
        }
    }

    /**
     * Assigns each pair (x,y) to available double rooms
     */
    private void handleMatchedPairs() {

        for (int i = 0; i < pairs.size(); ++i) {

            Student s = pairs.get(i).getKey();
            Student t = pairs.get(i).getValue();

            if (t == null) continue; //if the second student in the pair is null, move on,

            for (int j = 0; j < hostels.length; ++j) {
                if (t.hasRoom() && s.hasRoom()) break; //if both students have been assigned a room, end this loop

                // they want to be on campus but the hostel is an off campus hostel
                if (s.getOffCampusOrOn().ordinal() == 0 && hostels[j].isOffCampus())
                    continue;

                // they want to be off campus and the hostel is onn campus
                if (s.getOffCampusOrOn().ordinal() == 2 && !hostels[j].isOffCampus())
                    continue;

                Room[][] rooms = hostels[j].getRooms();

                for (int a = 0; a < rooms.length; a++) {

                    //end the students have been assigned rooms
                    if (t.hasRoom() && s.hasRoom()) break;

                    for (int b = 0; b < rooms[a].length; b++) {

                        if (rooms[a][b] instanceof DoubleRoom && rooms[a][b].isEmpty()) {
                            //if we have a free double room
                            if (rooms[a][b].addOccupant(s)) {
                                s.setRoomed();
                            }

                            if (rooms[a][b].addOccupant(t)) {
                                t.setRoomed();
                            }

                            break;
                        }
                    }
                }

            }
        }
    }

    /**
     * For every pair (x,y), finds the pair (a,b) that is most compatible with them
     */

    private void matchQuads() {
        for (int i = 0; i < pairs.size(); ++i) {

            Student s = pairs.get(i).getKey();
            if (s.hasRoom())
                continue;

            Map.Entry<Student, Student> bestMatchedPair = null;
            int score = 100;

            for (int j = i + 1; j < pairs.size(); ++j) {
                Student a = pairs.get(j).getValue(); //could be the key here as well
                
                if (a == null) continue;
                if (a.hasRoom()) continue;
                if (!s.getGender().equals(a.getGender())) continue; // opposite genders init

                int matchScore = calculateMatchScore(s, a);
                if (matchScore < score) {
                    score = matchScore;
                    bestMatchedPair = pairs.get(j);
                }
            }
            // we've gotten the matched pair now
            // let's put them in a room
            findQuadrupleRoom(pairs.get(i), bestMatchedPair);

        }
    }

    /**
     * Puts pairs (x,y) and (a,b) in a double room
     * @param pair1
     * @param pair2
     */

    private void findQuadrupleRoom(Map.Entry<Student, Student> pair1, Map.Entry<Student, Student> pair2) {
        Student s = pair1.getKey();

        if (pair1.getValue() == null) return;

        Student t = pair1.getValue();

        Student a = (pair2 == null) ? null : pair2.getKey();
        Student b = (pair2 == null) ? null : pair2.getValue();

        //finding a room for the pairs 
        for (int i = 0; i < hostels.length; i++) {
            
            if (s.hasRoom() && t.hasRoom()) break;
            // they want to be on campus but the hostel is an off campus hostel
            if (s.getOffCampusOrOn().ordinal() == 0 && hostels[i].isOffCampus())
                continue;

            // they want to be off campus and the hostel is onn campus
            if (s.getOffCampusOrOn().ordinal() == 2 && !hostels[i].isOffCampus())
                continue;
            Room[][] rooms = hostels[i].getRooms();

            for (int j = 0; j < rooms.length; j++) {
                
                //if the first pair have rooms, then we can break
                //as the other pair might be (null, null)
                //We could check that...
                //trying to keep the code pretty simple

                if (s.hasRoom() && t.hasRoom()) break;

                for (int k = 0; k < rooms[j].length; k++) {
                    if (rooms[j][k] instanceof QuadrupleRoom && rooms[j][k].isEmpty()) {
                        Room room = rooms[j][k];
                        if (s != null) {
                            if (room.addOccupant(s))
                                s.setRoomed();
                        }
                        if (t != null) {
                            if (room.addOccupant(t))
                                t.setRoomed();

                        }
                        if (a != null) {
                            if (room.addOccupant(a))
                                a.setRoomed();
                        }
                        if (b != null) {
                            if (room.addOccupant(b))
                                b.setRoomed();
                        }
                        break;
                    }
                }

            }
        }
    }

    /**
     * If a student hasn't been matched, we put them in a room
     */

    private void matchTheUnmatched() {

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).hasRoom())
                continue;
            for (int j = 0; j < hostels.length; j++) {
                Student s = students.get(i);

                if (s.hasRoom())
                    break;

                // they want to be on campus but the hostel is an off campus hostel
                if (s.getOffCampusOrOn().ordinal() == 0 && hostels[j].isOffCampus())
                    continue;

                // they want to be off campus and the hostel is onn campus
                if (s.getOffCampusOrOn().ordinal() == 2 && !hostels[j].isOffCampus())
                    continue;

                Room[][] rooms = hostels[j].getRooms();

                for (int a = 0; a < rooms.length; a++) {
                    for (int b = 0; b < rooms[a].length; b++) {
                        if (s.hasRoom())
                            break;
                        Room room = rooms[a][b];
                        if (room.isFull())  continue;
                        if (!room.isEmpty()) {
                            if (!room.getOccupants()[0].getGender().equals(s.getGender()))
                                continue;
                        }
                        if (room.addOccupant(s))
                            s.setRoomed();
                    }
                }
            }
        }
    }

    public void matchStudents() {
        System.out.printf("Welcome to The Roommate Matching System, currently reading from file:\n%s\n",
                studentFilename);
        // match students
        // step one put people in single rooms
        System.out.println("Matching single students");
        matchSingleStudents();
        System.out.println("Single students matched successfully");

        System.out.println("Matching double students");
        matchDoubleStudents();
        System.out.println("Matched pairs created");

        System.out.println("Handling matched pairs");
        handleMatchedPairs();
        System.out.println("Matched pairs handled successfully!");

        System.out.println("Handling the quads");
        matchQuads();
        System.out.println("Handled quads successfully!");

        System.out.println("Matching the unmatched :(");
        matchTheUnmatched();
        System.out.println("Matched the unmatched");

        displayMatches();

        int notMatched = 0;
        for (Student s : students) {
            if (!s.hasRoom()) {
                notMatched++;
                System.out.printf("Student with ID %s does not have a room\n", s.getId());
            }
        }
        System.out.printf("%d students did not get find a match\n", notMatched);
    }

    public void displayMatches() {
        for (int i = 0; i < hostels.length; i++) {
            hostels[i].displayRooms();
        }
    }

    private void initHostels() {
        hostels = new Hostel[4]; // temp change for testing purposes

        // Off campus hostels
        final int N_HOS_SINGLE_ROOMS = 1;
        final int N_HOS_DOUBLE_ROOMS = 4;
        final int N_HOS_QUAD_ROOMS = 4;
        final int N_HOS_TOTAL_ROOMS = N_HOS_SINGLE_ROOMS + N_HOS_DOUBLE_ROOMS + N_HOS_QUAD_ROOMS;
        final int N_HOS_FLOORS = 2;
        final int N_HOS_ROOMS_PER_FLOOR = N_HOS_TOTAL_ROOMS / N_HOS_FLOORS;
        Room newHosannaRooms[][] = new Room[N_HOS_FLOORS][N_HOS_ROOMS_PER_FLOOR];
        populateRooms(newHosannaRooms, N_HOS_SINGLE_ROOMS, N_HOS_DOUBLE_ROOMS, N_HOS_QUAD_ROOMS, N_HOS_FLOORS);

        hostels[0] = new Hostel("New Hosanna", N_HOS_TOTAL_ROOMS, newHosannaRooms, true, true, true, N_HOS_FLOORS);

        final int OFF_2_SINGLE_ROOMS = 0;
        final int OFF_2_DOUBLE_ROOMS = 5;
        final int OFF_2_QUAD_ROOMS = 5;
        final int OFF_2_TOTAL_ROOMS = OFF_2_SINGLE_ROOMS + OFF_2_DOUBLE_ROOMS + OFF_2_QUAD_ROOMS;
        final int OFF_2_FLOORS = 2;
        final int OFF_2_ROOMS_PER_FLOOR = OFF_2_TOTAL_ROOMS / OFF_2_FLOORS;
        Room offHos2Rooms[][] = new Room[OFF_2_FLOORS][OFF_2_ROOMS_PER_FLOOR];
        populateRooms(offHos2Rooms, OFF_2_SINGLE_ROOMS, OFF_2_DOUBLE_ROOMS, OFF_2_QUAD_ROOMS, OFF_2_FLOORS);

        hostels[1] = new Hostel("Off Campus 2", OFF_2_TOTAL_ROOMS, offHos2Rooms, true, true, true, OFF_2_FLOORS);

        final int ON_1_SINGLE_ROOMS = 0;
        final int ON_1_DOUBLE_ROOMS = 7;
        final int ON_1_QUAD_ROOMS = 10;
        final int ON_1_FLOORS = 2;
        final int ON_1_TOTAL_ROOMS = ON_1_DOUBLE_ROOMS + ON_1_SINGLE_ROOMS + ON_1_QUAD_ROOMS;
        final int ON_1_ROOMS_PER_FLOOR = (int) Math.ceil(ON_1_TOTAL_ROOMS / ON_1_FLOORS);
        Room onHos1Rooms[][] = new Room[ON_1_FLOORS][ON_1_ROOMS_PER_FLOOR];
        populateRooms(onHos1Rooms, ON_1_SINGLE_ROOMS, ON_1_DOUBLE_ROOMS, ON_1_QUAD_ROOMS, ON_1_FLOORS);

        hostels[2] = new Hostel("On Campus 1", ON_1_TOTAL_ROOMS, onHos1Rooms, false, false, false, ON_1_FLOORS);

        final int ON_2_SINGLE_ROOMS = 0;
        final int ON_2_DOUBLE_ROOMS = 0;
        final int ON_2_QUAD_ROOMS = 10;
        final int ON_2_FLOORS = 2;
        final int ON_2_TOTAL_ROOMS = ON_2_DOUBLE_ROOMS + ON_2_SINGLE_ROOMS + ON_2_QUAD_ROOMS;
        final int ON_2_ROOMS_PER_FLOOR = ON_2_TOTAL_ROOMS / ON_2_FLOORS;
        Room onHos2Rooms[][] = new Room[ON_2_FLOORS][ON_2_ROOMS_PER_FLOOR];
        populateRooms(onHos2Rooms, ON_2_SINGLE_ROOMS, ON_2_DOUBLE_ROOMS, ON_2_QUAD_ROOMS, ON_2_FLOORS);

        hostels[3] = new Hostel("On Campus 2", ON_2_TOTAL_ROOMS, onHos2Rooms, false,
                false, false, ON_2_FLOORS);
    }

    private void populateRooms(Room[][] rooms, int singleRooms, int doubleRooms, int quadRooms, int floors) {
        int singles = 0;
        int doubles = 0;
        int quads = 0;
        char roomL = 'A';
        int roomN = 1;
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (roomN >= 5) {
                    roomL++;
                    roomN = 1;
                }
                if (singles < singleRooms) {
                    rooms[i][j] = new SingleRoom(String.format("%s%d", roomL, roomN));
                    singles++;
                } else if (doubles < doubleRooms) {
                    rooms[i][j] = new DoubleRoom(String.format("%s%d", roomL, roomN));
                    doubles++;
                } else if (quads < quadRooms) {
                    rooms[i][j] = new QuadrupleRoom(String.format("%s%d", roomL, roomN));
                    quads++;
                }
                roomN++;
            }
        }
    }

    private void getStudents() throws IOException {
        File file = new File(studentFilename);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                // read the student data from the csv file
                String[] data = sc.nextLine().split(",");

                // create the student
                Student student = new Student(
                        data[1],
                        data[3],
                        data[2],
                        Integer.parseInt(data[4]),
                        Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]),
                        Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]),
                        Integer.parseInt(data[9]),
                        Integer.parseInt(data[10]));
                students.add(student);
            }
            sc.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (sc != null)
                sc.close();
        }
    }

    public static void main(String[] args) {
        try {
            String filename = (args.length == 0 )? "OOP Project Form (Responses) - Form responses 1.csv" : args[0];
            MatchingSystem matcher = new MatchingSystem(filename);
            matcher.matchStudents();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
