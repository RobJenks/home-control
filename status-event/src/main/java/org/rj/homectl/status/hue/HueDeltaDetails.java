package org.rj.homectl.status.hue;

import org.rj.homectl.hue.model.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HueDeltaDetails {
    private HueStatusLightData updates;
    private List<HueStatusEventDetails> events;

    public HueDeltaDetails() {
        this(new HueStatusLightData(new HashMap<>()), new ArrayList<>());
    }

    public HueDeltaDetails(HueStatusLightData updates, List<HueStatusEventDetails> events) {
        this.updates = updates;
        this.events = events;
    }

    public boolean isEmpty() {
        return updates.isEmpty() && events.isEmpty();
    }


    public HueStatusLightData getUpdates() {
        return updates;
    }

    public void setUpdates(HueStatusLightData updates) {
        this.updates = updates;
    }

    public List<HueStatusEventDetails> getEvents() {
        return events;
    }

    public void setEvents(List<HueStatusEventDetails> events) {
        this.events = events;
    }

    public void addUpdate(String id, Light data) {
        this.updates.put(id, data);
    }

    public void addEvent(HueStatusEventDetails event) {
        this.events.add(event);
    }

    public void addFrom(HueDeltaDetails other) {
        if (updates == null) updates = new HueStatusLightData();
        updates.putAll(other.getUpdates());

        if (events == null) events = new ArrayList<>();
        events.addAll(other.getEvents());
    }
}
