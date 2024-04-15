public class Hostel {
    // Declaring the instance variables of the hostel class
    private String name;
    private int numRooms; // rooms per floor
    private Room[][] rooms;
    private boolean offCampus;
    private int numFloors;

    // hostel constructor
    public Hostel(String name, int numRooms, Room[][] rooms, boolean offCampus, boolean hasParking,
            boolean allowsCooking, int numFloors) {
        this.name = name;
        this.numRooms = numRooms;
        this.rooms = rooms;
        this.offCampus = offCampus;
        this.numFloors = numFloors;
    }

    public String getName() {
        return name;
    }

    public int getnumRooms() {
        return numRooms;
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public boolean offCampus() {
        return offCampus;
    }

    public int numFloors() {
        return numFloors;
    }

    public int getNumFreeBeds() {
        int freeBeds = 0;
        for (int i = 0; i < rooms.length; ++i) {
            for (int j = 0; j < rooms[i].length; ++j) {
                freeBeds += rooms[i][j].getNumBeds() - rooms[i][j].getNumOccupants();
            }
        }
        return freeBeds;
    }

    public boolean isOffCampus() {
        return this.offCampus;
    }

    public void displayRooms() {
        System.out.printf("Hostel name: %s\n", name);
        System.out.printf("Number of rooms: %d\n", numRooms);
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                rooms[i][j].display();
            }

        }
    }

}
