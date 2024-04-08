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
    private Preference likesTalkingToRoommate;

    public Student(String id, String gender, String email, int likesLoudMusic, int likesHavingPeopleOver,
                int sleepsDeeply, int likesTidySpace, int likesToSleepWithLightsOff, int howMuchPersonalSpace,
                int likesTalkingToRoommate){

                    this.id = id;
                    this.gender  = (gender.equalsIgnoreCase("F"))? Gender.FEMALE : Gender.MALE;
                    this.email = email;
                    this.likesLoudMusic = Preference.values()[likesLoudMusic];
                    this.likesHavingPeopleOver = Preference.values()[likesHavingPeopleOver];
                    this.sleepsDeeply = Preference.values()[sleepsDeeply];
                    this.likesTidySpace = Preference.values()[likesTidySpace];
                    this.likesToSleepWithLightsOff = Preference.values()[likesToSleepWithLightsOff];
                    this.howMuchPersonalSpace = Preference.values()[howMuchPersonalSpace];
                    this.likesTalkingToRoommate = Preference.values()[likesTalkingToRoommate];

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

    public Preference getLikesTalkingToRoommate(){
        return this.likesTalkingToRoommate;
    }

    public String toString(){
        String generalInformation = String.format("id: %s\ngender: %s\nemail: %s\n", this.id, this.gender, this.email);

        String preferences1 = String.format(
        "likes loud music: %s\nlikes having people over: %s\nsleeps deeply: %s\nlikes a tidy space: %s",
        this.likesLoudMusic, this.likesHavingPeopleOver, this.sleepsDeeply, this.likesTidySpace);

        String preferences2 = String.format(
        "likes to sleep with lights off: %s\npersonal space: %s\nlikes talking to roommate: %s",
        this.likesToSleepWithLightsOff, this.howMuchPersonalSpace, this.likesTalkingToRoommate);

        return generalInformation+preferences1+preferences2;
    }

    public static void main(String[] args) {
        Student Tani = new Student("64582026", "M", "tanitoluwa.adebayo@ashesi.edu.gh",
         0, 0, 0, 0, 0, 0, 0);
         System.out.println(Tani);
    }

}