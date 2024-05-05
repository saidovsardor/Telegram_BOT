package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.CourierApplication;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.*;
import java.util.stream.Collectors;

public class CourierApplicationRepositoryImpl implements BaseRepository<CourierApplication, Long> {
    @Getter
    private static final CourierApplicationRepositoryImpl instance = new CourierApplicationRepositoryImpl();


    @Override
    public Boolean save(CourierApplication courier) {
        List<CourierApplication> allCouriersFromFile = getAllCourierFromFile();
        allCouriersFromFile.add(courier);
        setAllCouriersFromFile(allCouriersFromFile);
        return true;
    }

    @Override
    public Boolean update(CourierApplication courier) {
        List<CourierApplication> collect = getAllCourierFromFile().stream().map(temp -> {
            if (temp.getOwnerId()==(courier.getOwnerId()))
                return courier;
            return temp;
        }).collect(Collectors.toList());
        setAllCouriersFromFile(collect);
        return true;
    }

    @Override
    public List<CourierApplication> findAll() {
        return getAllCourierFromFile();
    }

    @Override
    public Optional<CourierApplication> findById(Long id) {
        return getAllCourierFromFile().stream().filter(temp -> temp.getId().equals(id)).findFirst();

    }
    @NonNull
    private List<CourierApplication> getAllCourierFromFile() {
        List<CourierApplication> load = FileHelper.load(FIleURLS.COURIER, new TypeToken<List<CourierApplication>>() {
        }.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllCouriersFromFile(List<CourierApplication> data) {
        FileHelper.write(FIleURLS.COURIER, data);
    }
}
