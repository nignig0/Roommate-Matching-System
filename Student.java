import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Student {
    enum Gender {
        FEMALE, MALE
    };

    enum Preference {
        NEVER, NEUTRAL, ALOT
    };

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
    private boolean roomed;

    public Student(String id, String gender, String email, int likesLoudMusic, int likesHavingPeopleOver,
            int sleepsDeeply, int likesTidySpace, int likesToSleepWithLightsOff, int howMuchPersonalSpace,
            int offCampusOrOn) {

        this.id = id;
        this.gender = (gender.equalsIgnoreCase("F")) ? Gender.FEMALE : Gender.MALE;
        this.email = email;
        this.likesLoudMusic = Preference.values()[likesLoudMusic];
        this.likesHavingPeopleOver = Preference.values()[likesHavingPeopleOver];
        this.sleepsDeeply = Preference.values()[sleepsDeeply];
        this.likesTidySpace = Preference.values()[likesTidySpace];
        this.likesToSleepWithLightsOff = Preference.values()[likesToSleepWithLightsOff];
        this.howMuchPersonalSpace = Preference.values()[howMuchPersonalSpace];
        this.offCampusOrOn = Preference.values()[offCampusOrOn];

        matched = false;
        roomed = false;

    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Preference getLikesLoudMusic() {
        return this.likesLoudMusic;
    }

    public Preference getLikesHavingPeopleOver() {
        return this.likesHavingPeopleOver;
    }

    public Preference getSleepsDeeply() {
        return this.sleepsDeeply;
    }

    public Preference getLikesTidySpace() {
        return this.likesTidySpace;
    }

    public Preference getLikesToSleepWithLightsOff() {
        return this.likesToSleepWithLightsOff;
    }

    public Preference getHowMuchPersonalSpace() {
        return this.howMuchPersonalSpace;
    }

    public Preference getOffCampusOrOn() {
        return this.offCampusOrOn;
    }

    public void setMatched() {
        this.matched = !matched;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setRoomed() {
        this.roomed = !roomed;
    }

    public boolean hasRoom() {
        return roomed;
    }

    public int[] getPreferenceArray() {
        int[] preferenceArray = {
                this.likesLoudMusic.ordinal(),
                this.likesHavingPeopleOver.ordinal(),
                this.sleepsDeeply.ordinal(),
                this.likesTidySpace.ordinal(),
                this.howMuchPersonalSpace.ordinal(),
                this.offCampusOrOn.ordinal()
        };
        return preferenceArray;
    }

    public String toString() {
        String generalInformation = String.format("id: %s\ngender: %s\nemail: %s\nis student matched: %s\n", this.id,
                this.gender, this.email, this.matched);

        String preferences1 = String.format(
                "likes loud music: %s\nlikes having people over: %s\nsleeps deeply: %s\nlikes a tidy space: %s\n",
                this.likesLoudMusic, this.likesHavingPeopleOver, this.sleepsDeeply, this.likesTidySpace);

        String preferences2 = String.format(
                "likes to sleep with lights off: %s\npersonal space: %s\nprefers to be off campus: %s",
                this.likesToSleepWithLightsOff, this.howMuchPersonalSpace, this.offCampusOrOn);

        return generalInformation + preferences1 + preferences2;
    }
}