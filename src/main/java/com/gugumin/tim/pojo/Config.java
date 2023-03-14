package com.gugumin.tim.pojo;

import lombok.Data;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author minmin
 * @date 2023/03/14
 */
@Data
public class Config {
    private static final String CONFIG_PATH = "GuGu_Typora_Image_Magic";
    private static final String CONFIG_FILE_NAME = "config.yml";

    private Request request;
    private Response response;

    public static Config load() {
        Path configPath = getConfigPath();
        return filling(configPath);
    }

    @SneakyThrows
    private static Config filling(Path configPath) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(new FileInputStream(configPath.toFile()), Config.class);
    }

    private static Path getConfigPath() {
        String dir = System.getProperty("user.home");
        Path path = Path.of(dir, CONFIG_PATH, CONFIG_FILE_NAME);
        if (Files.notExists(path)) {
            writeDefaultConfig(path);
        }
        return path;
    }

    @SneakyThrows
    private static void writeDefaultConfig(Path path) {
        Files.createDirectories(path.getParent());
        try(FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            InputStream resourceAsStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)){
            assert resourceAsStream != null;
            fileOutputStream.write(resourceAsStream.readAllBytes());
        }
    }
}
