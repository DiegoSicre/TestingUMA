package clubdeportivo;

public class Grupo {
	private String codigo;
	private String actividad;
	private int nplazas;
	private int nmatriculados;
	private double tarifa;
	
	public Grupo(String codigo, String actividad, int nplazas,  int matriculados, double tarifa) throws ClubException {
		if (nplazas<=0 || matriculados<0 || tarifa <=0) {
			throw new ClubException("ERROR: los datos numéricos no pueden ser menores o iguales que 0.");
		}
		if (matriculados>nplazas) {
			throw new ClubException("ERROR: El número de plazas es menor que el de matriculados.");
		}
		this.codigo=codigo;
		this.actividad=actividad;
		this.nplazas=nplazas;
		this.nmatriculados=matriculados;
		this.tarifa=tarifa;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getActividad() {
		return actividad;
	}

	public int getPlazas() {
		return nplazas;
	}

	public int getMatriculados() {
		return nmatriculados;
	}
	
	public double getTarifa() {
		return tarifa;
	}
	
	public int plazasLibres() {
		return nplazas-nmatriculados;
	}
	
	public void actualizarPlazas(int n) throws ClubException { 
		if (n<=0 || n < nmatriculados) {
			throw new ClubException("ERROR: número de plazas negativo.");
		}
		nplazas=n;		
	}
	
	public void matricular(int n) throws ClubException {
		if (plazasLibres()< n || n<=0) {
			throw new ClubException("ERROR: no hay plazas libres suficientes, plazas libre: "+ plazasLibres()+ " y matriculas: "+n);
		}
		nmatriculados+=n;
	}
	
	public String toString() {
		return "("+ codigo + " - "+actividad+" - " + tarifa + " euros "+ "- P:" + nplazas +" - M:" +nmatriculados+")";
	}
	
	public boolean equals(Object o) {
		boolean ok=false;
		if (o instanceof Grupo) {
			Grupo otro = (Grupo) o;
			ok = this.codigo.equalsIgnoreCase(otro.codigo) && this.actividad.equalsIgnoreCase(otro.actividad);
		}
		return ok;
	}
	
	public int hashCode() {
		return codigo.toUpperCase().hashCode()+actividad.toUpperCase().hashCode();
	}
}
