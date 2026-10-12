package models;

public class Usuario {
    public Integer id;
    public String email;
    public String password; // Contraseña encriptada (bcrypt)
    public String rol; // ej. administrador, cliente

    public Usuario(String email, String password, String rol) {
        this.email = email;
        this.password = password;
        this.rol = rol != null ? rol : "cliente";
    }
}
