import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class MatchingSystem {
    private ArrayList<Student> students; // we don't know how many students are in the file
    private Hostel[] hostels;
    ArrayList<Map.Entry<Student, Student>> pairs;

    public MatchingSystem() {
        students = new ArrayList<>();
        pairs  = new ArrayList<>();
        getStudents();
        System.out.println("Student objects created!");
        initHostels();
        System.out.println("Hostel objects created!");
    }

    private void matchSingleStudents(){
        ArrayList<Student> singleStudents = new ArrayList<Student>();

        for(int i = 0; i<students.size();++i){
            Student s = students.get(i);

            if((s.getHowMuchPersonalSpace().ordinal() == 2 
            || s.getHowMuchPersonalSpace().ordinal()  == 1 )
            && 
            (s.getOffCampusOrOn().ordinal() == 2 
            || s.getOffCampusOrOn().ordinal() == 1)){
                    singleStudents.add(s);
                }
        }

        int studCounter = 0;
        for(int i = 0; i<hostels.length; ++i){
            if(studCounter == singleStudents.size()) return;

            if(!hostels[i].isOffCampus()) continue;

            Room[][] rooms = hostels[i].getRooms();
            
            //System.out.println(rooms.length);
            
            for(int j = 0; j<rooms.length; ++j){
                for(int k = 0; k<rooms[j].length; ++k){

                    /*loop all the rooms in the off campus hostel
                    assign the students who want to be off campus and valye
                    personal space to the free rooms
                    */
                    if(rooms[j][k] instanceof SingleRoom){
                        rooms[j][k].addOccupant(singleStudents.get(studCounter));
                        singleStudents.get(studCounter).setMatched();
                        singleStudents.get(studCounter).setRoomed();
                        studCounter++;
                        if(studCounter == singleStudents.size()) return;
                    }
                }
            }

        }
    }

    private int calculateMatchScore(Student s, Student t){
        int[] sPrefArray = s.getPreferenceArray();
        int[] tPrefArray = t.getPreferenceArray();
        int score = 0;
        
        for(int i = 0; i<sPrefArray.length; ++i){
            score+= Math.abs(sPrefArray[i] - tPrefArray[i]);
        }
        return score;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void matchDoubleStudents(){
        //calculate everyone's best matches first 
        for(int i = 0;  i<students.size(); ++i){
            int score = 100;
            Student s = students.get(i);
            Student bestMatch = null;

            if(s.isMatched() || s.hasRoom()) continue;
            for(int j = i+1; j<students.size(); ++j){
                Student t = students.get(j);
                if(t.isMatched() || t.hasRoom()) continue;
                if(!s.getGender().equals(t.getGender())) continue;//opposite genders init

                int matchScore = calculateMatchScore(s, t);
                if(matchScore < score){
                    bestMatch = t;
                    score = matchScore;
                }
            }

            //at this point we have the best match 
            if(bestMatch!= null){
                bestMatch.setMatched();
                s.setMatched();
            }
            pairs.add(new AbstractMap.SimpleEntry(s, bestMatch));

        }
    }

    private void handleMatchedPairs(){
        for(int i = 0; i<pairs.size(); ++i){
            Student s = pairs.get(i).getKey();
            Student t = pairs.get(i).getValue();
            if(t == null) continue;

            for(int j = 0; j< hostels.length; ++j){
                
                // they want to be on campus but the hostel is an off campus hostel
                if(s.getOffCampusOrOn().ordinal() == 0 && hostels[j].isOffCampus()) continue;

                // they want to be off campus and the hostel is onn campus
                if(s.getOffCampusOrOn().ordinal() == 2 && !hostels[j].isOffCampus()) continue;

                Room[][] rooms = hostels[j].getRooms();

                for(int a = 0; a<rooms.length; a++){
                    boolean foundRoom = false;

                    for(int b = 0; b<rooms[a].length; b++){
                        if(rooms[a][b] instanceof DoubleRoom && rooms[a][b].isEmpty()){
                            rooms[a][b].addOccupant(s);
                            rooms[a][b].addOccupant(t);
                            foundRoom = true;
                            s.setRoomed();
                            t.setRoomed();
                            break;
                        }
                    }

                    if(foundRoom) break;
                    
                }


            }
        }
    }

    private void matchQuads(){
        for(int i = 0; i<pairs.size(); ++i){

            Student s = pairs.get(i).getKey();
            if(s.hasRoom()) continue;

            Map.Entry<Student, Student> bestMatchedPair = null;
            int score = 100;

            for(int j  = i+1; j<pairs.size(); ++j){
                Student a = pairs.get(j).getValue();
                if(a.hasRoom()) continue;
                if(!s.getGender().equals(a.getGender())) continue; //opposite genders init

                int matchScore = calculateMatchScore(s, a);
                if(matchScore < score){
                    score = matchScore;
                    bestMatchedPair = pairs.get(j);
                }
            }
            //we've gotten the matched pair now
            //let's put them in a room
            findQuadrupleRoom(pairs.get(i), bestMatchedPair);

        }
    }

    private void findQuadrupleRoom(Map.Entry<Student, Student> pair1, Map.Entry<Student, Student> pair2){
        Student s = pair1.getKey();
        if(pair1.getValue() == null) return;
        Student t = pair1.getValue();
        Student a = (pair2 == null)? null: pair2.getKey();
        Student b = (pair2 == null)? null: pair2.getValue();

        for(int i = 0; i<hostels.length; i++){
            // they want to be on campus but the hostel is an off campus hostel
            if(s.getOffCampusOrOn().ordinal() == 0 && hostels[i].isOffCampus()) continue;

            // they want to be off campus and the hostel is onn campus
            if(s.getOffCampusOrOn().ordinal() == 2 && !hostels[i].isOffCampus()) continue;
            Room[][] rooms = hostels[i].getRooms();

            for(int j = 0; j<rooms.length; j++){

                boolean foundRoom = false;

                for(int k = 0; k<rooms[k].length; k++){
                    if(rooms[j][k] instanceof QuadrupleRoom && rooms[j][k].isEmpty()){
                        Room room = rooms[j][k];
                        room.addOccupant(s); s.setRoomed();
                        room.addOccupant(t); t.setRoomed();
                        room.addOccupant(a); a.setRoomed();
                        room.addOccupant(b); b.setRoomed();
                        foundRoom = true;
                        break;
                    }
                }
                if(foundRoom) break;
            }
        }
    }

    private void matchTheUnmatched(){
        System.out.println(students.size());
        for(int i = 0; i< students.size(); i++){
            if(students.get(i).hasRoom()) continue;
            Student s = students.get(i);
            boolean foundRoom = false;
            for(int j = 0; j<hostels.length; j++){
                // they want to be on campus but the hostel is an off campus hostel
                if(s.getOffCampusOrOn().ordinal() == 0 && hostels[j].isOffCampus()) continue;

                // they want to be off campus and the hostel is onn campus
                if(s.getOffCampusOrOn().ordinal() == 2 && !hostels[j].isOffCampus()) continue;

                Room[][] rooms = hostels[i].getRooms();
                
                for(int a = 0; a<rooms.length; a++){
                    for(int b = 0; b<rooms[a].length; b++){
                        Room room = rooms[a][b];
                        if(room.isFull()) continue;
                        if(!room.isEmpty()){
                            if(!room.getOccupants()[0].getGender().equals(s.getGender())) continue;
                        }
                        foundRoom = room.addOccupant(s); //this is lazy

                        s.setRoomed();
                        if(foundRoom) break;
                    }
                    if(foundRoom) break;
                }
            }
            if(foundRoom) break;
        }
    }

    public void matchStudents() {
        // match students
        //step one put people in single rooms
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
    }

    public void displayMatches() {
        for(int i =0; i<hostels.length; i++){
            hostels[i].displayRooms();
        }
    }

    private void initHostels() {
        // do something here to initialize
        // the hostels
        hostels = new Hostel[1]; //temp change for testing purposes

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

        hostels[0] = new Hostel("New Hosanna", N_HOS_TOTAL_ROOMS, newHosannaRooms, false, true, true, N_HOS_FLOORS);
       // hostels[1] = new Hostel("New Hosanna On campus", N_HOS_TOTAL_ROOMS, newHosannaRooms, true, true, true, N_HOS_FLOORS);//temp change for Tani test
    }

    private void getStudents() {
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

    public static void main(String[] args) {
        MatchingSystem matcher = new MatchingSystem();
        matcher.matchStudents();
    }
}
