package model;

public class EBook extends Book{
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return String.format("EBook author: %s | title: %s | published date: %s | format: %s.", author, title, publishedDate, format);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EBook eBook = (EBook) o;
        return format.equals(eBook.format);
    }

}
