package org.keycloak.example_iot_broker;

import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Named
@SecurityDomain("keycloak")
public class DeviceManager {
    private Map<String, Device> registeredDevices = new HashMap<>();
    private Map<String, Device> requestedDevices = new HashMap<>();
    private Map<String, Device> deniedDevices = new HashMap<>();

    @Inject
    KeycloakManager kc;

    public void requestDevice(String sensorID, String description) {
        Device dev = new Device(sensorID, description);
        if (requestedDevices.containsKey(dev.getName()) || registeredDevices.containsKey(dev.getName())) {
            return;
        }
        requestedDevices.put(dev.getName(), dev);
    }

    @RolesAllowed({"deviceAdmin"})
    public void approveDevice(String deviceID) {
        Device d = popDevice(deviceID);
        d.setToken(kc.createToken(d));
        d.setStatus(Device.Status.Approved);
        registeredDevices.put(d.getName(), d);
    }

    @RolesAllowed({"deviceAdmin"})
    public void denyDevice(String deviceID) {
        Device d = popDevice(deviceID);
        d.setStatus(Device.Status.Denied);
        deniedDevices.put(deviceID, d);
    }

    @RolesAllowed({"deviceAdmin"})
    public void reapproveDevice(String deviceID) {
        Device d = popDevice(deviceID);
        d.setToken(kc.renewToken(d));
        d.setStatus(Device.Status.Approved);
        registeredDevices.put(deviceID,d);
    }

    @RolesAllowed({"deviceAdmin"})
    public boolean checkDeviceTempToken(String deviceID, String token) {
        if (token == null || deviceID == null) {
            return false;
        }
        return getDevice(deviceID).getTempToken().equals(token);
    }

    @RolesAllowed({"deviceAdmin"})
    public Device getDevice(String deviceID) {
        if (requestedDevices.containsKey(deviceID)) {
            return requestedDevices.get(deviceID);
        } else if (deniedDevices.containsKey(deviceID)) {
            return deniedDevices.get(deviceID);
        }
        return registeredDevices.get(deviceID);
    }

    @RolesAllowed({"deviceAdmin"})
    private Device popDevice(String deviceID) {
        if (requestedDevices.containsKey(deviceID)) {
            return requestedDevices.remove(deviceID);
        } else if (deniedDevices.containsKey(deviceID)) {
            return deniedDevices.remove(deviceID);
        }
        return registeredDevices.remove(deviceID);
    }

    @RolesAllowed({"deviceAdmin"})
    public boolean isRegistered(String name) {
        return registeredDevices.containsKey(name);
    }

    @RolesAllowed({"deviceAdmin"})
    public boolean isRequested(String name) {
        return requestedDevices.containsKey(name);
    }

    @RolesAllowed({"deviceAdmin"})
    public Set<Device> getRegisteredDevices() {
        return new HashSet<>(registeredDevices.values());
    }

    @RolesAllowed({"deviceAdmin"})
    public Set<Device> getRequestedDevices() {
        return new HashSet<>(requestedDevices.values());
    }

    @RolesAllowed({"deviceAdmin"})
    public Set<Device> getDeniedDevices() {
        return new HashSet<>(deniedDevices.values());
    }
}
