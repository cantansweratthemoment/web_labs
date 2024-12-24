package lab3.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("lab3.rest");

        register(JacksonFeature.class);
    }
}