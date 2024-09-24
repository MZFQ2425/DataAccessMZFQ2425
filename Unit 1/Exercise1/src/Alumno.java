public class Alumno {
    //Propiedades de alumno
    private String nombre;
    private Integer nota;

    //Getters setters
    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public Integer getNota(){
        return this.nota;
    }
    public void setNota(Integer nota){
        if(nota >= 0 && nota <= 10){
            this.nota = nota;
        }
    }

    //Funciones
    public Boolean isAprobado(){
        if(this.nota>=5){
            return true;
        }
        return false;
    }
}