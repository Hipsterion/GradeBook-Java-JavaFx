package main.java.businessLayer.domain;

public class Entity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Entity(ID id) {
        this.id = id;
    }

    public void update(Entity<ID> entity){

    }
}