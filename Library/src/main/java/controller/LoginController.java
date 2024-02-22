package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import service.user.UserService;
import view.LoginView;

import static database.Constants.Roles.CUSTOMER;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final Stage primaryStage;
    private Long id;
    private final CustomerController customerController;
    private final AdminController adminController;
    private final EmployeeController employeeController;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, UserService userService, Stage primaryStage, CustomerController customerController, AdminController adminController, EmployeeController employeeController){
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.primaryStage = primaryStage;
        this.customerController = customerController;
        this.adminController = adminController;
        this.employeeController = employeeController;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());

        this.customerController.addExitButtonListener(new ExitButtonListener());
        this.adminController.addExitButtonListener(new ExitButtonListener());
        this.employeeController.addExitButtonListener(new ExitButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification  = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("LogIn Successfull!");
                id = authenticationService.getIdFromUsername(username);

                if(userService.isCustomer(id)) {
                    customerController.setCurrentUser(id);
                    customerController.setTableData();
                    customerController.setScene(primaryStage);
                } else if(userService.isEmployee(id)) {
                    employeeController.setScene(primaryStage);
                    employeeController.setCurrentUser(id);
                } else if(userService.isAdmin(id)) {
                    adminController.setScene(primaryStage);
                }
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password, CUSTOMER);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }

    private class ExitButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            id = (long) -1;
            loginView.setScene(primaryStage);
        }
    }

    public void setScene(){
        loginView.setScene(primaryStage);
    }
}