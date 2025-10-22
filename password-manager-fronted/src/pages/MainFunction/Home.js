import Menu from "../Menu";
import styles from "../Menu.module.css";
function Home(){

    return(
        <div className ={styles.homeinfo}>
            <h2>Welcome to PasswordManager</h2>
            <p> PasswordManager is an application that helps you securely store and manage your passwords. All your data is encrypted, and only you have access to it.</p>
            <p> You can add, view, or generate new passwords, as well as decrypt and display them safely when needed.</p>
            <p><strong>Don`t forget your Master Password — without it, you won`t be able to access your vault!</strong></p>
        </div>
    );
}

export default Home