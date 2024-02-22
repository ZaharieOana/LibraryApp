package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Collections;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password, String role) {
//        String encodedPassword = hashPassword(password);
        //Criptare  messaj -> dasjdaskdasjdasjk -> messaj
        //Hashing parolasimpla2023! -> ajdsahduyadgasdashfaj8h8hbh
        Role customerRole = rightsRolesRepository.findRoleByTitle(role);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        boolean alreadyExists = userRepository.existsByUsername(user.getUsername());

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();


        if(alreadyExists){
            userRegisterNotification.addError("User already exists!");
            userRegisterNotification.setResult(Boolean.FALSE);
        } else if(!userValid){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            String salt = makeSalt();
            user.setSalt(salt);
            user.setPassword(hashPassword(password+salt));
            userRegisterNotification.setResult(userRepository.save(user));
        }

        return userRegisterNotification;
    }

    private String makeSalt(){
        String chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        SecureRandom r = new SecureRandom();

        String salt = r
                .ints(20, 0, chrs.length()) // 9 is the length of the string you want
                .mapToObj(i -> chrs.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return salt;
    }

    @Override
    public Notification<User> login(String username, String password) {
        String salt = userRepository.getUserSalt(username);
        return userRepository.findByUsernameAndPassword(username, hashPassword(password+salt));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public Long getIdFromUsername(String username) {
        return userRepository.getIdFromUsername(username);
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}