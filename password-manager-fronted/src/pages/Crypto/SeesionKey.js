let sessionKey = null;

export function setSessionKey(key) {
  sessionKey = key;     
}

export function getSessionKey() {
  return sessionKey;     
}

export function clearSessionKey() {
  sessionKey = null;    
}