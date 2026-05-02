import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { Plus, AlertTriangle, Clock, CheckCircle, MessageSquare } from 'lucide-react';
import api from '../services/api';

export default function DashboardCidadao() {
  const [denuncias, setDenuncias] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadDenuncias = useCallback(async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      const response = await api.get(`/apis/denuncia/usuario/${user.id}`);
      setDenuncias(response.data);
    } catch {
      setDenuncias([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadDenuncias();
  }, [loadDenuncias]);

  const urgenciaLabel = (nivel) => {
    const map = {
      1: { text: 'Baixa', color: 'bg-green-100 text-green-700' },
      2: { text: 'Média', color: 'bg-yellow-100 text-yellow-700' },
      3: { text: 'Alta', color: 'bg-orange-100 text-orange-700' },
      4: { text: 'Urgente', color: 'bg-red-100 text-red-700' },
      5: { text: 'Muito Urgente', color: 'bg-red-200 text-red-800' },
    };
    return map[nivel] || { text: 'N/A', color: 'bg-gray-100 text-gray-700' };
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    const parts = dateStr.split('-');
    if (parts.length === 3) return `${parts[2]}/${parts[1]}/${parts[0]}`;
    return dateStr;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando denúncias...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Minhas Denúncias</h1>
          <p className="text-gray-500 mt-1">Acompanhe o status das suas denúncias</p>
        </div>
        <Link
          to="/cidadao/nova-denuncia"
          className="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors shadow-sm"
        >
          <Plus className="w-4 h-4" />
          Nova Denúncia
        </Link>
      </div>

      {denuncias.length === 0 ? (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
          <AlertTriangle className="w-12 h-12 text-gray-300 mx-auto mb-4" />
          <h3 className="text-lg font-semibold text-gray-700 mb-2">Nenhuma denúncia encontrada</h3>
          <p className="text-gray-500 mb-6">Você ainda não registrou nenhuma denúncia. Comece agora!</p>
          <Link
            to="/cidadao/nova-denuncia"
            className="inline-flex items-center gap-2 bg-indigo-600 text-white px-5 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors"
          >
            <Plus className="w-4 h-4" />
            Criar Primeira Denúncia
          </Link>
        </div>
      ) : (
        <div className="grid gap-4">
          {denuncias.map((den) => {
            const urg = urgenciaLabel(den.urgencia);
            return (
              <div
                key={den.id}
                className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
              >
                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold text-gray-800">{den.titulo}</h3>
                    <div className="flex items-center gap-3 mt-2 flex-wrap">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${urg.color}`}>
                        {urg.text}
                      </span>
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-700">
                        {den.tipo?.nome || '—'}
                      </span>
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700">
                        {den.orgao?.org_nome || '—'}
                      </span>
                      <span className="inline-flex items-center gap-1 text-gray-400 text-xs">
                        <Clock className="w-3 h-3" />
                        {formatDate(den.data)}
                      </span>
                    </div>
                  </div>
                </div>

                {den.foto && (
                  <div className="mb-4 rounded-lg overflow-hidden border border-gray-200">
                    <img
                      src={`http://localhost:8081${den.foto}`}
                      alt={den.titulo}
                      className="w-full h-48 object-cover"
                    />
                  </div>
                )}

                <p className="text-gray-600 text-sm mb-4">{den.texto}</p>

                {den.feedBack ? (
                  <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                    <div className="flex items-start gap-3">
                      <CheckCircle className="w-5 h-5 text-green-600 mt-0.5 shrink-0" />
                      <div>
                        <h4 className="text-sm font-semibold text-green-800 mb-1">Feedback recebido</h4>
                        <p className="text-sm text-green-700">{den.feedBack.fee_texto}</p>
                      </div>
                    </div>
                  </div>
                ) : (
                  <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
                    <div className="flex items-center gap-2 text-gray-400 text-sm">
                      <MessageSquare className="w-4 h-4" />
                      Aguardando feedback do órgão competente
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
