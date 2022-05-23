package ar.com.sba.gara.auditoria.repository;

import ar.com.sba.gara.auditoria.entity.Auditoria;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuditoriaRepository implements ReactivePanacheMongoRepository<Auditoria> {

}
