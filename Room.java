abstract public class Room {
    protected String roomNum;
    protected int numBeds;
    protected Student[] occupants;

    public Room(int numBeds, String roomNum) {
        this.numBeds = numBeds;
        this.roomNum = roomNum;
        occupants = new Student[numBeds];
    }

    /**
     * gets the number of beds
     * @return returns the number of beds
     */
    public int getNumBeds() {
        return numBeds;
    }

    /**
     * 
     * @return returns the occupants of the room
     */
    public Student[] getOccupants() {
        // For immutable access to the occupants array
        return occupants.clone();
    }

    /**
     * 
     * @return returns the room number 
     */
    public String getRoomNum() {
        return roomNum;
    }

    /**
     * adds a student to a room
     * @param student
     * @return returns true if this was done successfully and false otherwise
     */
    public boolean addOccupant(Student student) {
        if (student == null)
            return false;
        // Check if the student is already in the room
        for (Student occupant : occupants) {
            if (occupant != null && occupant.getId().equals(student.getId()))
                return false;
        }

        // If the actual number of occupants is equal to the
        // number of beds then the room is full and cannot be added to
        if (getNumOccupants() == numBeds)
            return false;

        for (int i = 0; i < numBeds; i++) {
            if (occupants[i] == null) {
                occupants[i] = student;
                return true;
            }
        }
        return false;
    }

    /**
     * removes an occupant from a room
     * @param student
     * @return returns true if this was done successfully and false otherwise
     */
    public boolean removeOccupant(Student student) {
        for (int i = 0; i < numBeds; i++) {
            if (occupants[i] != null && student.getId().equals(occupants[i].getId())) {
                occupants[i] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @return returns the current number of occupants of the room
     */
    public int getNumOccupants() {
        int numOccupants = 0;
        for (int i = 0; i < occupants.length; ++i) {
            if (occupants[i] != null)
                numOccupants++;
        }
        return numOccupants;
    }

    /**
     * 
     * @return returns true if the room is empty and false otherwise
     */
    public boolean isEmpty() {
        return getNumOccupants() == 0;
    }

    /**
     * @return returns true if the room is true and false otherwise 
     */
    public boolean isFull() {
        return getNumOccupants() == getNumBeds();
    }

    /**
     * helper method to display rooms
     */
    public void display() {
        System.out.printf("Room number: %s\n", this.roomNum);
        System.out.println("-----------------");
        for (int i = 0; i < occupants.length; i++) {
            if (occupants[i] == null) {
                System.out.println("Empty bed!");
            } else {
                System.out.printf("Bed %d\n", i + 1);
                System.out.printf("Student id: %s\n", occupants[i].getId());
                System.out.printf("Student email: %s\n", occupants[i].getEmail());
                System.out.println("--------------");
            }

        }
        System.out.println();
    }
}
