
import { Routes, Route, Link, Navigate, Outlet } from "react-router-dom";
import Menu from "./pages/Menu";
import Auth from "./pages/Auth";

function App() {

  const token = localStorage.getItem("accessToken");
  
  const PrivateRoutes = () =>{
       return (token ? <Outlet/> : <Navigate to="/auth"/>);
  }

  return (
    <Routes>
      <Route element={<PrivateRoutes/>}>
              <Route path="/" element={<Navigate to="/menu" replace />} />
              <Route path="/menu/*" element={<Menu />} />
      </Route>
      
      <Route path="/auth/*" element={<Auth />} />
    </Routes>
  );
}

export default App;
