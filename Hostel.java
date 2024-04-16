public class Hostel {
    // Declaring the instance variables of the hostel class
    private String name;
    private int numRooms; // total number of rooms in the hostel
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

    /**
     * @return returns the name of the hostel
     */
    public String getName() {
        return name;
    }

    /**
     * @return returns the number of rooms in the hostel
     */
    public int getNumRooms() {
        return numRooms;
    }

    /**
     * @return returns the  rooms in the hostel
     */
    public Room[][] getRooms() {
        return rooms;
    }

    /**
     * @return returns the number of floors in the hostel
     */
    public int numFloors() {
        return numFloors;
    }

    /**
     * @return returns true if the campus is off campus
     */
    public boolean isOffCampus() {
        return this.offCampus;
    }

    /**
     * helper method to help display rooms
     */
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
