package launcher;

import controller.AdminController;
import controller.CustomerController;
import controller.EmployeeController;
import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.sale.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import view.AdminView;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final CustomerView customerView;
    private final CustomerController customerController;
    private final AdminView adminView;
    private final AdminController adminController;
    private final EmployeeView employeeView;
    private final EmployeeController employeeController;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final UserService userService;
    private final SaleService saleService;
    private static ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if (instance == null){
            instance = new ComponentFactory(componentsForTests, stage);
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();

        //repo
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        //service
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.bookService = new BookServiceImpl(new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(connection),
                new Cache<>()
        ), new SaleRepositoryMySQL(connection));
        this.userService = new UserServiceImpl(userRepository);
        this.saleService = new SaleServiceImpl(new SaleRepositoryMySQL(connection));

        //views
        this.loginView = new LoginView();
        this.customerView = new CustomerView();
        this.adminView = new AdminView();
        this.employeeView = new EmployeeView();

        //controllers
        this.customerController = new CustomerController(customerView, bookService, userService);
        this.adminController = new AdminController(adminView, userService, authenticationService, saleService);
        this.employeeController = new EmployeeController(employeeView, bookService, saleService);
        this.loginController = new LoginController(loginView, authenticationService, userService, stage, customerController, adminController, employeeController);

        loginController.setScene();
        stage.setTitle("Book Store");
        stage.show();

    }

    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository(){
        return rightsRolesRepository;
    }

    public LoginView getLoginView(){
        return loginView;
    }

    public BookService getBookService(){
        return bookService;
    }

    public LoginController getLoginController(){
        return loginController;
    }

}