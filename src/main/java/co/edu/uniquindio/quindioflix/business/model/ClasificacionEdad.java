package co.edu.uniquindio.quindioflix.business.model;

public enum ClasificacionEdad {
    TP, MAS_7, MAS_13, MAS_16, MAS_18;
    public boolean permitidoInfantil() {
        return this==TP||this==MAS_7||this==MAS_13;

    }

}
