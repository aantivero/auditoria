package ar.com.sba.gara.auditoria.resource;

import ar.com.sba.gara.auditoria.entity.Auditoria;
import ar.com.sba.gara.auditoria.repository.AuditoriaRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Path("/gara/api/v1/auditoria")
@Consumes("application/json")
@Produces("application/json")
public class AuditoriaResource {

    @Inject
    AuditoriaRepository auditoriaRepository;

    @GET
    @Path("/{id}")
    public Uni<Auditoria> get(@PathParam("id") String id) {
        return auditoriaRepository.findById(new ObjectId(id));
    }

    @GET
    public Multi<Auditoria> list() {
        return auditoriaRepository.streamAll();
    }

    @GET
    @Path("/search")
    public Uni<List<Auditoria>> search(@QueryParam("usuario") String usuario,
                                       @QueryParam("entidad") String entidad) {
        if (usuario == null) {
            return auditoriaRepository.find("{'entidad': ?1}", entidad).list();
        } else {
            return auditoriaRepository.find("{'usuario': ?1, 'entidad': ?2}",
                    usuario, entidad).list();
        }
    }

    @GET
    @Path("/usuario/{usuario}")
    public Uni<List<Auditoria>> findByUsuario(@PathParam("usuario") String usuario,
                                              @QueryParam("dateFrom") String dateFrom,
                                              @QueryParam("dateTo") String dateTo)  {
        if (dateFrom != null && dateTo != null) {
            return auditoriaRepository.find("{'usuario': ?1, 'fechaHora': {$gte: ?2}, 'fechaHora': {$lte: ?3}}",
                            usuario,
                            ZonedDateTime.parse(dateFrom).toLocalDateTime(),
                            ZonedDateTime.parse(dateTo).toLocalDateTime())
                    .list();
        } else {
            return auditoriaRepository.find("{'usuario': ?1}",
                            usuario)
                    .list();
        }

    }

    @GET
    @Path("/entidad/{entidad}")
    public Uni<List<Auditoria>> findByEntidad(@PathParam("entidad") String entidad,
                                              @QueryParam("dateFrom") String dateFrom,
                                              @QueryParam("dateTo") String dateTo)  {
        if (dateFrom != null && dateTo != null) {
            return auditoriaRepository.find("{'entidad': ?1, 'fechaHora': {$gte: ?2}, 'fechaHora': {$lte: ?3}",
                            entidad,
                            ZonedDateTime.parse(dateFrom).toLocalDateTime(),
                            ZonedDateTime.parse(dateTo).toLocalDateTime())
                    .list();
        } else {
            return auditoriaRepository.find("{'entidad': ?1}",
                            entidad)
                    .list();
        }
    }

    @POST
    public Uni<Response> create(Auditoria auditoria) {
        auditoria.fechaHora = LocalDateTime.now();
        return auditoriaRepository.persist(auditoria).map(
                v -> Response.created(URI.create("/gara/api/v1/auditoria/" + v.id.toString()))
                        .entity(auditoria).build()
        );
    }
}
