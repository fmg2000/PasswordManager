import { Routes, Route, NavLink , Navigate} from "react-router-dom";
import Listitem from "./MainFunction/ListItem.js";
import CreateAccount from "./MainFunction/CreateAccount.js";
import Home from "./MainFunction/Home.js";
import VaultPage from "./MainFunction/VaultPage.js";
import styles from "./Menu.module.css";
import { useEffect } from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {decryptEntry, deriveKey, encryptEntry} from "./Crypto/Crypto.js";
import { setSessionKey } from "./Crypto/SeesionKey.js";
 
function Menu(){

    console.log("[MENU] component mounted");
    const [meta, setMeta] = useState(null);
    const [showAlert, setShowAlert] = useState(false);
    const [pass, setPassword] = useState("");
    const [ok, setOk] = useState(false);
    const [counter, setCounter] = useState(0);
    const navigate = useNavigate();

    // se apelaza la creare primim metadatele si la fiecare clik pe nav counter s emodifica se apeleaza din nou 
    useEffect(() => {

        
        const fetchData = async () =>{
            const token = localStorage.getItem("accessToken");
            if(!token){
                navigate("/auth" ,{replace : true});
                return;
            }

            // verificam auth
            const validToken = await checkAuth(token);
            if (!validToken) {
                localStorage.removeItem("accessToken");
                navigate("/auth" ,{replace : true});
                return;
            }

            if (counter === 0) {
                await getMeta(token); // primim metadatele
                setShowAlert(true);
            }

        };
        fetchData();
    
    },[counter]);

    //verificam authentificarea
    async function checkAuth(token) {

        try {
            const res = await fetch("http://localhost:8080/auth/check", {
            headers: { "Authorization": `Bearer ${token}` },
            });
            return res.ok;
        }
        catch {
            return false;
        }
    }
    
    // luam metadatele
    async function getMeta(token) {

         try
            {
                const rez = await fetch("http://localhost:8080/user/meta", {
                    method:"GET",
                    headers:{
                        "Content-type": "application/json",
                        "Authorization" : `Bearer ${token}` },
                });

        
                if(rez.status == 401){

                    localStorage.removeItem("accessToken");
                    navigate("/auth" ,{replace : true});
                    return;
                }

                if(rez.ok){
                    const data = await rez.json();
                    setMeta(data);
                }
           }
        catch(e){
                navigate("/auth" ,{replace : true});
            }
    }


    //verificam passwordmaster daca nu exista setam si cream metadatele daca exista verificam 
    const submit = async () =>{
        
        const token = localStorage.getItem("accessToken");
        
        if(meta.saltMaster==null)
        {
            const encrypt = await encryptEntry("OK",pass, meta.saltMaster)
            meta.saltMaster = encrypt.salt;
            meta.verifyCyper = encrypt.ciphertext;
            meta.verifyIv = encrypt.iv;
            
            const response = await fetch("http://localhost:8080/user/encript",{
                
                    method:"PUT",
                    headers:{
                           "Content-type": "application/json",
                            "Authorization" : `Bearer ${token}` },
                    body:JSON.stringify(meta)
                    });
            
            if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth" ,{replace : true});
                    return;
                }

            if(response.ok){
                setShowAlert(false);
                const obj = await deriveKey(pass, meta.saltMaster);
                setSessionKey(obj.key);
            }
                
        }
        else
        {
            try{  
                const validToken = await checkAuth(token);
                if (!validToken) {
                    localStorage.removeItem("accessToken");
                    navigate("/auth" ,{replace : true});
                    return;
                }

                const objdecrypt = {salt: meta.saltMaster, ciphertext: meta.verifyCyper, iv: meta.verifyIv}
                console.log("heyy");
                const decrypt = await decryptEntry(objdecrypt,pass)
                if(decrypt == "OK"){
    
                    const obj = await deriveKey(pass, meta.saltMaster);
                    setSessionKey(obj.key);
                    setShowAlert(false);
                }
              }
            catch(e){
                    setOk(true);
              } 
        }

    }

    const setClick =() =>{
        setCounter(prev => prev + 1);
    }


    const logoutButton = ()=>{
        localStorage.clear();
        navigate("/auth" ,{replace : true});
    }

    return(
        <div>
            {showAlert ? 
                (
                    <div className={styles.alertOverlay}>
                        <div className={styles.alertBox}>
                            <p>Set your master password if you don’t have one yet, 
                                or enter it for verification if you’ve already created it. 
                                This password encrypts and decrypts your data. 
                                Keep it safe — we cannot recover it if you forget it.
                            </p>
                            <div className={styles.groupAlertBox}>
                                <input type="password" className={styles.inputAlertBox} value={pass} onChange={(e) => setPassword(e.target.value)}></input>
                                <button className="btn btn-secondary" onClick={submit}>Confirm</button>
                            </div>
                            {ok ? <p className={styles.errorMaster}>Password incorect</p>: null}
                        </div>
                    </div>
                ):null}
            <div className={styles.container}>
                <div className={styles.sidebar}>
                    <div>
                        <h3 className={styles.titlebar}>PasswordManager</h3>
                        <nav className="nav flex-column">
                            {/* isActive functie data de NavLink daca link este pe ruta respectiva  */}
                                <NavLink to="/menu/home" end className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)} onClick={setClick}>Home</NavLink>
                                <NavLink to="/menu/list" className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)} onClick={setClick}>My Accounts</NavLink>
                                <NavLink to="/menu/new-account" className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)}onClick={setClick}>New Account</NavLink>                
                        </nav>
                    </div>
                    <button type= "button" className={styles.logoutButton} onClick={logoutButton}>Logout</button>
                </div>
                <div className={styles.content}>
                    <Routes>
                        <Route path="/" element={ <Navigate to = "home" />}/>
                        <Route path="list" element={<Listitem />}/>
                        <Route path="new-account" element={<CreateAccount />}/>
                        <Route path="home" element={<Home />}/>
                        <Route path="item/:id" element={<VaultPage />} />
                    </Routes>
                </div>
            </div>
        </div>

    );
}
export default Menu;
