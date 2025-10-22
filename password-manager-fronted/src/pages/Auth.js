import styles from "./Auth.module.css";
import { Routes, Route, Navigate } from "react-router-dom";
import Register from "./AuthFunction/Register";
import Login from "./AuthFunction/Login";

function Auth(){
    return(
        <div className={styles.CardAuth1}>
            <div className={styles.CardAuth2}> 
        <Routes> {/*rutele aici is componentele copi */}
            <Route path="/" element={<Navigate to="login" replace />} />
            <Route path="login" element={<Login />} />
            <Route path="register" element={<Register />} />
       </Routes>
            </div>
        </div>
    );
}
export default Auth;