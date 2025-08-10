package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class GetEntitiesRequest implements Serializable {


    private static final long serialVersionUID = -8224097662914849956L;
    private String entityType;

    public GetEntitiesRequest(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }
}

