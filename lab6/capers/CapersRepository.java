package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");
    // TODO Hint: look at the `join`
    //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        // TODO to prepare the persistence dir
        File cappers = CapersRepository.CAPERS_FOLDER;
        if(!cappers.exists()){
            cappers.mkdir();
        }
        File dogs = Dog.DOG_FOLDER;
        if(!dogs.exists()){
            dogs.mkdir();
        }


    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) throws IOException {
        // TODO 1 find the directory location
        //we need the txt been restored on .capers/story
        //story is a file store the story
        File story = Utils.join(CAPERS_FOLDER,"story");
        try{
            if(!story.exists()){
                if (!story.createNewFile()) {
                    System.out.println("文件创建失败: " + story.getAbsolutePath());
                }
            }
            String previousText = Utils.readContentsAsString(story);
            if(!previousText.isBlank()){
                text = previousText + '\n' + text;
            }
//            Utils.writeContents(story,previousText, text);
//            text += System.lineSeparator();
            Utils.writeContents(story,text);
            System.out.println(Utils.readContentsAsString(story));
        }catch (IOException e){
            System.out.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        Dog dog = new Dog(name, breed, age);
        dog.saveDog();
        System.out.println(dog);
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        //get the target object
        Dog birthdayDog = Utils.readObject(Utils.join(Dog.DOG_FOLDER,name), Dog.class);
        birthdayDog.haveBirthday();
        //save the change object
        birthdayDog.saveDog();
    }
}
