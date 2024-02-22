package service.user;

import model.User;
import repository.user.UserRepository;

import java.util.List;

import static database.Constants.Roles.*;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getEmployees() {
        return userRepository.getUsersByRole(EMPLOYEE);
    }

    @Override
    public List<User> getCustomers() {
        return userRepository.getUsersByRole(CUSTOMER);
    }

    @Override
    public List<User> getAdmins() {
        return userRepository.getUsersByRole(ADMINISTRATOR);
    }

    @Override
    public boolean isEmployee(Long id) {
        List<User> users = getEmployees();
        for(User u : users)
            if(u.getId().equals(id))
                return true;
        return false;
    }

    @Override
    public boolean isCustomer(Long id) {
        List<User> users = getCustomers();
        for(User u : users)
            if(u.getId().equals(id))
                return true;
        return false;
    }

    @Override
    public boolean isAdmin(Long id) {
        List<User> users = getAdmins();
        for(User u : users)
            if(u.getId().equals(id))
                return true;
        return false;
    }


}
