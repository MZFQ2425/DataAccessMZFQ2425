import java.util.ArrayList;

public class Alumnos {
    //Propiedades
    private ArrayList<Alumno> listaAlumnos = new ArrayList<Alumno>();

    //Funciones
    public void addAlumno(Alumno alu){
        listaAlumnos.add(alu);
    }

    public Alumno getAlumno(int pos){
        if(pos >= 0 && pos <= listaAlumnos.size()){
            return listaAlumnos.get(pos);
        }
        return null;
    }

    public Float obtenerMedia(){
        if(listaAlumnos.size() == 0){
            return 0f;
        }else{
            Float media = 0f;
            for(int i = 0; i < listaAlumnos.size(); i++){
                media += listaAlumnos.get(i).getNota();
            }
            return (media / listaAlumnos.size());
        }
    }
}
