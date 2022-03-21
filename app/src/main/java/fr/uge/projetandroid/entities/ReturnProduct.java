package fr.uge.projetandroid.entities;

public class ReturnProduct {

    private long id;

    private String returnAt;

    private long product;

    private String state;

    public ReturnProduct() {
    }

    public ReturnProduct(long id, String returnAt, long product, String state) {
        this.id = id;
        this.returnAt = returnAt;
        this.product = product;
        this.state = state;
    }

    public ReturnProduct(long product, String state) {
        this.product = product;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReturnAt() {
        return returnAt;
    }

    public void setReturnAt(String returnAt) {
        this.returnAt = returnAt;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ReturnProduct{" +
                "id=" + id +
                ", returnAt='" + returnAt + '\'' +
                ", product=" + product +
                ", state='" + state + '\'' +
                '}';
    }

    public String toJson(){

        return    "    {\n"+
                "        \"product\": "+product+",\n"+
                "        \"state\": \""+state+"\"\n"+
                "    }";

    }


}
