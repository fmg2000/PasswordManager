import { Routes, Route, NavLink , Navigate} from "react-router-dom";
import Listitem from "./MainFunction/ListItem.js";
import CreateAccount from "./MainFunction/CreateAccount.js";
import Home from "./MainFunction/Home.js";
import styles from "./Menu.module.css";
import { useEffect } from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {decryptEntry, deriveKey, encryptEntry} from "./Crypto/Crypto.js";
import { setSessionKey } from "./Crypto/SeesionKey.js";
 
function Menu(){

    const [meta, setMeta] = useState(null);
    const [showAlert, setShowAlert] = useState(false);
    const [pass, setPassword] = useState("");
    const [ok, setOk] = useState(false);
    const [counter, setCounter] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {

        const fetchData = async () =>{
            const token = localStorage.getItem("accessToken");
            if(!token){
                navigate("/auth");
                return;
            }

            const validToken = await checkAuth(token);
            if (!validToken) {
                localStorage.removeItem("accessToken");
                navigate("/auth");
                return;
            }

            if (counter === 0) {
                await getMeta(token);
                setShowAlert(true);
            }

        };
        fetchData();
    
    },[counter]);

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
                    navigate("/auth")
                    return;
                }

                if(rez.ok){
                    const data = await rez.json();
                    setMeta(data);
                }
           }
        catch(e){
                navigate("/auth");
            }
    }


    const submit = async () =>{
        
        const token = localStorage.getItem("accessToken");
        
        if(meta.saltMaster==null)
        {
            const encrypt = await encryptEntry("OK",pass, meta.saltMaster)
            meta.saltMaster = encrypt.salt;
            meta.verifyCyper = encrypt.ciphertext;
            meta.verifyIv = encrypt.iv;
            
            const response = await fetch("http://localhost:8080/user/encript",{
                
                    method:"POST",
                    headers:{
                           "Content-type": "application/json",
                            "Authorization" : `Bearer ${token}` },
                    body:JSON.stringify(meta)
                    });
            
            if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth")
                    return;
                }

            if(response.ok){
                setShowAlert(false);
                const { key } = await deriveKey(pass, meta.saltMaster);
                setSessionKey(key);
            }
                
        }
        else
        {
            try{  
                const validToken = await checkAuth(token);
                if (!validToken) {
                    localStorage.removeItem("accessToken");
                    navigate("/auth");
                    return;
                }

                const objdecrypt = {salt: meta.saltMaster, ciphertext: meta.verifyCyper, iv: meta.verifyIv}
                console.log("heyy");
                const decrypt = await decryptEntry(objdecrypt,pass)
                if(decrypt == "OK"){
    
                    const { key , saltkey} = await deriveKey(pass, meta.saltMaster);
                    setSessionKey(key);
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
                    <h3 className={styles.titlebar}>PasswordManager</h3>
                    <nav className="nav flex-column">
                        {/* isActive functie data de NavLink daca link este pe ruta respectiva  */}
                            <NavLink to="/menu/home" end className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)} onClick={setClick}>Home</NavLink>
                            <NavLink to="/menu/list" className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)}onClick={setClick}>My Accounts</NavLink>
                            <NavLink to="/menu/new-account" className={({ isActive }) => (isActive ? `${styles.link} ${styles.active}` : styles.link)}onClick={setClick}>New Account</NavLink>                
                    </nav>
                </div>
                <div className={styles.content}>
                    <Routes>
                        <Route path="/" element={ <Navigate to = "home" />}/>
                        <Route path="list" element={<Listitem />}/>
                        <Route path="new-account" element={<CreateAccount />}/>
                        <Route path="home" element={<Home />}/>
                    </Routes>
                </div>
            </div>
        </div>

    );
}
export default Menu;
