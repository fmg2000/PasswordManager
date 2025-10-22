package com.example.PasswordManager;

import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.repo.UserRepository;
import com.example.PasswordManager.vault.model.Vault;
import com.example.PasswordManager.vault.repo.RepoVault;
import com.example.PasswordManager.vault.service.VaultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaultServiceTest {

    @Mock
    private RepoVault repoVault;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VaultService vaultService;

    @Test
    void createVault_whenDomainExists_returnsFalse() {
        Vault v = new Vault();
        v.setDomain("example.com");
        when(repoVault.findByDomainAndUserUsername("example.com", "alice")).thenReturn(Optional.of(v));

        boolean ok = vaultService.createVault(v, "alice");
        assertFalse(ok);
        verify(repoVault, never()).save(any());
    }

    @Test
    void createVault_whenNew_savesVaultAndReturnsTrue() {
        Vault v = new Vault();
        v.setDomain("newsite.com");
        User user = new User();
        user.setUsername("bob");
        when(userRepository.findByUsername("bob")).thenReturn(user);
        when(repoVault.findByDomainAndUserUsername("newsite.com", "bob")).thenReturn(Optional.empty());

        boolean ok = vaultService.createVault(v, "bob");
        assertTrue(ok);
        assertEquals(user, v.getUser());
        verify(repoVault, times(1)).save(v);
    }

    @Test
    void getVaultList_returnsListFromRepo() {
        Vault v1 = new Vault(); v1.setId(1L);
        Vault v2 = new Vault(); v2.setId(2L);
        when(repoVault.findByUserUsernameOrderByIdAsc("bob")).thenReturn(List.of(v1, v2));

        List<Vault> res = vaultService.getVaultList("bob");
        assertEquals(2, res.size());
        verify(repoVault, times(1)).findByUserUsernameOrderByIdAsc("bob");
    }

    @Test
    void getVault_returnsVaultOrNull() {
        Vault v = new Vault(); v.setId(5L);
        when(repoVault.findById(5L)).thenReturn(Optional.of(v));
        assertNotNull(vaultService.getVault(5L));
        when(repoVault.findById(10L)).thenReturn(Optional.empty());
        assertNull(vaultService.getVault(10L));
    }
}
