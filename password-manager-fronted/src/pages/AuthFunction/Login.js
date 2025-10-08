import styles from "../Auth.module.css";
import { useNavigate } from "react-router-dom";
import { useState } from "react";


function Login(){

        const navigate = useNavigate();

        // state pentru usename si password
        const [email, setEmail] = useState("");
        const [pass, setPassword] = useState("");
        const [errorMessage, setErrorMessage] = useState("");

        // json pentru body
        const user = {
            username: email,
            password:pass
        }

        const goToRegister = () =>{
                navigate("/auth/register");
        };

        const goToHome = () => {
            navigate("/menu");
        };

                
        const loginSubmit = async (e) =>{
            e.preventDefault();
            setErrorMessage("");

            try
            {
                const rez = await fetch("http://localhost:8080/auth/login", {
                    method:"POST",
                    headers: {"Content-Type": "application/json" },
                    body: JSON.stringify(user)
                    });

                const msg = await rez.text();
                console.log("salut");
                if(rez.ok){
                    localStorage.setItem("accessToken", msg);
                    goToHome();
                }
                else
                    setErrorMessage(msg);

            }catch (error){
                setErrorMessage("Conenction failed");
            }

        };
    

  return(
       
       <div>
        <h2>Login</h2>
        <form onSubmit={loginSubmit}>
            <div className="form-group">
                <label htmlFor="exampleInputEmail1">Email address / Username</label>
                <input type="text" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" value={email} onChange={(e) => setEmail(e.target.value)}/>
                <small id="emailHelp" className="form-text text-muted">Enter username or email.</small>
            </div>
            <div className="form-group">
                <label htmlFor="exampleInputPassword1">Password</label>
                <input type="password" className="form-control" id="exampleInputPassword1" value={pass} onChange={(e)=> setPassword(e.target.value)}/>
            </div>
            <div className={styles.LoginButon}>
                <button type="submit" className="btn btn-primary">Submit</button>
                <button type="button" className="btn btn-primary" onClick={goToRegister}>Create an account</button>
            </div>
           {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
        </form>
        </div>
    
    );
}
export default Login;