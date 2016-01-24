package org.keycloak.example_iot_broker;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Device {
    private String name;
    private String description;
    private String token;
    private String tempToken;
    private Status status;

    public enum Status {Requested, Approved, Denied}

    public Device(String name, String description) {
        this.name = name;
        this.description = description;
        this.tempToken = generateRandomToken();
        status = Status.Requested;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTempToken() {
        return tempToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        status = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Device) {
            Device compared = (Device) obj;
            if (name.equals(compared.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    private String generateRandomToken() {
        return new BigInteger(130, new SecureRandom()).toString(16);
    }
}
