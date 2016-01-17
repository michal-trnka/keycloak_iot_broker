package org.keycloak.example_iot_broker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class DeviceManager {
    private Set<Device> registeredDevices = new HashSet<>();
    private Set<Device> requestedDevices = new HashSet<>();

    public void requestDevice(Device d){
        requestedDevices.add(d);
    }

    public void approveDevice(Device d){
        requestedDevices.remove(d);
        registeredDevices.add(d);
    }

    public Set<Device> getRegisteredDevices() {
        return registeredDevices;
    }

    public Set<Device> getRequestedDevices() {
        return requestedDevices;
    }

}
