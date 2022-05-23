package ar.com.sba.gara.auditoria.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "auditoria")
public class Auditoria {

    public ObjectId id;

    public String usuario;

    public String entidad;

    public Accion accion;

    public String datos;

    public LocalDateTime fechaHora;
}
