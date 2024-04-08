abstract public class Room {
    protected String roomNum;
    protected int numBeds;
    protected Student[] occupants;

    public Room(int numBeds, String roomNum) {
        this.numBeds = numBeds;
        this.roomNum = roomNum;
        occupants = new Student[numBeds];
    }

    public int getNumBeds() {
        return numBeds;
    }

    public Student[] getOccupants() {
        // For immutable access to the occupants array
        return occupants.clone();
    }

    public String getRoomNum() {
        return roomNum;
    }

    public boolean addOccupant(Student student) {
        // Get actual number of occupants
        int occNum = 0;
        for (var occ : occupants) {
            if (occ != null) {
                occNum++;
                if (occ.getId().equals(student.getId()))
                    return false;

            }

        }

        // If the actual number of occupants is equal to the
        // number of beds then the room is full and cannot be added to
        if (occNum == numBeds)
            return false;

        for (int i = 0; i < numBeds; i++) {
            if (occupants[i] == null) {
                occupants[i] = student;
                return true;
            }
        }
        return false;
    }

    public boolean removeOccupant(Student student) {
        for (int i = 0; i < numBeds; i++) {
            if (occupants[i] != null && student.getId().equals(occupants[i].getId())) {
                occupants[i] = null;
                return true;
            }
        }
        return false;
    }
}
