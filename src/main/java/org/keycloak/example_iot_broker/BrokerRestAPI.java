package org.keycloak.example_iot_broker;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

@Stateless
@PermitAll
@Path("/")
public class BrokerRestAPI {

    @Inject
    private DeviceManager deviceManager;

    @POST
    @Path("/{sensorID}/requestToken")
    public void addRequest(@PathParam("sensorID") String sensorID, @FormParam("description") String description){
        Device dev = new Device(sensorID,description);

        deviceManager.requestDevice(dev);
    }

    @POST
    @Path("/test")
    public void test(){
        //Device dev = new Device("Dummy","Dummy");
        System.out.println("Test");
        deviceManager.requestDevice(null);
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/crap")
    public Map<String,String> ssss(){
        Map<String,String> map = new HashMap<>();
        System.out.println("Device manager " + deviceManager);

        return map;
    }
}
