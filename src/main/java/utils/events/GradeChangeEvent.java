package main.java.utils.events;

import main.java.businessLayer.domain.Grade;

public class GradeChangeEvent implements Event {
    private ChangeEventType type;
    private Grade data, oldData;

    public GradeChangeEvent(ChangeEventType type, Grade data) {
        this.type = type;
        this.data = data;
    }
    public GradeChangeEvent(ChangeEventType type, Grade data, Grade oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Grade getData() {
        return data;
    }

    public Grade getOldData() {
        return oldData;
    }
}
