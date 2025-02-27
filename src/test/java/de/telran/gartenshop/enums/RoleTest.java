package de.telran.gartenshop.enums;

import de.telran.gartenshop.entity.enums.Role;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

class RoleTest {

    @Test
    void GetTitleTest() {
        assertEquals("Клиент", Role.CLIENT.getTitle(), "Клиент");
        assertEquals("Администратор", Role.ADMINISTRATOR.getTitle(), "Администратор");
    }
}
