package domain;

public abstract class Usuario {
	
	protected int idUsuario;
	protected String nombre;
	protected String apellidos;
	protected String dni;
	protected String email;
	protected int nTelefono;
	protected String dirección;
	
	public int getnTelefono() {
		return nTelefono;
	}
	public void setnTelefono(int nTelefono) {
		this.nTelefono = nTelefono;
	}

	public int getId() {
		return idUsuario;
	}
	public void setId(int id) {
		this.idUsuario = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDirección() {
		return dirección;
	}
	public void setDirección(String dirección) {
		this.dirección = dirección;
	}
	public Usuario(String nombre, String apellidos, String dni, String email, int nTelefono) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.nTelefono = nTelefono;
	}
	
	
	
	

}
