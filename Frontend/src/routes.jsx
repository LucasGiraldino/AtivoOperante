import { Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';

// Placeholder components for now
const CitizenDashboard = () => <div>Dashboard Cidadão</div>;
const NewReport = () => <div>Nova Denúncia</div>;
const AdminDashboard = () => <div>Dashboard Admin</div>;
const Organs = () => <div>Órgãos Competentes</div>;
const ProblemTypes = () => <div>Tipos de Problema</div>;

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/cadastro" element={<div>Cadastro</div>} />
      
      <Route path="/cidadao" element={<Layout role="cidadao" />}>
        <Route index element={<CitizenDashboard />} />
        <Route path="nova-denuncia" element={<NewReport />} />
      </Route>

      <Route path="/admin" element={<Layout role="admin" />}>
        <Route index element={<AdminDashboard />} />
        <Route path="orgaos" element={<Organs />} />
        <Route path="tipos-problema" element={<ProblemTypes />} />
      </Route>
    </Routes>
  );
}
