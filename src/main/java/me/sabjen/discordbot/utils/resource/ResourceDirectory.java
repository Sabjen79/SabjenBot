package me.sabjen.discordbot.utils.resource;

import me.sabjen.discordbot.utils.RandomUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

class ResourceDirectory {
    private final File file;

    public ResourceDirectory(String path) {
        file = new File(ResourceManager.getInstance().getPath() + path);

        if(Files.exists(file.toPath())) return;

        try {
            Files.createDirectories(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getRandomFile(String search) {
        if(search == null || search.equals("")) {
            return RandomUtil.randomFrom(file.listFiles());
        }

        var files = file.listFiles();
        if(files == null) return null;

        return RandomUtil.randomFrom(
                Arrays.stream(files).filter((f) -> f.getName().contains(search)).toList()
        );
    }
}
