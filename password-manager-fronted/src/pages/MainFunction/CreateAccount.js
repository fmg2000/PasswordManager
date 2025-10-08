
import styles from "../Menu.module.css";

import {useState} from "react"
import { getSessionKey } from "../Crypto/SeesionKey";


function CreateAccount(){
    
    const[showPass ,setShowPass] = useState(false)
    const[domain, setDomain] = useState("");
    const[username, setUsername] = useState("");  
    const[pass, setPass] = useState("");      
    
    const key = getSessionKey();

    const generatePass =() =>{
      /// implement  
    }

    const hidePass =() =>{
        setShowPass(!showPass)
    }

    const registerSubmit = (e) =>{
            e.preventDefault();
            

    };

    return(
        <div className={styles.containerInfo}>
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



            </form>
        </div>
    );
}

export default CreateAccount;