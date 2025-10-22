
import styles from "../Menu.module.css";

import {useState} from "react"
import { getSessionKey } from "../Crypto/SeesionKey";
import {encryptEntryKey} from "../Crypto/Crypto";
import { replace, useNavigate } from "react-router-dom";


function CreateAccount(){

    const[showAlert, setShowAlert] = useState(false);
    const[showPass ,setShowPass] = useState(false);
    const[errorMessage, setErrorMessage] = useState("");
    
    const[domain, setDomain] = useState("");
    const[username, setUsername] = useState("");  
    const[pass, setPass] = useState("");   
    
    const key = getSessionKey();

    const navigate = useNavigate();
    
    const obj = {
        domain: domain,
        username:username,
        passwordCrypt:"",
        iv:"",
    };

    //buton de ascundere password folosind {verify? aaa:bbb}
    const hidePass =() =>{
        setShowPass(!showPass)
    };

    //apelam server pentru creare de password
    const generatePass = async (e) =>{
            try{

                const token = localStorage.getItem("accessToken")
                const response = await fetch("http://localhost:8080/generate/password", {
                    method:"GET",
                    headers: {"Authorization": `Bearer ${token}`}
                });

                if(response.ok){
                    setPass(await response.text());
                    return;
                }

                if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth", {replace : true});
                    return;
                }

            }
            catch(e){
                localStorage.removeItem("accessToken");
                navigate("/auth", {replace : true}); // navigeaza la auth si sterge history inlocuieste 
            }
    };

    //apelam server pentru adaugare vault  
    const registerSubmit = async (e) =>{
            e.preventDefault();
            
            const objencrypt = await encryptEntryKey(pass,key);
            obj.passwordCrypt = objencrypt.ciphertext;
            obj.iv = objencrypt.iv;
            console.log(obj);
            
            const token = localStorage.getItem("accessToken");

            try{
                const response = await fetch("http://localhost:8080/vault/create", {
                    method:"POST",
                    headers:{"Content-Type" : "application/json",
                            "Authorization" : `Bearer ${token}`},
                    body: JSON.stringify(obj)
                });
                const message = await response.text()    
                console.log(message);
                if(response.ok){
                    setShowAlert(true); // facem timeout de o secunda pentru animatie 
                     const timeout = setTimeout(()=>{
                            setShowAlert(false);
                            setErrorMessage("");
                            setDomain("");
                            setPass("");
                            setUsername("");
                    },1000);    
                }
                
                if(response.status == 400){   
                     setErrorMessage(message);
                     return;
                }
                
                if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth",  { replace: true });
                    return;
                }
            }
            catch(e){
                localStorage.removeItem("accessToken");
                navigate("/auth",  { replace: true }); // înlocuiește ruta curentă;
            }
    };

    return(
        <div className={styles.containerInfo}>
             {showAlert ? (
                    <div className={styles.alertOverlay}>
                        <div className={styles.saveAlert}>
                          <p>Success...</p>
                        </div>
                    </div>
                  ):null}
           <h2>Create Account</h2>
            <form onSubmit={registerSubmit}>
                <div className="form-group">
                    <label htmlFor="domainInput">Domain</label>
                    <input type="text" className="form-control" id="domainInput" value={domain} onChange={(e) => setDomain(e.target.value)}/>
                    <small className="form-text text-muted">Ex: www.Netflix.com , Microsoft</small>
                </div>
                <div className="form-group">
                    <label htmlFor="exampleInputEmail1">Email address / Username</label>
                    <input type="text" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" value={username} onChange={(e) => setUsername(e.target.value)}/>
                    <small className="form-text text-muted">Enter username or email.</small>
                </div>
                <div className="form-group">
                    <label htmlFor="exampleInputPassword1">Password</label>
                    <div className="input-group mb-3">
                        <input type={showPass ? "text" : "password"} className="form-control" aria-label="Text input with segmented dropdown button" value={pass} onChange={(e) => setPass(e.target.value)}/>
                        <button type="button" className="btn btn-outline-secondary" onClick={hidePass}>Show</button>
                        <button type="button" className="btn btn-outline-primary" onClick={generatePass} >Generate</button>
                    </div>
                    <small className="form-text text-muted">Password must contain Az!2</small>
                </div>
                
                <div className={styles.button}>
                    <button type="submit" className="btn btn-primary">Submit</button>
                </div>
                {errorMessage && <p className={styles.errorText}>{errorMessage}</p>}

            </form>
        </div>
    );
}

export default CreateAccount;