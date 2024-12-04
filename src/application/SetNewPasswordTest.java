package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SetNewPasswordTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testSetNewPasswordWhenOTPIsActive() {
        user.setOneTimePassword(true); // OTP is active
        boolean result = user.setNewPassword("NewSecurePassword123");

        assertFalse(result, "Password should not be set when OTP is active.");
        assertNull(user.getPassword(), "Password should remain null when OTP is active.");
        assertFalse(user.isAccountSetupCompleted(), "Account setup should not be marked completed when OTP is active.");
    }

    @Test
    void testSetNewPasswordWhenOTPIsInactive() {
        user.setOneTimePassword(false); // OTP is inactive
        boolean result = user.setNewPassword("NewSecurePassword123");

        assertTrue(result, "Password should be set when OTP is inactive.");
        assertEquals("NewSecurePassword123", user.getPassword(), "Password should match the new password set.");
        assertTrue(user.isAccountSetupCompleted(), "Account setup should be marked completed when OTP is inactive.");
    }
}
