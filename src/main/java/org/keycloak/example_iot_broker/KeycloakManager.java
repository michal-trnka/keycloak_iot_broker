package org.keycloak.example_iot_broker;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakManager {
    public String createToken(Device dev){
        return "";
    }

    public String renewToken(Device dev){
        return "";
    }

    public void revokeToken(Device dev){

    }

}
