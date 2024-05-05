package uz.pdp.bot.repository;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.bot.model.TempData;

import javax.management.ObjectName;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TempDataRepository {
    @Getter
    private static final TempDataRepository instance = new TempDataRepository();
    private TempDataRepository() {
    }

    public void createData(TempData<?> tempData){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        allTempDataFromFile.removeIf(temp -> temp.getCreator().equals(GlobalVar.getUSER().getId()) && temp.getKey().equals(tempData.getKey()));
        allTempDataFromFile.add(tempData);
        setAllTempDataToFile(allTempDataFromFile);
    }

    public TempData<?> getAndDelete(String key){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        for (TempData<?> tempData : allTempDataFromFile) {
            if (tempData.getCreator().equals(GlobalVar.getUSER().getId()) && tempData.getKey().equals(key)) {
                allTempDataFromFile.remove(tempData);
                setAllTempDataToFile(allTempDataFromFile);
                return tempData;
            }
        }
        return null;
    }

    public TempData<?> get(String key){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        for (TempData<?> tempData : allTempDataFromFile) {
            if (tempData.getCreator().equals(GlobalVar.getUSER().getId()) && tempData.getKey().equals(key)) {
                return tempData;
            }
        }
        return null;
    }

    public void deleteData(String key){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        allTempDataFromFile.removeIf(tempData -> tempData.getCreator().equals(GlobalVar.getUSER().getId()) && tempData.getKey().equals(key));
        setAllTempDataToFile(allTempDataFromFile);
    }

    public void deleteMyAllData(){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        allTempDataFromFile.removeIf(tempData -> tempData.getCreator().equals(GlobalVar.getUSER().getId()));
        setAllTempDataToFile(allTempDataFromFile);
    }

    public boolean contains(String key){
        List<TempData<?>> allTempDataFromFile = getAllTempDataFromFile();
        return allTempDataFromFile.stream().anyMatch(tempData -> tempData.getCreator().equals(GlobalVar.getUSER().getId()) && tempData.getKey().equals(key));
    }

    @NonNull
    private List<TempData<?>> getAllTempDataFromFile(){
        try (
                FileInputStream fileInputStream = new FileInputStream(FIleURLS.TEMP_DATA);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ){
            //noinspection unchecked
            return (List<TempData<?>>) objectInputStream.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void setAllTempDataToFile(List<TempData<?>> data){
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(FIleURLS.TEMP_DATA);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
