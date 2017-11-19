package co.edureka.session6;

/**
 * Created by ishantkumar on 19/11/17.
 */

// Model or Bean or POJO(Plain Old Java Object)
public class Book {

    // Attributes
    String price;
    String name;
    String author;


    public Book() {
    }

    public Book(String price, String name, String author) {
        this.price = price;
        this.name = name;
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
