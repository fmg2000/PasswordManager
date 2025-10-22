package com.example.PasswordManager.vault.repo;

import com.example.PasswordManager.vault.model.Vault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoVault extends JpaRepository<Vault,Long> {
    Optional<Vault> findByDomainAndUserUsername(String domain, String username);
    List<Vault> findByUserUsernameOrderByIdAsc(String username);
    List<Vault> findByUserUsernameAndDomainContainingIgnoreCase(String username, String domain);

    Optional<Vault> findByIdAndUserUsername(Long id, String userName);
}
