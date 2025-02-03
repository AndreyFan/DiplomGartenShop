package de.telran.gartenshop.entity.enums;

public enum Role {
    CLIENT("Клиент"),
    ADMINISTRATOR("Администратор");

    private String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
