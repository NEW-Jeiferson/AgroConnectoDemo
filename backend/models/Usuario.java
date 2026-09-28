package models;

public class Usuario {
    public Integer id;
    public String email;
    public String password; // Contraseña encriptada (bcrypt)

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
