package org.rj.homectl.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonDeserialize(converter = HomeState.PostDeserializationSanitizer.class)
public class HomeState {
    private List<Room> rooms;
    private List<Device> devices;
    @JsonIgnore
    private Map<String, Room> indexedRooms;
    @JsonIgnore
    private Map<String, Device> indexedDevices;


    public HomeState() { }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @JsonIgnore
    public Map<String, Room> getIndexedRooms() {
        return indexedRooms;
    }

    @JsonIgnore
    public Map<String, Device> getIndexedDevices() {
        return indexedDevices;
    }

    @JsonIgnore
    private void updatedIndexedCollections() {
        this.indexedRooms = this.rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
        this.indexedDevices = this.devices.stream().collect(Collectors.toMap(Device::getId, Function.identity()));
    }

    protected static class PostDeserializationSanitizer extends StdConverter<HomeState, HomeState> {
        @Override
        public HomeState convert(HomeState state) {
            // Default values where necessary
            if (state.rooms == null) state.rooms = new ArrayList<>();
            if (state.devices == null) state.devices = new ArrayList<>();

            // Build internal indexed collections
            state.updatedIndexedCollections();

            return state;
        }
    }
}
