package uz.pdp.backend.service;

import uz.pdp.backend.utils.GlobalVar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileHelper {
    public static <E> List<E> load(String url, Type type){
        try {
            String json = new String(Files.readAllBytes(Path.of(url)));
            return GlobalVar.GSON.fromJson(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> void write(String url, List<E> data){
        try {
            String json = GlobalVar.GSON.toJson(data);
            Files.writeString(Path.of(url), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
