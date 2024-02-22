package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Sale;
import model.User;
import model.validator.Notification;
import service.sale.SaleService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;

import java.util.List;

import static database.Constants.Roles.EMPLOYEE;

public class AdminController {
    private final AdminView adminView;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final SaleService saleService;
    private TableView<Sale> saleTable;

    public AdminController(AdminView adminView, UserService userService, AuthenticationService authenticationService, SaleService saleService) {
        this.adminView = adminView;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.saleService = saleService;
        saleTable = new TableView<>();

        this.adminView.addAddEmployeeButtonListener(new AddEmployeeButtonListener());
        this.adminView.addDownloadReportButtonListener(new DownloadReportButtonListener());
    }

    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener){
        adminView.addExitButtonListener(exitButtonListener);
    }

    public void setTableData(){
        List<User> employees = userService.getEmployees();
        adminView.setDataInTable(employees);
    }

    public void setScene(Stage primaryStage){
        adminView.setScene(primaryStage);
        setTableData();
    }

    private void setSaleTable(){
        TableColumn<Sale, String> employee = new TableColumn<>("Employee");
        employee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        employee.setMinWidth(200);
        TableColumn<Sale, String> customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        customer.setMinWidth(200);
        TableColumn<Sale, String> book = new TableColumn<>("Book Title");
        book.setCellValueFactory(new PropertyValueFactory<>("title"));
        book.setMinWidth(200);

        saleTable.getColumns().addAll(employee, customer, book);

        List<Sale> sales = saleService.findAll();
        ObservableList<Sale> saleList = FXCollections.observableArrayList(sales);
        saleTable.getItems().clear();
        saleTable.setItems(saleList);
    }

    private class AddEmployeeButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            Notification<Boolean> createEmployeeNotification = authenticationService.register(username, password, EMPLOYEE);

            if (createEmployeeNotification.hasErrors()) {
                adminView.setActionTargetText(createEmployeeNotification.getFormattedErrors());
            } else {
                adminView.setActionTargetText("Employee Created Successfully!");
                setTableData();
            }
        }
    }

    private class DownloadReportButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            setSaleTable();

            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
            PrinterJob job = PrinterJob.createPrinterJob();
            if(job != null) {
                job.printPage(pageLayout, saleTable);
                job.endJob();
            }
        }
    }

}
