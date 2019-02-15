package main.java.FEM.model;

public class Element {


    private Integer[] id;
    private Double k;


    public Integer[] getId() {
        return id;
    }

    void setId(Integer[] id) {
        this.id = id;
    }


    void setK(Double k) {
        this.k = k;
    }

}
