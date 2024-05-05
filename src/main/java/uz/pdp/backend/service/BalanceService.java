package uz.pdp.backend.service;

import javassist.NotFoundException;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.exceptions.AlreadyExistsException;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Balance;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.repository.impl.BalanceRepositoryImpl;
import uz.pdp.backend.repository.impl.BusinessRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public class BalanceService {

    private static  final BalanceService instance = new BalanceService();
    private static final BalanceRepositoryImpl balanceRepository = BalanceRepositoryImpl.getInstance();
    private static final BusinessRepositoryImpl businessRepository = BusinessRepositoryImpl.getInstance();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final long defaultCardNumber = 8600_0000_0000_0000L + balanceRepository.findAll().size();

    private BalanceService() {}
    public Balance createBalance(Long ownerId) throws UserNotFoundException, NotFoundException {
        Optional<User> user = authRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not Found!");
        }
        List<Balance> balances = balanceRepository.findAll();
        Optional<Balance> first = balances.stream().filter(temp -> temp.getOwnerId().equals(ownerId) && !temp.isDelete()).findFirst();
        if (first.isPresent()){
            throw new NotFoundException("You have another balance!");
        }
        Balance balance = new Balance(defaultCardNumber + "", ownerId, 0L);
        balanceRepository.save(balance);
        return balance;
    }
    public Balance addMoney(UUID balanceId, Long money) throws NotFoundException {
        Optional<Balance> balance = balanceRepository.findById(balanceId);
        if (balance.isEmpty()){
            throw new NotFoundException("Balance not found!");
        }
        Balance newBalance = balance.get();
        newBalance.setBalance(newBalance.getBalance()+money);
        balanceRepository.update(newBalance);
        return newBalance;
    }
    public boolean transferMoney(UUID fromBalanceId, UUID toBalanceId, Long money) throws NotFoundException {
        Optional<Balance> fromBalance = balanceRepository.findById(fromBalanceId);
        if (fromBalance.isEmpty()) {
            throw new NotFoundException("Sender balance not found!");
        }
        Optional<Balance> toBalance = balanceRepository.findById(toBalanceId);
        if (toBalance.isEmpty()) {
            throw new NotFoundException("Receiver balance not found!");
        }
        Balance from = fromBalance.get();
        Balance to = toBalance.get();
        if(from.getBalance()< money){
            throw new IllegalArgumentException("You do not have enough money!");
        }
        from.setBalance(from.getBalance() - money);
        to.setBalance(to.getBalance() + money);
        balanceRepository.update(from);
        balanceRepository.update(to);
        return true;
    }
    public Business addBalance(Long userId, UUID businessId, String cardNumber) throws WrongRoleException, AlreadyExistsException {
        User user = authRepository.findById(userId).get();
        if(user.getRole() != Role.BUSINESSMAN)
            throw new WrongRoleException("You are not Businessman");

        Business business = businessRepository.findById(businessId).get();
        if(business.getBalanceCardNumber() != null)
            throw new AlreadyExistsException("You already have balance!");

        business.setBalanceCardNumber(cardNumber);
        businessRepository.update(business);

        return business;
    }
}

