package foodstore.entities;

import foodstore.interfaces.Identificable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Base implements Identificable<Long> {

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    protected Base() {
        this(null);
    }

    protected Base(Long id) {
        this.id = id;
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean sameId(Long id) {
        return this.id != null && this.id.equals(id);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }
        Base otraEntidad = (Base) objeto;
        return id != null && id.equals(otraEntidad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public abstract String toString();
}
