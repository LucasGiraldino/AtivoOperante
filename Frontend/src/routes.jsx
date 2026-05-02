import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';
import Cadastro from './pages/Cadastro';
import DashboardCidadao from './pages/DashboardCidadao';
import NovaDenuncia from './pages/NovaDenuncia';
import AdminDashboard from './pages/AdminDashboard';
import GerenciarOrgaos from './pages/GerenciarOrgaos';
import GerenciarTipos from './pages/GerenciarTipos';
import AuthGuard from './components/AuthGuard';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      
      <Route path="/cidadao" element={<AuthGuard requiredRole="cidadao"><Layout role="cidadao" /></AuthGuard>}>
        <Route index element={<DashboardCidadao />} />
        <Route path="nova-denuncia" element={<NovaDenuncia />} />
      </Route>

      <Route path="/admin" element={<AuthGuard requiredRole="admin"><Layout role="admin" /></AuthGuard>}>
        <Route index element={<AdminDashboard />} />
        <Route path="orgaos" element={<GerenciarOrgaos />} />
        <Route path="tipos-problema" element={<GerenciarTipos />} />
      </Route>
    </Routes>
  );
}
