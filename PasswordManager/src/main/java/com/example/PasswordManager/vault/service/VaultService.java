package com.example.PasswordManager.vault.service;

import com.example.PasswordManager.user.repo.UserRepository;
import com.example.PasswordManager.vault.model.Vault;
import com.example.PasswordManager.vault.repo.RepoVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// * Serviciu care gestionează logica pentru User *
@Service
public class VaultService {

    @Autowired
    private RepoVault repoVault;

    @Autowired
    private UserRepository userRepository;

    /// return un obiect Vault după ID.
    public Vault getVault(Long id) {
        return repoVault.findById(id).orElse(null);

    }
    /// return lista cu vault a unui User
    public List<Vault> getVaultList(String userName) {
        return repoVault.findByUserUsernameOrderByIdAsc(userName);
    }

    /// creaza un vault in db
    public boolean createVault(Vault vault, String userName) {

        vault.setUser(userRepository.findByUsername(userName));
        if(repoVault.findByDomainAndUserUsername(vault.getDomain(),userName).isPresent()){
            return false;
        }
        repoVault.save(vault);
        return true;
    }

    ///update la un vault
    public boolean updateVault(Vault vault, String userName) {
        vault.setUser(userRepository.findByUsername(userName));
        if(repoVault.findByDomainAndUserUsername(vault.getDomain(),userName).isPresent() && !Objects.equals(vault.getId(), repoVault.findByDomainAndUserUsername(vault.getDomain(), userName).get().getId())){
            return false;
        }
        repoVault.save(vault);
        return true;
    }

    /// delete
    public void deleteVault(Vault vault) {
        repoVault.delete(vault);
    }

    ///serach dupa un vault in functie de domain
    public List<Vault> searchVaults(String username, String domain) {
        return repoVault.findByUserUsernameAndDomainContainingIgnoreCase(username, domain);
    }
}
