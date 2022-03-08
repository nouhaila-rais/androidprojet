package fr.uge.projetandroid.entities;

public class Comment {

    private long id;

    private String content;

    private int rate;

    private String createdAt;

    private User user;

    private long product;

    public Comment() {
    }

    public Comment(long id, String content, int rate, String createdAt, User user, long product) {
        this.id = id;
        this.content = content;
        this.rate = rate;
        this.createdAt = createdAt;
        this.user = user;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", rate=" + rate +
                ", createdAt='" + createdAt + '\'' +
                ", user=" + user +
                ", product=" + product +
                '}';
    }

    public String toJson(){

        return    "    {\n"+
                "        \"content\": \""+content+"\",\n"+
                "        \"rate\": \""+rate+"\",\n"+
                "        \"product\": \""+product+"\"\n"+
                "    }";
    }
}
