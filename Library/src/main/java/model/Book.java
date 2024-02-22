package model;

import java.time.LocalDate;

public class Book {
    protected Long id;
    protected String author;
    protected String title;
    protected LocalDate publishedDate;
    protected int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return String.format("Book author: %s | title: %s | published date: %s.", author, title, publishedDate);
    }

    @Override
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(!(o instanceof Book))
            return false;
        Book b = (Book) o;
        return this.author.equals(b.author) &&
                this.title.equals(b.title) &&
                this.publishedDate.equals(b.publishedDate);
    }
}
