package lab3.rest;

import lab3.dao.AnimalDAO;
import lab3.model.Animal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
public class AnimalSearchResource {

    @GET
    public Response searchAnimals(
            @QueryParam("name") String name,
            @QueryParam("species") String species,
            @QueryParam("habitat") String habitat,
            @QueryParam("age") Integer age) {
        try {
            List<Animal> animals = AnimalDAO.search(name, species, habitat, age);
            return Response.ok(animals).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching for animals").build();
        }
    }
}