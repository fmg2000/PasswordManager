////package com.example.PasswordManager.account.model;
////
////import jakarta.persistence.Entity;
////import lombok.AllArgsConstructor;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////
////@Data
////@AllArgsConstructor
////@NoArgsConstructor
////@Entity
////public class UserAccount {
////
////    private Long id;
////    private String username;
////    private String passwordCrypt;
////    private String iv;
////    private String salt;
////    private Long idUser;
////}
////
/////**
//// * {
//// * zero-knowledge”.
//// *   "id": "uuid-1234",
//// *   "domain": "facebook.com",
//// *   "username": "ana@gmail.com",
//// *   "ciphertext": "A9F8B2...",
//// *   "iv": "7Gq1x...",
//// *   "salt": "xkP3a..."
//// * }**/
//import { useEffect, useState } from "react";
//import { useNavigate } from "react-router-dom";
//
//function Home() {
//  const [data, setData] = useState(null);
//  const navigate = useNavigate();
//
//    useEffect(() => {
//    const fetchData = async () => {
//      const token = localStorage.getItem("accessToken");
//    if (!token) {
//        navigate("/login"); // dacă nu există token
//        return;
//    }
//
//    try {
//        const res = await fetch("http://localhost:8080/user/meta", {
//                headers: { "Authorization": `Bearer ${token}` }
//        });
//
//        if (res.status === 401) {
//            // token invalid/expirat
//            localStorage.removeItem("accessToken");
//            navigate("/login");
//            return;
//        }
//
//        if (res.ok) {
//          const json = await res.json();
//            setData(json);
//        }
//    } catch (e) {
//        navigate("/login");
//    }
//    };
//
//    fetchData();
//  }, [navigate]);
//
//    if (!data) return <p>Loading...</p>;
//
//    return (
//            <div>
//            <h1>Bine ai venit, {data.username}</h1>
//            </div>
//  );
//}
//
//export default Home;
