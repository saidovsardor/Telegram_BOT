package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.Balance;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceRepositoryImpl implements BaseRepository<Balance, UUID> {
    @Getter
    private static final BalanceRepositoryImpl instance = new BalanceRepositoryImpl();
    private BalanceRepositoryImpl(){};

    @Override
    public Boolean save(Balance balance) {
        List<Balance> allBalancesFromFile = getAllBalanceFromFile();
        allBalancesFromFile.add(balance);
        setAllBalancesFromFile(allBalancesFromFile);
        return true;
    }

    @Override
    public Boolean update(Balance balance) {
        List<Balance> collect = getAllBalanceFromFile().stream().map(temp -> {
            if (temp.getOwnerId().equals(balance.getOwnerId()))
                return balance;
            return temp;
        }).collect(Collectors.toList());
        setAllBalancesFromFile(collect);
        return true;
    }
    @Override
    public List<Balance> findAll() {
        return getAllBalanceFromFile();
    }
    @Override
    public Optional<Balance> findById(UUID id) {
        return getAllBalanceFromFile().stream().filter(balance -> Objects.equals(balance.getOwnerId(), id)).findFirst();
    }

    @NonNull
    private List<Balance> getAllBalanceFromFile() {
        List<Balance> load = FileHelper.load(FIleURLS.BALANCE, new TypeToken<List<Balance>>() {
        }.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllBalancesFromFile(List<Balance> data) {
        FileHelper.write(FIleURLS.BALANCE, data);
    }
}
