package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class GetEntitiesResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<?> entities;

    public GetEntitiesResponse(List<?> entities) {
        this.entities = entities;
    }

    public List<?> getEntities() {
        return entities;
    }
}

