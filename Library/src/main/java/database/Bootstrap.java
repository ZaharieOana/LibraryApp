package database;

import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.sale.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static database.Constants.Rights.*;
import static database.Constants.Roles.*;
import static database.Constants.Schemas.PRODUCTION;
import static database.Constants.Schemas.SCHEMAS;
import static database.Constants.getRolesRights;

// Script - code that automates some steps or processes

public class Bootstrap {

    private static RightsRolesRepository rightsRolesRepository;

    public static void main(String[] args) throws SQLException {
        dropAll();

        bootstrapTables();

        bootstrapUserData();

        bootstrapPopulateBook();
        bootstrapPopulateUser();
    }

    private static void dropAll() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Dropping all tables in schema: " + schema);

            Connection connection = new JDBConnectionWrapper(schema).getConnection();
            Statement statement = connection.createStatement();

            String[] dropStatements = {
                    "TRUNCATE `role_right`;",
                    "DROP TABLE `role_right`;",
                    "TRUNCATE `right`;",
                    "DROP TABLE `right`;",
                    "TRUNCATE `user_role`;",
                    "DROP TABLE `user_role`;",
                    "TRUNCATE `role`;",
                    "TRUNCATE `sale`;",
                    "DROP TABLE `sale`;",
                    "DROP TABLE  `book`, `role`, `user`;"
            };

            Arrays.stream(dropStatements).forEach(dropStatement -> {
                try {
                    statement.execute(dropStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();

        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping " + schema + " schema");


            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            Connection connection = connectionWrapper.getConnection();

            Statement statement = connection.createStatement();

            for (String table : Constants.Tables.ORDERED_TABLES_FOR_CREATION) {
                String createTableSQL = sqlTableCreationFactory.getCreateSQLForTable(table);
                statement.execute(createTableSQL);
            }
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapUserData() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping user data for " + schema);

            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            rightsRolesRepository = new RightsRolesRepositoryMySQL(connectionWrapper.getConnection());

            bootstrapRoles();
            bootstrapRights();
            bootstrapRoleRight();
            bootstrapUserRoles();
        }
    }

    private static void bootstrapRoles() throws SQLException {
        for (String role : ROLES) {
            rightsRolesRepository.addRole(role);
        }
    }

    private static void bootstrapRights() throws SQLException {
        for (String right : RIGHTS) {
            rightsRolesRepository.addRight(right);
        }
    }

    private static void bootstrapRoleRight() throws SQLException {
        Map<String, List<String>> rolesRights = getRolesRights();

        for (String role : rolesRights.keySet()) {
            Long roleId = rightsRolesRepository.findRoleByTitle(role).getId();

            for (String right : rolesRights.get(role)) {
                Long rightId = rightsRolesRepository.findRightByTitle(right).getId();

                rightsRolesRepository.addRoleRight(roleId, rightId);
            }
        }
    }

    private static void bootstrapUserRoles() throws SQLException {

    }

    private static void bootstrapPopulateBook() throws SQLException{
        Book book1 = new BookBuilder()
                .setAuthor("Cezar Petrescu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(1931, 6, 2))
                .setAmount(12)
                .build();
        Book book2 = new BookBuilder()
                .setAuthor("Stephen King")
                .setTitle("IT")
                .setPublishedDate(LocalDate.of(1986, 9, 15))
                .setAmount(24)
                .build();

        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(PRODUCTION);
        Connection connection = connectionWrapper.getConnection();

        BookService bookService = new BookServiceImpl(new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(connectionWrapper.getConnection()),
                new Cache<>()
        ), new SaleRepositoryMySQL(connectionWrapper.getConnection()));

        bookService.save(book1);
        bookService.save(book2);

    }

    private static void bootstrapPopulateUser() throws SQLException {
        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(PRODUCTION);
        Connection connection = connectionWrapper.getConnection();

        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(new UserRepositoryMySQL(connection, rightsRolesRepository),rightsRolesRepository);

        authenticationService.register("admin1@admin.com", "admin1@admin.com", ADMINISTRATOR);
        authenticationService.register("employee1@e.com", "employee1@e.com", EMPLOYEE);
        authenticationService.register("employee2@e.com", "employee2@e.com", EMPLOYEE);
        authenticationService.register("customer1@c.com", "customer1@c.com", CUSTOMER);
    }

}