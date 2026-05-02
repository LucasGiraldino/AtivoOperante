import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import { LogOut, Home, AlertCircle, Settings, FileText } from 'lucide-react';

export default function Layout({ role }) {
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/');
  };

  const isActive = (path) => {
    return location.pathname === path ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700 hover:bg-gray-100';
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
        <div className="h-16 flex items-center px-6 border-b border-gray-200">
          <AlertCircle className="w-6 h-6 text-indigo-600 mr-2" />
          <span className="text-xl font-bold text-gray-900">Ativo Operante</span>
        </div>
        
        <nav className="flex-1 px-4 py-6 space-y-2">
          {role === 'admin' ? (
            <>
              <Link to="/admin" className={`flex items-center px-3 py-2.5 rounded-lg transition-colors ${isActive('/admin')}`}>
                <Home className="w-5 h-5 mr-3" /> Dashboard
              </Link>
              <Link to="/admin/orgaos" className={`flex items-center px-3 py-2.5 rounded-lg transition-colors ${isActive('/admin/orgaos')}`}>
                <Settings className="w-5 h-5 mr-3" /> Órgãos Competentes
              </Link>
              <Link to="/admin/tipos-problema" className={`flex items-center px-3 py-2.5 rounded-lg transition-colors ${isActive('/admin/tipos-problema')}`}>
                <AlertCircle className="w-5 h-5 mr-3" /> Tipos de Problema
              </Link>
            </>
          ) : (
            <>
              <Link to="/cidadao" className={`flex items-center px-3 py-2.5 rounded-lg transition-colors ${isActive('/cidadao')}`}>
                <Home className="w-5 h-5 mr-3" /> Minhas Denúncias
              </Link>
              <Link to="/cidadao/nova-denuncia" className={`flex items-center px-3 py-2.5 rounded-lg transition-colors ${isActive('/cidadao/nova-denuncia')}`}>
                <FileText className="w-5 h-5 mr-3" /> Nova Denúncia
              </Link>
            </>
          )}
        </nav>
        
        <div className="p-4 border-t border-gray-200">
          <button 
            onClick={handleLogout}
            className="flex items-center w-full px-3 py-2.5 text-gray-700 rounded-lg hover:bg-red-50 hover:text-red-600 transition-colors"
          >
            <LogOut className="w-5 h-5 mr-3" /> Sair
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col overflow-hidden">
        <header className="h-16 bg-white border-b border-gray-200 flex items-center px-8 justify-between shadow-sm z-10">
          <h2 className="text-lg font-semibold text-gray-800">
            {role === 'admin' ? 'Área Administrativa' : 'Área do Cidadão'}
          </h2>
          <div className="flex items-center gap-3">
             <span className="text-sm text-gray-600">{user.email}</span>
             <div className="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-700 font-bold">
               {role === 'admin' ? 'A' : 'C'}
             </div>
          </div>
        </header>
        <div className="flex-1 overflow-auto p-8 bg-gray-50">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
