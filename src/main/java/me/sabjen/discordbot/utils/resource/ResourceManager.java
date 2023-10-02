package me.sabjen.discordbot.utils.resource;

import java.io.File;

public class ResourceManager {
    public static ResourceManager getInstance() {
        if(INSTANCE == null) INSTANCE = new ResourceManager();
        return INSTANCE;
    }
    private static ResourceManager INSTANCE;
    //=========================================================================================//
    private ResourceDirectory soundsDirectory,
                              imagesDirectory;

    public File getRandomSound() { return getRandomSound(null); }
    public File getRandomSound(String search) {
        return soundsDirectory.getRandomFile(search);
    }

    public File getRandomImage() { return getRandomImage(null); }
    public File getRandomImage(String search) {
        return imagesDirectory.getRandomFile(search);
    }
    //=========================================================================================//
    public boolean initialize() {

        soundsDirectory = new ResourceDirectory("/resources/sounds/");
        imagesDirectory = new ResourceDirectory("/resources/images/");

        return (new File(getPath() + "/resources/database.mv.db")).exists(); //returns true if database exists.
    }

    private ResourceManager() {}

    public String getPath() { return new File("").getAbsolutePath(); }

    public File getFileFromPath(String path) {
        return new File(getPath() + path);
    }
}
