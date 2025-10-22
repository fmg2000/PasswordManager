
import { useParams, useNavigate} from "react-router-dom";
import { useEffect, useState } from "react";
import styles from "../Menu.module.css";
import { getSessionKey } from "../Crypto/SeesionKey";
import { decryptEntryKey, deriveKey, encryptEntryKey } from "../Crypto/Crypto";
import { pre } from "framer-motion/m";

function VaultEntryPage() {
  

  const {id}  = useParams(); //extragem userId din URL
  const navigate = useNavigate();
  const [vault, setVault] = useState(null);
  const [vaultcopy, setVaultCopy] = useState(null);

  const [timer, setTimer] = useState(0);
  const[timerActive, setTimerActive] = useState(false);
  const[showPass, setShowPass] = useState(false);
  const[showAlert, setShowAlert] = useState(false);
  const[showDeleteAlert, setshowDeleteAlert] = useState(false);
  const[errorMessage, setErrorMessage] = useState("");

  const key = getSessionKey();

  /// la creare de componenta obtinem Vault id
  useEffect(() =>{
    
    const getVaultId = async() => {

      const token = localStorage.getItem("accessToken");
      try{
          const resposne = await fetch(`http://localhost:8080/vault/get/${id}`,{
            method:"GET",
            headers:{"Authorization": `Bearer ${token}`}
          });
          if(resposne.ok)
          { 
            const obj = await resposne.json();
            setVault(obj);
            setVaultCopy(obj);
            return;
          }
          if(resposne.status === 401)
          {
            localStorage.removeItem("accessToken");
            navigate('/auth',{replace:true});
            return;
          }

      }catch(e){
        localStorage.removeItem("accessToken");
        navigate('/auth', {replreplaceace:true});
        return;
      }
    }

  getVaultId();

  },[]);

  //save
  const saveButton = async() =>{
    
    let obj = null
    const token = localStorage.getItem("accessToken");
    console.log(vault.passwordCrypt);
    if(timerActive==true)
        return;

    if(vault.passwordCrypt !== vaultcopy.passwordCrypt){
      // cream un dto si salvam obectul 
      const crypt = await encryptEntryKey(vault.passwordCrypt,key);
      setVault(pre=>({ ...pre, passwordCrypt: crypt.ciphertext, iv:crypt.iv}));
      const newobj ={
        id: vault.id,
        domain:vault.domain,
        username:vault.username,
        passwordCrypt:crypt.ciphertext,
        iv:crypt.iv,
        user:vault.user
      }
      obj = newobj;
    }

    console.log(obj);

    //apelam server /update
    try{
          const resposne = await fetch("http://localhost:8080/vault/update",{
            method:"PUT",
            headers:{"Content-Type" : "application/json",
                     "Authorization": `Bearer ${token}`},
            body: JSON.stringify(obj ? obj : vault)
          });

          const msg = await resposne.text();
          if(resposne.ok)
          { 
              setShowAlert(true);
              const timeout = setTimeout(()=>{
                setErrorMessage("");
                setShowAlert(false);
                setVault(obj ? obj : vault)
                setVaultCopy(obj ? obj : vault)
              },1000);
            
          }

          if(resposne.status === 400)
          {
            setErrorMessage(msg);
            return;
          }

          if(resposne.status === 401)
          {
            setErrorMessage(msg);
            localStorage.removeItem("accessToken");
            navigate('/auth',{replace:true});
            return;
          }

      }catch(e){

        localStorage.removeItem("accessToken");
        navigate('/auth', {replreplaceace:true});
        return;
      }

  };

  //cancelButton
  const cancelButton =()=>{
      setErrorMessage("");
      setVault(vaultcopy);
  };

  //alertCancel
  const alertCancelButton = ()=>{
    setshowDeleteAlert(false);
  };

  //deleteButton  = apeleaza un mesaj de alert pentru sterger " Doriti sa stergeti ....."
  const deleteButton = ()=>{
    setshowDeleteAlert(true)
  };

  // functie pentru stergere a vault
  const deleteConfirmButton = async ()=>{
      
    const token = localStorage.getItem("accessToken");
    try{
          const resposne = await fetch("http://localhost:8080/vault/delete",{
            method:"DELETE",
            headers:{"Content-Type" : "application/json",
                    "Authorization": `Bearer ${token}`},
            body: JSON.stringify(vault)
          });

          if(resposne.ok)
          { 
              setshowDeleteAlert(false);
              setShowAlert(true);
              const timeout = setTimeout(()=>{
                setShowAlert(false);
                navigate("/menu/list", {replace:true});
              },1000);
            
          }
          if(resposne.status === 401)
          {
            localStorage.removeItem("accessToken");
            navigate('/auth',{replace:true});
            return;
          }

      }catch(e){

        localStorage.removeItem("accessToken");
        navigate('/auth', {replreplaceace:true});
        return;
      }
  };

  const showPassButton = () => {
    setShowPass(prev => !prev);
  };


  ///show decript pass pentru 10 secunde
  useEffect(()=>{
    if (!timerActive) return; // nu porni timerul dacă nu e activ    
    setTimer(10);
    const interval = setInterval(() => {
    setTimer(prev => {
      if(prev <= 1){
          setVault(vaultcopy);
          clearInterval(interval);
          setTimerActive(false);
          return 0;
      }
      return prev-1; 
      });
    },1000);
    return() => {clearInterval(interval);}
    
  },[timerActive]);

  //buttondecrypt 
  const decryptButton = async () =>{
    
    console.log(vault.iv);
    try{  
        const passwordDecrypt = await decryptEntryKey(vault.passwordCrypt, vault.iv, key);
        setVault(prev => ({ ...prev, passwordCrypt: passwordDecrypt}));
        setTimerActive(true);
      }
    catch(e){
        console.error("Decrypt failed:", e);}
  };

  //Generate password
  const generatePassButton = async()=>{

    if(timerActive==true)
        return;

    const token = localStorage.getItem("accessToken");
    try{
        const token = localStorage.getItem("accessToken")
        const response = await fetch("http://localhost:8080/generate/password", {
            method:"GET",
            headers: {"Authorization": `Bearer ${token}`}
            });

        if(response.ok){
          const pass = await response.text();
          setVault(prev => ({...prev, passwordCrypt: pass}));
          return;
        }

        if(response.status == 401){
          localStorage.removeItem("accessToken");
          navigate("/auth", {replace : true});
          return;
       }

    }catch(e){
      localStorage.removeItem("accessToken");
      navigate("/auth", {replace : true}); // navigeaza la auth si sterge history inlocuieste 
    }
  };


  const backtoList = () =>{
    navigate("/menu/list");
  }

  return (
    <div className={styles.page}>
      {showAlert ? (
        <div className={styles.alertOverlay}>
            <div className={styles.saveAlert}>
              <p>Success...</p>
            </div>
        </div>
      ):null}

      {showDeleteAlert ? (
         <div className={styles.alertOverlay}>
            <div className={styles.deleteAlert}>
                <p>Are you sure you want to delete this account?</p>
                <div class={styles.actionsAlert}>
                  <button  className="btn btn-secondary" type="button" onClick={alertCancelButton}>NO</button>
                  <button className="btn btn-primary" type="button" onClick={deleteConfirmButton}>Yes</button>
                </div>
            </div>
        </div>
      ):null}

      <header className={styles.header}>
        <div>
          <h2 >Account</h2>
          <p className={styles.subtitle}>View & edit your saved credentials.</p>
        </div>

        <div className={styles.actions}>
          <button className="btn btn-success" onClick={saveButton}>Save</button>
          <button className="btn btn-secondary" onClick={cancelButton}>Cancel</button>
        </div>
      </header>

      <section className={styles.grid}>
        <div className={styles.cardPage}>
          <label className={styles.labelPage}>Domain</label>
          <input className={styles.input} type="text" value={vault ? vault.domain : ""} onChange={(e)=>setVault(prev => ({ ...prev, domain: e.target.value}))}/>

          <label className={styles.labelPage}>Username / Email</label>
          <input className={styles.input} type="text" value={vault ? vault.username : ""} onChange={(e)=>setVault(prev => ({ ...prev, username: e.target.value}))}/>

          <div className={styles.actions}>
              <label className={styles.labelPage}>Password</label>
              {timerActive && <p className={styles.timer}>{timer}</p>}
          </div>
          <div className="form-group">
            <div className="input-group mb-3">
              <input className={styles.labelPassword} type={showPass ? "text" : "password"} value={vault ? vault.passwordCrypt : ""} onChange={(e)=>setVault(prev => ({ ...prev, passwordCrypt: e.target.value}))}/>
              <button type="button" className="btn btn-outline-secondary" onClick={showPassButton}>Show</button>
              <button type="button" className="btn btn-outline-primary" onClick={decryptButton}>Decrypt</button>
              <button type="button" className="btn btn-primary" onClick={generatePassButton}>Generate</button>
            </div>
          </div>
           {errorMessage && <p className={styles.errorText}>{errorMessage}</p>}
        </div>
      </section>

      {/* FOOTER */}
      <footer className={styles.footer}>
        <button className="btn btn-outline-secondary"onClick={backtoList}>← Back to list</button>
        <button className="btn btn-outline-danger" type="button" onClick={deleteButton}>Delete</button>
      </footer>
    </div>

  );
}

export default VaultEntryPage;
