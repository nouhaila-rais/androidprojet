package fr.uge.projetandroid.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Notification {

    private long id;
    private String message;
    private String createdAt;
    private long user;
    private long product;
    private String image;


    public Notification() {
    }

    public Notification(long id, String message, String createdAt, long user, long product, String image) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.product = product;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", user=" + user +
                ", product=" + product +
                ", image='" + image + '\'' +
                '}';
    }
}