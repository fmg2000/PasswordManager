package com.example.PasswordManager.vault.controller;

import com.example.PasswordManager.util.ValidName;
import com.example.PasswordManager.vault.model.Vault;
import com.example.PasswordManager.vault.service.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("vault")
@CrossOrigin
public class VaultController {

    @Autowired
    private VaultService vaultService;


    /**
     * Returnează un obiect Vault (parolă salvată) după ID.
     *
     * @param id - identificatorul unic al obiectului Vault
     * @return 200 OK cu obiectul Vault dacă este găsit, altfel 404 Not Found
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Vault> getVault(@PathVariable Long id)
    {
        Vault vault = vaultService.getVault(id);
        if(vault != null)
            return ResponseEntity.ok(vault);
        return ResponseEntity.notFound().build();
    }

    /**
     * Returnează lista tuturor înregistrărilor din "vault" asociate utilizatorului autentificat.
     *
     * @param p - Principal (utilizatorul curent)
     * @return listă de obiecte Vault ordonate după ID
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<Vault>> getVaultList(Principal p)
    {
        List<Vault> vaultList = vaultService.getVaultList(p.getName());
        return ResponseEntity.ok(vaultList);
    }

    /**
     * Creează o nouă înregistrare Vault (o parolă salvată pentru un domeniu).
     * Verifică dacă valorile sunt valide și dacă domeniul nu există deja.
     *
     * @param vault - obiectul Vault trimis în corpul cererii
     * @param p - utilizatorul autentificat
     * @return 200 OK dacă este creat cu succes, 400 Bad Request dacă domeniul există deja
     */
    @PostMapping("/create")
    public ResponseEntity<String> createVault(@RequestBody  Vault vault, Principal p)
    {
        if (!ValidName.verifyName(vault.getDomain()) || !ValidName.verifyName(vault.getUsername())) {
            return ResponseEntity.badRequest().body("Field must either be empty or start with a letter.");
        }
        if (vault.getPasswordCrypt().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid password null");
        }
        boolean ok = vaultService.createVault(vault,p.getName());
        if(ok)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("Domain already exists");
    }

    /**
     * Actualizează o înregistrare Vault existentă.
     *
     * @param vault - obiectul Vault actualizat
     * @return 200 OK dacă actualizarea a avut succes
     */

    @PutMapping("/update")
    public ResponseEntity<String> updateVault(@RequestBody  Vault vault, Principal p)
    {
        if (!ValidName.verifyName(vault.getDomain()) || !ValidName.verifyName(vault.getUsername())) {
            return ResponseEntity.badRequest().body("Field must either be empty or start with a letter.");
        }
        if (vault.getPasswordCrypt().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid password null");
        }
        boolean ok = vaultService.updateVault(vault,p.getName());
        if(ok)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("Domain already exists");
    }


    /**
     * Șterge o înregistrare Vault existentă.
     *
     * @param vault - obiectul Vault care urmează să fie șters
     * @return 200 OK după ștergere
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteVault(@RequestBody  Vault vault)
    {
        vaultService.deleteVault(vault);
        return ResponseEntity.ok().build();
    }

    /**
     * Caută înregistrările Vault ale utilizatorului curent, filtrând după domeniu (insensibil la majuscule).
     *
     * @param domain - domeniul căutat (ex: "google", "facebook")
     * @param p - utilizatorul curent
     * @return listă de Vault-uri care conțin domeniul căutat
     */
    @GetMapping("/search")
    public ResponseEntity<List<Vault>> searchVaults(@RequestParam String domain, Principal p){

        return ResponseEntity.ok(vaultService.searchVaults(p.getName(), domain));
    }




}
