public class Hostel{
    //Declaring the instance variables of the hostel class
    private String name;
    private int numRooms;
    private Room[][] rooms;
    private boolean offCampus;
    private int numFloors;

    //hostel constructor
    public Hostel(String name, int numRooms, Room[][] rooms,  boolean offCampus, boolean hasParking, boolean allowsCooking, int numFloors) {
        this.name = name;
        this.numRooms = numRooms;
        this.rooms = rooms;
        this.offCampus = offCampus;
        this.numFloors = numFloors;
    }

    public String getName(){
        return name;
    }

    public int getnumRooms(){
        return numRooms;
    }

    public Room[][] getRooms(){
        return rooms;
    }
    public boolean offCampus(){
        return offCampus;
    }

    public int numFloors(){
        return numFloors;
    }

}
