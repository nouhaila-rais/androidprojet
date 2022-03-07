package fr.uge.projetandroid.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Product {

    long id;
    String name;
    String category;
    String type;
    String description;
    double price;
    String state;
    boolean available;
    Date createdAt;
    Media image;
    User user;
    List<Comment> comments;
    List<Borrow> borrows;
    List<RequestBorrow> requestBorrows;
    int totalComment;
    double avgRate;

    public Product() {
        this.borrows = new ArrayList<>();
        this.requestBorrows = new ArrayList<>();
        this.comments = new ArrayList<>();
    }


    public Product(long id, String name, String category, String type, String description, double price, String state, boolean available, Date createdAt, Media image, User user, List<Comment> comments, List<Borrow> borrows, List<RequestBorrow> requestBorrows, int totalComment, double avgRate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.description = description;
        this.price = price;
        this.state = state;
        this.available = available;
        this.createdAt = createdAt;
        this.image = image;
        this.user = user;
        this.comments = comments;
        this.borrows = borrows;
        this.requestBorrows = requestBorrows;
        this.totalComment = totalComment;
        this.avgRate = avgRate;
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }

    public List<RequestBorrow> getRequestBorrows() {
        return requestBorrows;
    }

    public void setRequestBorrows(List<RequestBorrow> requestBorrows) {
        this.requestBorrows = requestBorrows;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(double avgRate) {
        this.avgRate = avgRate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", state='" + state + '\'' +
                ", available=" + available +
                ", createdAt=" + createdAt +
                ", image=" + image +
                ", user=" + user +
                ", comments=" + comments +
                ", borrows=" + borrows +
                ", requestBorrows=" + requestBorrows +
                ", totalComment=" + totalComment +
                ", avgRate=" + avgRate +
                '}';
    }
}
