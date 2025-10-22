
import {useEffect, useState } from "react";
import {useNavigate } from "react-router-dom";
import { getSessionKey } from "../Crypto/SeesionKey";
import { decryptEntryKey, deriveKey } from "../Crypto/Crypto";
import styles from "../Menu.module.css";


function Listitem (){

    const [listItem, setListItem] = useState([]);
    const [allItems, setAllItems] = useState([]);
    const [revealed, setRevealed] = useState(null)
    const [dtoObj,setDto] = useState(null);
    const [showPass,setShowPass] = useState(false);
    const[timer, setTimer] = useState(10);
    const[search,setSearch] = useState("");

    const navigate = useNavigate();

    //cheia de decriptare 
    const key = getSessionKey();

    ///la constructia componentei se apeleaza, creaza request la server si obtine lista de vault 
    useEffect(()=>{
        const fetchData = async(e) => {

            try{
                const token = localStorage.getItem("accessToken");
                const response = await fetch("http://localhost:8080/vault/getAll", {
                    method: "GET",
                    headers:{"Authorization" : `Bearer ${token}`}
                });

                if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth", {replace:true});
                    return;
                }

                const list = await response.json();
                setListItem(list);
                setAllItems(list);
            }
            catch(e){
                localStorage.removeItem("accessToken");
                navigate("/auth", {replace:true});
            }

        };
        fetchData();
    },[]);

    // se apeleaza defiecare data cand se schimba showpass pentru a arata parola cu timer
    useEffect(()=>{

        if(showPass == false)
        {
                return;
        }
         
        //setam timer pentru 10 secunde si afisam parola cu un counter descrescator 
        setTimer(10);
        const interval = setInterval(() => {
            setTimer(prev => {
                if(prev <= 1){
                   clearInterval(interval);
                   setShowPass(false);
                   console.log("sunt aici ");
                   return 0;
                }
               return prev-1; 
            });
        },1000);

        return() => {
            clearInterval(interval);
        }
        
    },[showPass]);

    // pentru functia de search se apelaza la fiecare schimbare de search 
    useEffect(()=>{

        const searchFunction = async() =>{
            
            //daca nu avem litere, trim() elimina spatiile 
            if (!search.trim()) {
                setListItem(allItems);
                return;
            }

            try{
                const token = localStorage.getItem("accessToken");
                const params = new URLSearchParams({domain:search}).toString();
                const response = await fetch(`http://localhost:8080/vault/search?${params}`,{
                    method: "GET",
                    headers:{"Authorization" : `Bearer ${token}`}   
                });

                if(response.status == 401){
                    localStorage.removeItem("accessToken");
                    navigate("/auth", {replace:true});
                    return;
                }

                const list = await response.json();
                setListItem(list);
            }
            catch(e){
                localStorage.removeItem("accessToken");
                navigate("/auth", {replace:true});
            }
        }
        searchFunction();

    },[search]);

    // decrypt button pentru decrypt parola 
    const decrypt = async(entity)=>{
        try{  

            const passwordDecrypt = await decryptEntryKey(entity.passwordCrypt, entity.iv, key);
            setShowPass(true);
            setRevealed({ id: entity.id, passwordDecrypt: passwordDecrypt});
        }
        catch(e){
            console.error("Decrypt failed:", e);
        }
        
    };

    //VaultPage cu link dinamic 
    const goToItem = (id) => {navigate(`/menu/item/${id}`)}

    return(
        <div>
            <div className={styles.search}>
                <h5 className={styles.textSearch}>Search:</h5>
                <input className={styles.inputSearch} type="text" value={search} onChange={(e) => setSearch(e.target.value)}/>
            </div>
         

            {listItem.map(item=>(
                <div key = {item.id} className={styles.card} role="button" onClick={() => goToItem(item.id)}>

                        {/* Hedear Card */}
                         <div className={styles.cardHeader}>
                            <div className={styles.titleGroup}>
                                <div className={styles.domain}>{item.domain}</div>
                                <div className={styles.subtle}>ID: {item.id}</div>
                            </div>
                            {showPass && revealed.id === item.id ?<span className={styles.timer}>{timer}</span>:null}                        
                        </div>
                        <div className={styles.row}>
                             <span className={styles.label}>Domain: </span>
                             <span className={styles.value}>{item.domain}</span>
                        </div>
                        <div className={styles.row}>
                             <span className={styles.label}>Email/Username:</span>
                             <span className={styles.value}>{item.username}</span>
                        </div>
                        <div className={styles.row}>
                            <span className={styles.label}>Password</span>
                            <span className={styles.value} >{showPass && revealed.id === item.id ? revealed.passwordDecrypt : item.passwordCrypt}</span>
                            <button type="button" className="btn btn-outline-secondary" onClick={(e) => { e.stopPropagation();decrypt(item)} }>Decrypt</button>
                        </div>
                </div> 
            ))}
        </div>
    );
}

export default Listitem;