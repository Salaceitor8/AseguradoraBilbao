package domain;

import java.util.List;

public class Cliente extends Usuario{
	
	

	protected List<Seguro> seguros;

	


	public List<Seguro> getSeguros() {
		return seguros;
	}

	public void setSeguros(List<Seguro> seguros) {
		this.seguros = seguros;
	}
	
	public Cliente(String nombre, String apellidos, String dni, String email, int nTelefono) {
		super(nombre, apellidos, dni, email, nTelefono);
	}

	

}
