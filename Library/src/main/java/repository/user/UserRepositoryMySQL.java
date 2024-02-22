package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next()){
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setSalt(userResultSet.getString("salt"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setString(3, user.getSalt());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ?");
            preparedStatement.setString(1, email);
            ResultSet userResultSet = preparedStatement.executeQuery();

            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getIdFromUsername(String username) {
        Long id = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ?");
            preparedStatement.setString(1, username);
            ResultSet userResultSet = preparedStatement.executeQuery();
            userResultSet.next();
            id = userResultSet.getLong("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public String getUserSalt(String username) {
        String salt = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ?");
            preparedStatement.setString(1, username);
            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next())
                salt = userResultSet.getString("salt");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salt;
    }

    @Override
    public List<User> getUsersByRole(String userRole) {
        String sql = "SELECT user.id, user.username FROM user, user_role, role " +
                "WHERE user.id = user_role.user_id and user_role.role_id = role.id and role.role = '" + userRole + "'";
        List<User> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                employees.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong(1))
                .setUsername(resultSet.getString(2))
                .build();
    }

}