//Práctica Realizada por Ángel Escaño y Diego Sicre

package clubdeportivo;

import java.util.StringJoiner;

public class ClubDeportivo {
	private String nombre;
	private int ngrupos;
	private Grupo[] grupos;
	private static final int TAM = 10;


	public ClubDeportivo(String nombre) throws ClubException {
        this(nombre, TAM);
    }

    public ClubDeportivo(String nombre, int n) throws ClubException {
        validarNombre(nombre);
        validarNumeroGrupos(n);

        this.nombre = nombre;
        this.ngrupos = 0; // Inicializamos el número de grupos en 0
        this.grupos = new Grupo[n];
    }

    // Validación en los constructores
    private void validarNombre(String nombre) throws ClubException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ClubException("ERROR: El nombre del club no puede ser nulo o vacío");
        }
    }

    private void validarNumeroGrupos(int n) throws ClubException {
        if (n <= 0) {
            throw new ClubException("ERROR: El número de grupos debe ser mayor que 0.");
        }
    }

	private int buscar(Grupo g) {
		int i = 0;
		while (i < ngrupos && !g.equals(grupos[i])) {
			i++;
		}
		if (i == ngrupos) {
			i = -1;
		}
		return i;
	}
	/* 
	public void anyadirActividad(String[] datos) throws ClubException {
		try {
			int plazas = Integer.parseInt(datos[2]);
			int matriculados = Integer.parseInt(datos[3]);
			double tarifa = Double.parseDouble(datos[4]);
			Grupo g = new Grupo(datos[0], datos[1], plazas, matriculados, tarifa);
			anyadirActividad(g);
		} catch (NumberFormatException e) {
			throw new ClubException("ERROR: formato de número incorrecto");
		}
	}*/
	public void anyadirActividad(String[] datos) throws ClubException {
		validarDatosActividad(datos);
		try {
			int plazas = Integer.parseInt(datos[2]);
			int matriculados = Integer.parseInt(datos[3]);
			double tarifa = Double.parseDouble(datos[4]);
			Grupo g = new Grupo(datos[0], datos[1], plazas, matriculados, tarifa);
			anyadirActividad(g);
		} catch (NumberFormatException e) {
			throw new ClubException("ERROR: formato de número incorrecto");
		}
	}
	public void anyadirActividad(Grupo g) throws ClubException {
		validarGrupoNoNulo(g);
		
		if (ngrupos >= grupos.length) {
			throw new ClubException("ERROR: No se pueden añadir más grupos, capacidad máxima alcanzada.");
		}
	
		int pos = buscar(g);
		if (pos == -1) { // El grupo es nuevo
			grupos[ngrupos] = g;
			ngrupos++;
		} else { // El grupo ya existe, se actualizan las plazas
			grupos[pos].actualizarPlazas(g.getPlazas()); //Cómo está 
		}
	}
	/* 
	public void anyadirActividad(Grupo g) throws ClubException {
		if (g==null){ // ADDME: anaydido para comprobar los grupos nulos
			throw new ClubException("ERROR: el grupo es nulo");
		}
		int pos = buscar(g);
		if (pos == -1) { // El grupo es nuevo
			grupos[ngrupos] = g;
			ngrupos++;
		} else { // El grupo ya existe --> modificamos las plazas
			grupos[pos].actualizarPlazas(g.getPlazas());
		}
	}*/
	private void validarGrupoNoNulo(Grupo g) throws ClubException {
		if (g == null) {
			throw new ClubException("ERROR: el grupo es nulo");
		}
	}
	
	private void validarDatosActividad(String[] datos) throws ClubException {
		if (datos == null || datos.length < 5) {
			throw new ClubException("ERROR: los datos de la actividad son inválidos o incompletos.");
		}
	}
	/* 
	public int plazasLibres(String actividad) {
		int p = 0;
		int i = 0;
		while (i < ngrupos) {
			if (grupos[i].getActividad().equals(actividad)) {
				p += grupos[i].plazasLibres();
			}
			i++;
		}
		return p;
	}*/
	public int plazasLibres(String actividad) throws ClubException {
		validarActividad(actividad);
		
		int p = 0;
		int i = 0;
		while (i < ngrupos) {
			if (grupos[i] != null && grupos[i].getActividad().equals(actividad)) {
				p += grupos[i].plazasLibres();
			}
			i++;
		}
		return p;
	}
	
	
	private void validarActividad(String actividad) throws ClubException {
		if (actividad == null || actividad.trim().isEmpty()) {
			throw new ClubException("ERROR: La actividad no puede ser nula o vacía.");
		}
	}

	public void matricular(String actividad, int npersonas) throws ClubException {
		int plazas = plazasLibres(actividad);
		if (plazas < npersonas) {
			throw new ClubException("ERROR: no hay suficientes plazas libres para esa actividad en el club.");
		}
		int i = 0;
		while (i < ngrupos && npersonas > 0) {
			if (actividad.equals(grupos[i].getActividad())) {
				int plazasGrupo = grupos[i].plazasLibres();
				if (npersonas >= plazasGrupo) {
					grupos[i].matricular(plazasGrupo);
					npersonas -= plazasGrupo;
				} else {
					grupos[i].matricular(npersonas);
				}
			}
			i++;
		}
	}

	public double ingresos() {
		double cantidad = 0.0;
		int i = 0;
		while (i < ngrupos) {
			cantidad += grupos[i].getTarifa() * grupos[i].getMatriculados();
			i++;
		}
		return cantidad;
	}

	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[ ", " ]");
		int i = 0;
		while (i < ngrupos) {
			sj.add(grupos[i].toString());
			i++;
		}
		return nombre + " --> " + sj.toString();
	}
}
