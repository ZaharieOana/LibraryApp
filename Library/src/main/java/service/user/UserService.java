package service.user;

import model.User;

import java.util.List;

public interface UserService {
    List<User> getEmployees();
    List<User> getCustomers();
    List<User> getAdmins();
    boolean isEmployee(Long id);
    boolean isCustomer(Long id);
    boolean isAdmin(Long id);
}
