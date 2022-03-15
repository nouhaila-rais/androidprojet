package fr.uge.projetandroid.entities;

public class RequestBorrow {
    private long id;

    private String askedAt;

    private boolean status;

    private String startAt;

    private String endAt;

    private long product;

    private long user;

    public RequestBorrow() {

    }

    public RequestBorrow(long id, String askedAt, boolean status, String startAt, String endAt, long product, long user) {
        this.id = id;
        this.askedAt = askedAt;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
        this.product = product;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAskedAt() {
        return askedAt;
    }

    public void setAskedAt(String askedAt) {
        this.askedAt = askedAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        return "RequestBorrow{" +
                "id=" + id +
                ", askedAt='" + askedAt + '\'' +
                ", status='" + status + '\'' +
                ", startAt='" + startAt + '\'' +
                ", endAt='" + endAt + '\'' +
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
