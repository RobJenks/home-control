package org.rj.homectl.common.beans.events;

public class StatusEvent {
    private String name;
    private Integer num;

    public StatusEvent() { }

    public StatusEvent(String name, Integer num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
