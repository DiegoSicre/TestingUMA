package clubdeportivo;


public class ClubDeportivoAltoRendimiento extends ClubDeportivo{
	private int maximoPersonasGrupo;
	private double incremento;
	/* 
	public ClubDeportivoAltoRendimiento(String nombre, int maximo, double incremento) throws ClubException {
		super(nombre);
		if (maximo<=0 || incremento<=0) {
			throw new ClubException("ERRORES: valores 0 o negativos.");
		}
		maximoPersonasGrupo=maximo;
		this.incremento=incremento;
	}*/
	public ClubDeportivoAltoRendimiento(String nombre, int maximo, double incremento) throws ClubException {
        super(nombre);
        validarMaximoIncremento(maximo, incremento);
        this.maximoPersonasGrupo = maximo;
        this.incremento = incremento;
    }

	/* 
	public ClubDeportivoAltoRendimiento(String nombre, int tam, int maximo, double incremento) throws ClubException {
		super(nombre,tam);
		if (maximo<=0 || incremento<=0) {
			throw new ClubException("ERRORES: valores 0 o negativos.");
		}
		maximoPersonasGrupo=maximo;
		this.incremento=incremento;
	}*/
	public ClubDeportivoAltoRendimiento(String nombre, int tam, int maximo, double incremento) throws ClubException {
        super(nombre, tam);
        validarMaximoIncremento(maximo, incremento);
        this.maximoPersonasGrupo = maximo;
        this.incremento = incremento;
    }
	// Validación extra para maximoPersonasGrupo e incremento
    private void validarMaximoIncremento(int maximo, double incremento) throws ClubException {
        if (maximo <= 0 || incremento <= 0) {
            throw new ClubException("ERROR: El máximo de personas y el incremento deben ser valores positivos.");
        }
    }

	
	// El club de alto rendimiento tiene limitadas las plazas. Si el número de plazas que se recibe como parametro es mayor que el permitido, 
	// se establece su valor al maximo permitido por grupo para el club.
	@Override
	public void anyadirActividad(String[] datos) throws ClubException {
		if (datos.length<5) {
			throw new ClubException ("ERROR: faltan datos");
		}
		try {
			int plazas = Integer.parseInt(datos[2]);
			int matriculados = Integer.parseInt(datos[3]);
			double tarifa = Double.parseDouble(datos[4]);
			if (plazas > maximoPersonasGrupo) {
				plazas=maximoPersonasGrupo;
			}
			Grupo g = new Grupo(datos[0], datos[1], plazas, matriculados, tarifa);
			super.anyadirActividad(g);
		} catch (NumberFormatException e) {
			throw new ClubException("ERROR: formato de número incorrecto");
		}
	}
	
	
	// Los ingresos del club tienen
	@Override
    public double ingresos() {
        double cantidad = super.ingresos();
        return cantidad + cantidad * (incremento / 100);
    }
	
}
