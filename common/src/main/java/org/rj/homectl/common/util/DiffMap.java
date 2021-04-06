package org.rj.homectl.common.util;

import com.google.common.collect.MapDifference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.stream.Collectors;


public class DiffMap {
    private Map<String, Object> newFields;
    private Map<String, DiffMapValueChange<Object>> updatedFields;
    private Map<String, Object> removedFields;

    public DiffMap() { }

    public DiffMap(Map<String, Object> newFields, Map<String, DiffMapValueChange<Object>> updatedFields, Map<String, Object> removedFields) {
        this.newFields = newFields;
        this.updatedFields = updatedFields;
        this.removedFields = removedFields;
    }

    public static DiffMap fromCalculatedMapDifference(MapDifference<String, Object> mapDifference) {
        return new DiffMap(
                mapDifference.entriesOnlyOnRight(),
                mapDifference.entriesDiffering().entrySet().stream()
                        .map(vd -> ImmutablePair.of(vd.getKey(), new DiffMapValueChange<>(vd.getValue().leftValue(), vd.getValue().rightValue())))
                        .collect(Collectors.toMap(Pair::getKey, Pair::getValue)),
                mapDifference.entriesOnlyOnLeft()
        );
    }

    public Map<String, Object> getNewFields() {
        return newFields;
    }

    public void setNewFields(Map<String, Object> newFields) {
        this.newFields = newFields;
    }

    public Map<String, DiffMapValueChange<Object>> getUpdatedFields() {
        return updatedFields;
    }

    public void setUpdatedFields(Map<String, DiffMapValueChange<Object>> updatedFields) {
        this.updatedFields = updatedFields;
    }

    public Map<String, Object> getRemovedFields() {
        return removedFields;
    }

    public void setRemovedFields(Map<String, Object> removedFields) {
        this.removedFields = removedFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DiffMap diffMap = (DiffMap) o;

        return new EqualsBuilder().append(newFields, diffMap.newFields).append(updatedFields, diffMap.updatedFields).append(removedFields, diffMap.removedFields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(newFields).append(updatedFields).append(removedFields).toHashCode();
    }
}
