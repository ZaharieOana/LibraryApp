package repository.book;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepositoryMySQL implements BookRepository{
    protected final Connection connection;
    public AbstractRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    protected abstract String getTableName();
    protected abstract String getTableValues();
    protected abstract void setPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException;
    protected abstract Book getBookFromResultSet(ResultSet resultSet) throws SQLException;

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM " + this.getTableName() + ";";
        List<Book> books = new ArrayList<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM " + this.getTableName() + " WHERE id=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return Optional.ofNullable(getBookFromResultSet(resultSet));
            else
                return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO " + this.getTableName() + this.getTableValues();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setPreparedStatement(book, preparedStatement);

            int rowsInserted = preparedStatement.executeUpdate();
            return (rowsInserted != 1) ? false : true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "TRUNCATE TABLE " + this.getTableName() + ";";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
