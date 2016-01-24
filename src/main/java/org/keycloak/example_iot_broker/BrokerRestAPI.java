package org.keycloak.example_iot_broker;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Stateless
@PermitAll
@Path("/")
public class BrokerRestAPI {

    @Inject
    private DeviceManager deviceManager;

    @POST
    @Path("/requests")
    public Response addRequest(@FormParam("sensorID") String sensorID, @FormParam("description") String description,
                               @Context UriInfo uri){
        sensorID = sensorID.replaceAll("[^A-Za-z0-9]","_");
        if(deviceManager.isRequested(sensorID)||deviceManager.isRegistered(sensorID)){
            return Response.notModified().build();
        }
        deviceManager.requestDevice(sensorID,description);
        Device dev = deviceManager.getDevice(sensorID);
        return Response.created(uri.getBaseUriBuilder().path("/requests/{id}").build(dev.getName())).entity(dev).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/requests/{deviceID}")
    public Response getDevice(@PathParam("deviceID") String deviceID, @QueryParam("temp_token") String token){
        if(!deviceManager.checkDeviceTempToken(deviceID,token)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok().entity(deviceManager.getDevice(deviceID)).build();
    }

    @PUT
    @Produces(APPLICATION_JSON)
    @Path("/requests/{deviceID}")
    public Response renewToken(@PathParam("deviceID") String deviceID, @FormParam("temp_token") String token){
        if(!deviceManager.checkDeviceTempToken(deviceID,token)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok().entity(deviceManager.getDevice(deviceID)).build();
    }
}
