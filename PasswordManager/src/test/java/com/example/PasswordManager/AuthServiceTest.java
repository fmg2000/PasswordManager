package com.example.PasswordManager;

import com.example.PasswordManager.auth.dto.UserDTO;
import com.example.PasswordManager.auth.service.AuthService;
import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_whenUsernameExists_returnsErrorMessage() throws Exception {
        when(userService.findUserbyName("john")).thenReturn(new User());
        UserDTO dto = new UserDTO("john", "Aa1!aaaa");
        String result = authService.registerUser(dto);
        assertEquals("UserName already existed", result);
        verify(userService, times(1)).findUserbyName("john");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void registerUser_whenNameInvalid_returnsErrorMessage() throws Exception {
        when(userService.findUserbyName("1abc")).thenReturn(null);
        UserDTO dto = new UserDTO("1abc", "Aa1!aaaa");
        String result = authService.registerUser(dto);
        assertEquals("Username must start with a letter", result);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void registerUser_whenPasswordWeak_returnsErrorMessage() throws Exception {
        when(userService.findUserbyName("alice")).thenReturn(null);
        UserDTO dto = new UserDTO("alice", "weakpass");
        String result = authService.registerUser(dto);
        assertEquals("Password is to week", result);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void registerUser_whenAllGood_savesUserAndReturnsSuccess() throws Exception {
        when(userService.findUserbyName("bob")).thenReturn(null);
        when(passwordEncoder.encode("Aa1!aaaa")).thenReturn("hashed");
        UserDTO dto = new UserDTO("bob", "Aa1!aaaa");
        String result = authService.registerUser(dto);
        assertEquals("Registration successful", result);
        verify(passwordEncoder, times(1)).encode("Aa1!aaaa");
        verify(userService, times(1)).saveUser(any());
    }
}
