package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthRepositoryImpl implements BaseRepository<User, Long> {
    @Getter
    private static final AuthRepositoryImpl instance = new AuthRepositoryImpl();

    private AuthRepositoryImpl() {
    }
    @Override
    public Boolean save(User user) {
        List<User> allUsersFromFile = getAllUsersFromFile();
        allUsersFromFile.add(user);
        setAllUsersToFile(allUsersFromFile);
        return true;
    }

    @Override
    public Boolean update(User user) {
        List<User> collect = getAllUsersFromFile().stream().map(temp -> {
            if (temp.getId().equals(user.getId()))
                return user;
            return temp;
        }).collect(Collectors.toList());

        setAllUsersToFile(collect);
        return true;
    }

    @Override
    public List<User> findAll() {
        return getAllUsersFromFile();
    }

    @Override
    public Optional<User> findById(Long id) {
        return getAllUsersFromFile().stream().filter(user -> Objects.equals(user.getId(), id)).findFirst();
    }

    @NonNull
    private List<User> getAllUsersFromFile(){
        List<User> load = FileHelper.load(FIleURLS.USERS, new TypeToken<List<User>>() {}.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllUsersToFile(List<User> data){
        FileHelper.write(FIleURLS.USERS, data);
    }
}
