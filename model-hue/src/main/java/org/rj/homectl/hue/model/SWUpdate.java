package org.rj.homectl.hue.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

public class SWUpdate {
    @JsonAlias({ "lastinstall" })
    private LocalDate lastInstall;
    private String state;

    public SWUpdate() { }

    public LocalDate getLastInstall() {
        return lastInstall;
    }

    public void setLastInstall(LocalDate lastInstall) {
        this.lastInstall = lastInstall;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SWUpdate swUpdate = (SWUpdate) o;

        return new EqualsBuilder().append(lastInstall, swUpdate.lastInstall).append(state, swUpdate.state).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(lastInstall).append(state).toHashCode();
    }
}
