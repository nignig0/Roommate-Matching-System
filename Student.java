import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Student {
    enum Gender {FEMALE, MALE};
    enum Preference {NEVER, NEUTRAL, ALOT};

    private String id;
    private Gender gender;
    private String email;
    private Preference likesLoudMusic;
    private Preference likesHavingPeopleOver;
    private Preference sleepsDeeply;
    private Preference likesTidySpace;
    private Preference likesToSleepWithLightsOff;
    private Preference howMuchPersonalSpace;
    private Preference offCampusOrOn;
    private boolean matched;

    public Student(String id, String gender, String email, int likesLoudMusic, int likesHavingPeopleOver,
                int sleepsDeeply, int likesTidySpace, int likesToSleepWithLightsOff, int howMuchPersonalSpace,
                int offCampusOrOn){

                    this.id = id;
                    this.gender  = (gender.equalsIgnoreCase("F"))? Gender.FEMALE : Gender.MALE;
                    this.email = email;
                    this.likesLoudMusic = Preference.values()[likesLoudMusic];
                    this.likesHavingPeopleOver = Preference.values()[likesHavingPeopleOver];
                    this.sleepsDeeply = Preference.values()[sleepsDeeply];
                    this.likesTidySpace = Preference.values()[likesTidySpace];
                    this.likesToSleepWithLightsOff = Preference.values()[likesToSleepWithLightsOff];
                    this.howMuchPersonalSpace = Preference.values()[howMuchPersonalSpace];
                    this.offCampusOrOn = Preference.values()[offCampusOrOn];
                    
                    matched = false;

    }

    public String getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public Preference getLikesLoudMusic(){
        return this.likesLoudMusic;
    }

    public Preference getLikesHavingPeopleOver(){
        return this.likesHavingPeopleOver;
    }

    public Preference getSleepsDeeply(){
        return this.sleepsDeeply;
    }

    public Preference getLikesTidySpace(){
        return this.likesTidySpace;
    }

    public Preference getLikesToSleepWithLightsOff(){
        return this.likesToSleepWithLightsOff;
    }

    public Preference getHowMuchPersonalSpace(){
        return this.howMuchPersonalSpace;
    }

    public Preference getOffCampusOrOn(){
        return this.offCampusOrOn;
    }

    public void toggleMatched(){
        this.matched = !matched;
    }

    public int[] getPreferenceArray(){
        return {
        this.likesLoudMusic.ordinal(), 
        this.likesHavingPeopleOver.ordinal(),
        this.sleepsDeeply.ordinal(), 
        this.likesTidySpace.ordinal(),
        this.likesToSleepWithLightsOff.ordinal(), 
        this.howMuchPersonalSpace.ordinal(),
        this.offCampusOrOn.ordinal()
        };
    }
 
    public String toString(){
        String generalInformation = String.format("id: %s\ngender: %s\nemail: %s\nis student matched: %s\n", this.id, this.gender, this.email, this.matched);

        String preferences1 = String.format(
        "likes loud music: %s\nlikes having people over: %s\nsleeps deeply: %s\nlikes a tidy space: %s\n",
        this.likesLoudMusic, this.likesHavingPeopleOver, this.sleepsDeeply, this.likesTidySpace);

        String preferences2 = String.format(
        "likes to sleep with lights off: %s\npersonal space: %s\nprefers to be off campus: %s",
        this.likesToSleepWithLightsOff, this.howMuchPersonalSpace, this.offCampusOrOn);

        return generalInformation+preferences1+preferences2;
    }

    public static void main(String[] args) {
        Student Tani = new Student("64582026", "M", "tanitoluwa.adebayo@ashesi.edu.gh",
         0, 0, 0, 0, 0, 0, 0);
         System.out.println(Tani);
         Student baseStudent = null;

         File file = new File("OOP_Project_Form.txt");
         try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                //read the student data from the csv file
                String[] data = sc.nextLine().split(",");

                //create the student
                baseStudent = new Student(
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
                System.out.println();
                System.out.println(baseStudent);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
    }

}