package lab3.rest;

import lab3.dao.AnimalDAO;
import lab3.model.Animal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnimalResource {

    @POST
    public Response createAnimal(Animal animal) {
        try {
            int id = AnimalDAO.create(animal);
            return Response.status(Response.Status.CREATED).entity("Created animal with ID: " + id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating animal").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAnimal(@PathParam("id") int id, Animal animal) {
        try {
            boolean updated = AnimalDAO.update(id, animal);
            if (updated) {
                return Response.ok("Animal updated").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Animal not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating animal").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAnimal(@PathParam("id") int id) {
        try {
            boolean deleted = AnimalDAO.delete(id);
            if (deleted) {
                return Response.ok("Animal deleted").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Animal not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting animal").build();
        }
    }
}