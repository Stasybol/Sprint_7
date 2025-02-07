package ru.praktikum.services.qa.scooter.model;

public class Credentials {

    private String login;
    private String password;

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static Credentials fromCourier(Courier courier) {
        return new Credentials(courier.getLogin(), courier.getPassword());
    }

    public static Credentials otherLoginCourier(Courier courier, String login) {
        return new Credentials(login, courier.getPassword());
    }

    public static Credentials otherPasswordCourier(Courier courier, String password) {
        return new Credentials(courier.getLogin(), password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
