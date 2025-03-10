package clubdeportivo;

public class Grupo {
	private String codigo;
	private String actividad;
	private int nplazas;
	private int nmatriculados;
	private double tarifa;

	public Grupo(String codigo, String actividad, int nplazas, int matriculados, double tarifa) throws ClubException {
		validarCodigo(codigo);
		validarActividad(actividad);
		validarNplazas(nplazas);
		validarNmatriculados(matriculados, nplazas);
		validarTarifa(tarifa);

		this.codigo = codigo;
		this.actividad = actividad;
		this.nplazas = nplazas;
		this.nmatriculados = matriculados;
		this.tarifa = tarifa;
	}

	private void validarCodigo(String codigo) throws ClubException {
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new ClubException("ERROR: El código del grupo no puede ser nulo o vacío.");
		}
	}

	private void validarActividad(String actividad) throws ClubException {
		if (actividad == null || actividad.trim().isEmpty()) {
			throw new ClubException("ERROR: La actividad del grupo no puede ser nula o vacía.");
		}
	}

	private void validarNplazas(int nplazas) throws ClubException {
		if (nplazas <= 0) {
			throw new ClubException("ERROR: El número de plazas debe ser mayor que 0.");
		}
	}

	private void validarNmatriculados(int nmatriculados, int nplazas) throws ClubException {
		if (nmatriculados < 0) {
			throw new ClubException("ERROR: El número de matriculados no puede ser negativo.");
		}
		if (nmatriculados > nplazas) {
			throw new ClubException("ERROR: El número de matriculados no puede ser mayor que el número de plazas.");
		}
	}

	private void validarTarifa(double tarifa) throws ClubException {
		if (tarifa <= 0) {
			throw new ClubException("ERROR: La tarifa debe ser mayor que 0.");
		}
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
		return nplazas - nmatriculados;
	}

	public void actualizarPlazas(int n) throws ClubException {
		validarNplazas(n);
		validarNmatriculados(nmatriculados, n);
		this.nplazas = n;
	}

	public void matricular(int n) throws ClubException {
		if (n <= 0) {
			throw new ClubException("ERROR: No se puede matricular un número negativo o cero de alumnos.");
		}
		if (plazasLibres() < n) {
			throw new ClubException("ERROR: No hay suficientes plazas libres.");
		}
		this.nmatriculados += n;
	}

	public String toString() {
		return "(" + codigo + " - " + actividad + " - " + tarifa + " euros " + "- P:" + nplazas + " - M:" + nmatriculados + ")";
	}

	public boolean equals(Object o) {
		if (o instanceof Grupo) {
			Grupo otro = (Grupo) o;
			return this.codigo.equalsIgnoreCase(otro.codigo) && this.actividad.equalsIgnoreCase(otro.actividad);
		}
		return false;
	}

	public int hashCode() {
		return codigo.toUpperCase().hashCode() + actividad.toUpperCase().hashCode();
	}
}
