package fr.uge.projetandroid.entities;

public class Borrow {

    private long id;

    private String startAt;

    private String endAt;

    private boolean returned;

    private long product;

    private long user;

    public Borrow() {
    }

    public Borrow(long id, String startAt, String endAt, boolean returned, long product, long user) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
        this.returned = returned;
        this.product = product;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", startAt='" + startAt + '\'' +
                ", endAt='" + endAt + '\'' +
                ", returned=" + returned +
                ", product=" + product +
                ", user=" + user +
                '}';
    }

    public String toJson(){

        return    "    {\n"+
                "        \"startAt\": \""+startAt+"\",\n"+
                "        \"endAt\": \""+endAt+"\",\n"+
                "        \"user\": "+user+",\n"+
                "        \"product\": "+product+"\n"+
                "    }";
    }


}
