package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class DeleteUserRequest implements Serializable {
    private int id;
    private String NameTable;

    public DeleteUserRequest(int id, String NameTable) {
        this.id = id;
        this.NameTable = NameTable;
    }
    public DeleteUserRequest() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNameTable() {
        return NameTable;
    }
    public void setNameTable(String nameTable) {
        this.NameTable = nameTable;
    }
}
