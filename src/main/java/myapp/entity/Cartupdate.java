package myapp.entity;

public class Cartupdate {
    private String id; // Cart 엔티티의 ID
    private int count; // 업데이트할 수량

    // Getter, Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

