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
import model.Book;
import model.Sale;
import model.builder.BookBuilder;
import service.book.BookService;
import service.sale.SaleService;
import view.EmployeeView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;
    private final SaleService saleService;
    private TableView<Sale> saleTable;

    public EmployeeController(EmployeeView employeeView, BookService bookService, SaleService saleService) {
        this.employeeView = employeeView;
        this.bookService = bookService;
        this.saleService = saleService;
        saleTable = new TableView<>();

        setSaleTable();

        this.employeeView.addDownloadReportButtonListener(new DownloadReportButtonListener());
        this.employeeView.addUpdateAmountButtonListener(new UpdateAmountButtonListener());
        this.employeeView.addAddBookButtonListener(new AddBookButtonListener());
    }

    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener){
        employeeView.addExitButtonListener(exitButtonListener);
    }

    public void setScene(Stage primaryStage){
        employeeView.setScene(primaryStage);
        setTableData();
    }

    public void setCurrentUser(Long id){
        employeeView.setCurrentUser(id);
    }

    public void setTableData(){
        List<Book> books = bookService.findAll();
        employeeView.setDataInTable(books);
    }

    private void setSaleTable(){
        TableColumn<Sale, String> customer = new TableColumn<>("Customer");
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        customer.setMinWidth(200);
        TableColumn<Sale, String> book = new TableColumn<>("Book Title");
        book.setCellValueFactory(new PropertyValueFactory<>("title"));
        book.setMinWidth(200);

        saleTable.getColumns().addAll(customer, book);

    }

    private void setSaleTableData(){
        List<Sale> sales = saleService.findAllForEmployee(employeeView.getCrtUser());
        ObservableList<Sale> saleList = FXCollections.observableArrayList(sales);
        saleTable.getItems().clear();
        saleTable.setItems(saleList);
    }

    private class DownloadReportButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            setSaleTableData();

            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            PrinterJob job = PrinterJob.createPrinterJob();
            if(job != null) {
                job.printPage(pageLayout, saleTable);
                job.endJob();
            }
        }
    }

    private class UpdateAmountButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String updateAmount = employeeView.getUpdateAmount();
            if(updateAmount.equals("")) {
                employeeView.setActiontargetUpdateText("Empty Field!");
            } else {
                try {
                    Integer amount = Integer.parseInt(updateAmount);
                    if(amount <= 0 )
                        employeeView.setActiontargetUpdateText("Amount must be a positive number!");
                    else {
                        Book book = employeeView.getSelectedBook();
                        if(book == null) {
                            employeeView.setActiontargetUpdateText("No book selected!");
                        } else {
                            employeeView.setActiontargetUpdateText("Amount updated successfully!");
                            bookService.updateAmount(bookService.getBookId(book), amount);
                            setTableData();
                        }
                    }
                } catch (NumberFormatException e) {
                    employeeView.setActiontargetUpdateText("Amount must be a number!");
                }
            }
        }
    }

    private class AddBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String bookTitle = employeeView.getTitle();
            String bookAuthor = employeeView.getAuthor();
            String bookDate = employeeView.getDate();
            String bookAmount = employeeView.getAmount();

            if(bookTitle.equals("") || bookAuthor.equals("") || bookDate.equals("") || bookAmount.equals("")) {
                employeeView.setActiontargetNewBookText("All fields must be completed!");
            } else {
                try {
                    Integer amount = Integer.parseInt(bookAmount);
                    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(bookDate, formatter);
                    if(amount <= 0 )
                        employeeView.setActiontargetNewBookText("Amount must be a positive number!");
                    else {
                        Book book = new BookBuilder()
                                .setTitle(bookTitle)
                                .setAuthor(bookAuthor)
                                .setPublishedDate(date)
                                .setAmount(amount)
                                .build();
                        if(bookService.getBookId(book) != null) {
                            employeeView.setActiontargetNewBookText("Book already exists!");
                        } else {
                            bookService.save(book);
                            employeeView.setActiontargetNewBookText("Book created successfully!");
                            setTableData();
                        }
                    }
                } catch (NumberFormatException e) {
                    employeeView.setActiontargetNewBookText("Amount must be a number!");
                } catch (DateTimeParseException e) {
                    employeeView.setActiontargetNewBookText("Date must have the format dd/MM/yyyy!");
                }
            }
        }
    }

}
