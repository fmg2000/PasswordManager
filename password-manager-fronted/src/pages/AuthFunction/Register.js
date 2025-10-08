import styles from "../Auth.module.css";
import { useNavigate } from "react-router-dom"; // folosit pentru navigate
import {useState} from "react"; //folosit pentru stari 

function Register(){

    const navigate = useNavigate()

      // state pentru input-uri le actualizeaza automat 
    const [email, setEmail] = useState("");
    const [pass1, setPass1] = useState("");
    const [pass2, setPass2] = useState("");
    const [errorMessage,setErrorMessage] = useState("");


    // cream un obiect user;
    const user = {
        username: email,
        password: pass1
    }


    const goToLogin = () =>{
        navigate("/auth/login");
    };

    const registerSubmit = async (e) =>{
        e.preventDefault();
        setErrorMessage("");

        console.log("Email:", email);
        console.log("Parola:", pass1);
        console.log("Confirmare:", pass2);

        if (pass1 !== pass2) {
        setErrorMessage("Password not matching");
        return;
        }

        
        try{
            const res = await fetch("http://localhost:8080/auth/register", {
                
                method:"POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
            
            });
            const message = await res.text();

            if(res.ok){
                goToLogin();
            }
            else
                setErrorMessage(message);

        }
        catch(error){
            setErrorMessage("Connection failed");
        }

        // aici vei face fetch spre Spring Boot /register
    };



    return(
        <div>
            <h2>Register</h2>
            <form onSubmit={registerSubmit}>
                <div className="form-group">
                    <label htmlFor="exampleInputEmail1">Email address / Username</label>
                    <input type="text" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" value={email} onChange={(e) => setEmail(e.target.value)}/>
                    <small id="emailHelp" className="form-text text-muted">Enter username or email.</small>
                </div>
                <div className="form-group">
                    <label htmlFor="exampleInputPassword1">Password</label>
                    <input type="password" className="form-control" id="exampleInputPassword1" value={pass1} onChange={(e) => setPass1(e.target.value)}/>
                    <small id="passwordHelp" className="form-text text-muted">Password must contain Az!2</small>
                </div>

                <div className="form-group">
                    <label htmlFor="exampleInputPassword2">Confirm Password</label>
                    <input type="password" className="form-control" id="exampleInputPassword2"  value={pass2} onChange={(e) => setPass2(e.target.value)}/>
                </div>

                <div className={styles.LoginButon}>
                    <button type="submit" className="btn btn-primary">Submit</button>
                    <button type="button" className="btn btn-primary" onClick={goToLogin}>Login</button>
                </div>

                {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}

            </form>

        </div>
    );
}

export default Register;