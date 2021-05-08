package org.rj.homectl.common.util;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiffGeneration {

    public static<T> DiffMap mapDifference(T from, T to) {
        return mapDifference(Util.convertToJsonMap(from), Util.convertToJsonMap(to));
    }

    public static DiffMap mapDifference(Map<String, Object> from, Map<String, Object> to) {
        return DiffMap.fromCalculatedMapDifference(Maps.difference(from, to));
    }

    public static Map<String, Object> deepMapDifference(Map<String, Object> from, Map<String, Object> to) {
        return calculateDeepDifference(from, to).toSingleMapView();
    }

    @SuppressWarnings("unchecked")
    private static DeepDiffComponent calculateDeepDifference(Map<String, Object> from, Map<String, Object> to) {
        if (from == null && to == null) return new DeepDiffComponent();
        if (from == null && to != null) return new DeepDiffComponent(new HashMap<>(to), null, null);
        if (from != null && to == null) return new DeepDiffComponent(null, null, new HashMap<>(from));

        final DeepDiffComponent diff = new DeepDiffComponent();

        // Removed fields
        diff.setRemovedFields(from.entrySet().stream()
                .filter(e -> !to.containsKey(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        // Added or updated fields
        to.entrySet().forEach(e -> {
            if (!from.containsKey(e.getKey())) {                                // New field
                diff.addNewField(e.getKey(), e.getValue());
            }
            else if (!Objects.equals(from.get(e.getKey()), e.getValue())) {     //  Updated field
                if (e.getValue() instanceof Map) {
                    final var fieldDiff = calculateDeepDifference((Map<String, Object>)from.get(e.getKey()), (Map<String, Object>)e.getValue());
                    diff.addUpdatedField(e.getKey(), fieldDiff.toSingleMapView());
                }
                else {
                    diff.addUpdatedField(e.getKey(), e.getValue());
                }
            }
        });

        return diff;
    }

    private static class DeepDiffComponent {
        private Map<String, Object> newFields;
        private Map<String, Object> updatedFields;
        private Map<String, Object> removedFields;

        public DeepDiffComponent() { }

        public DeepDiffComponent(Map<String, Object> newFields, Map<String, Object> updatedFields, Map<String, Object> removedFields) {
            this.newFields = newFields;
            this.updatedFields = updatedFields;
            this.removedFields = removedFields;
        }

        public void addNewField(String key, Object newField) {
            if (newFields == null) newFields = new HashMap<>();
            newFields.put(key, newField);
        }

        public void addUpdatedField(String key, Object updatedField) {
            if (updatedFields == null) updatedFields = new HashMap<>();
            updatedFields.put(key, updatedField);
        }

        public void addRemovedField(String key, Object removedField) {
            if (removedFields == null) removedFields = new HashMap<>();
            removedFields.put(key, removedField);
        }

        public Map<String, Object> toSingleMapView() {
            final Map<String, Object> map = new HashMap<>();
            if (newFields != null) map.putAll(newFields);
            if (updatedFields != null) map.putAll(updatedFields);
            if (removedFields != null) removedFields.keySet().forEach(k -> map.put(k, null));

            return map;
        }

        public Map<String, Object> getNewFields() {
            return newFields;
        }

        public void setNewFields(Map<String, Object> newFields) {
            this.newFields = newFields;
        }

        public Map<String, Object> getUpdatedFields() {
            return updatedFields;
        }

        public void setUpdatedFields(Map<String, Object> updatedFields) {
            this.updatedFields = updatedFields;
        }

        public Map<String, Object> getRemovedFields() {
            return removedFields;
        }

        public void setRemovedFields(Map<String, Object> removedFields) {
            this.removedFields = removedFields;
        }
    }

}
