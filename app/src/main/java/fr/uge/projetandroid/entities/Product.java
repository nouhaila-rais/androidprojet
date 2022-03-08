package fr.uge.projetandroid.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Product {

    private long id;

    private String name;

    private String category;

    private String type;

    private String description;

    private double price;

    private String state;

    private boolean available;

    private String createdAt;

    private String path;

    private User user;

    private Vector<Comment> comments;

    private Collection<Borrow> borrows;

    private Collection<RequestBorrow> requestBorrows;

    public Product() {
        this.borrows = new ArrayList<>();
        this.requestBorrows = new ArrayList<>();
        this.comments = new Vector<>();
    }


    public Product(long id, String name, String category, String type, String description, double price, String state, boolean available, String createdAt, String path, User user, Vector<Comment> comments, Collection<Borrow> borrows, Collection<RequestBorrow> requestBorrows) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.description = description;
        this.price = price;
        this.state = state;
        this.available = available;
        this.createdAt = createdAt;
        this.path = path;
        this.user = user;
        this.comments = comments;
        this.borrows = borrows;
        this.requestBorrows = requestBorrows;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vector<Comment> getComments() {
        return comments;
    }

    public void setComments(Vector<Comment> comments) {
        this.comments = comments;
    }

    public Collection<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(Collection<Borrow> borrows) {
        this.borrows = borrows;
    }

    public Collection<RequestBorrow> getRequestBorrows() {
        return requestBorrows;
    }

    public void setRequestBorrows(Collection<RequestBorrow> requestBorrows) {
        this.requestBorrows = requestBorrows;
    }

    public void addComment(Comment c){
        comments.add(c);
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
                ", createdAt='" + createdAt + '\'' +
                ", path='" + path + '\'' +
                ", user=" + user +
                ", comments=" + comments +
                ", borrows=" + borrows +
                ", requestBorrows=" + requestBorrows +
                '}';
    }

    public String toJson(){

        return    "    {\n"+
                "        \"name\": \""+name+"\",\n"+
                "        \"category\": \""+category+"\",\n"+
                "        \"type\": \""+type+"\",\n"+
                "        \"description\": \""+description+"\",\n"+
                "        \"price\": "+price+",\n"+
                "        \"path\": "+path+",\n"+
                "        \"state\": \""+state+"\"\n"+
                "    }";
    }
}
