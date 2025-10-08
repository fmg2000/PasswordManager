
//AES-GCM (Advanced Encryption Standard – Galois/Counter Mode)/
//AES = standardul de criptare simetrică (128/192/256 biți).
//GCM = modul de lucru al AES → oferă și confidențialitate, și 
// integritate (are un tag care verifică dacă datele nu au fost modificate).

// ---- utilitare text/Base64 ----
const enc = new TextEncoder();
const dec = new TextDecoder();
const toB64 = buf => btoa(String.fromCharCode(...new Uint8Array(buf)));
const fromB64 = b64 => Uint8Array.from(atob(b64), c => c.charCodeAt(0));

// ---- configurație (fixă în app, NU în DB) ----
const PBKDF2_ITERATIONS = 150_000;
const AES_KEY_BITS = 256;     // AES-256
const IV_BYTES = 12;          // recomandat la GCM (12)
const SALT_BYTES = 16;        // salt de 16 bytes

// 1) Derivează cheia din master password (creează salt nou dacă lipsește)
//PBKDF2 - creaza din master o key criptocrafica sigura
export async function deriveKey(masterPassword, saltB64) { 
  const salt = saltB64 ? fromB64(saltB64) : crypto.getRandomValues(new Uint8Array(SALT_BYTES)); // daca exista salt si daca nu creaza
  const pwKey = await crypto.subtle.importKey("raw", enc.encode(masterPassword), "PBKDF2", false, ["deriveKey"]);
  const key = await crypto.subtle.deriveKey(
    { name: "PBKDF2", hash: "SHA-256", salt, iterations: PBKDF2_ITERATIONS }, // cum fac derivarea
    pwKey, // cheia de baza din master
    { name: "AES-GCM", length: AES_KEY_BITS }, // cheia de final ce tip vreau
    false, // chiea exportabila = false
    ["encrypt", "decrypt"] // key - permisunea de encrypt decrypt
  );
  return  { key, saltB64: toB64(salt) };
}

// 2) Criptează parola site-ului -> obiect de salvat în DB
export async function encryptEntry(plaintextPassword, masterPassword, existsaltB64) {
  const iv = crypto.getRandomValues(new Uint8Array(IV_BYTES)); // IV - previne repetitia
  const { key, saltB64 } = await deriveKey(masterPassword, existsaltB64);
  const ct = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, enc.encode(plaintextPassword));
  return {
    ciphertext: toB64(ct), // conține și authentication tag
    iv: toB64(iv),
    salt: saltB64
  };
}

// 3) Decriptează un entry din DB -> parola în clar
export async function decryptEntry(entry, masterPassword) {
  const { key } = await deriveKey(masterPassword, entry.salt);
  const pt = await crypto.subtle.decrypt({ name: "AES-GCM", iv: fromB64(entry.iv) }, key, fromB64(entry.ciphertext));
  return dec.decode(pt);
}
