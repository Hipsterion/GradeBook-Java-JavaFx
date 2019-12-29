package main.java.utils.events;

import main.java.businessLayer.domain.Student;

public class TaskStatusEvent implements Event {
    private TaskExecutionStatusEventType type;
    private Student task;
    public TaskStatusEvent(TaskExecutionStatusEventType type, Student task) {
        this.task=task;
        this.type=type;
    }

    public Student getTask() {
        return task;
    }

    public void setTask(Student task) {
        this.task = task;
    }

    public TaskExecutionStatusEventType getType() {
        return type;
    }

    public void setType(TaskExecutionStatusEventType type) {
        this.type = type;
    }
}
