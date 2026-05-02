import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Send, ArrowLeft } from 'lucide-react';
import toast from 'react-hot-toast';
import api from '../services/api';

export default function NovaDenuncia() {
  const navigate = useNavigate();
  const [titulo, setTitulo] = useState('');
  const [texto, setTexto] = useState('');
  const [urgencia, setUrgencia] = useState(1);
  const [orgaos, setOrgaos] = useState([]);
  const [tipos, setTipos] = useState([]);
  const [orgaoId, setOrgaoId] = useState('');
  const [tipoId, setTipoId] = useState('');
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(true);

  const loadSelects = useCallback(async () => {
    try {
      const [orgRes, tipoRes] = await Promise.all([
        api.get('/apis/orgaos'),
        api.get('/apis/tipo'),
      ]);
      setOrgaos(orgRes.data);
      setTipos(tipoRes.data);
    } catch {
      toast.error('Erro ao carregar dados. Tente novamente.');
    } finally {
      setFetching(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadSelects();
  }, [loadSelects]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!orgaoId || !tipoId) {
      toast.error('Selecione o orgão competente e o tipo de problema.');
      return;
    }

    const user = JSON.parse(localStorage.getItem('user') || '{}');

    setLoading(true);
    try {
      await api.post('/apis/denuncia', {
        titulo,
        texto,
        urgencia: parseInt(urgencia),
        orgao: { org_id: parseInt(orgaoId) },
        tipo: { id: parseInt(tipoId) },
        usuario: { id: user.id },
        data: new Date().toISOString().split('T')[0],
      });
      toast.success('Denúncia registrada com sucesso!');
      navigate('/cidadao');
    } catch (error) {
      if (error.response?.data?.mensagem) {
        toast.error(error.response.data.mensagem);
      } else {
        toast.error('Erro ao registrar denúncia. Tente novamente.');
      }
    } finally {
      setLoading(false);
    }
  };

  if (fetching) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando formulário...</div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center gap-4">
        <button
          onClick={() => navigate('/cidadao')}
          className="flex items-center gap-1 text-gray-500 hover:text-gray-700 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          Voltar
        </button>
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Nova Denúncia</h1>
          <p className="text-gray-500 mt-1">Descreva o problema encontrado na sua comunidade</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 space-y-5">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Título</label>
          <input
            type="text"
            value={titulo}
            onChange={(e) => setTitulo(e.target.value)}
            className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors"
            placeholder="Ex: Semáforo quebrado"
            maxLength={40}
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Descrição</label>
          <textarea
            value={texto}
            onChange={(e) => setTexto(e.target.value)}
            className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors resize-none"
            rows={4}
            placeholder="Descreva o problema com detalhes..."
            required
          />
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Órgão Competente</label>
            <select
              value={orgaoId}
              onChange={(e) => setOrgaoId(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors bg-white"
              required
            >
              <option value="">Selecione...</option>
              {orgaos.map((o) => (
                <option key={o.org_id} value={o.org_id}>{o.org_nome}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Tipo de Problema</label>
            <select
              value={tipoId}
              onChange={(e) => setTipoId(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors bg-white"
              required
            >
              <option value="">Selecione...</option>
              {tipos.map((t) => (
                <option key={t.id} value={t.id}>{t.nome}</option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-3">Nível de Urgência</label>
          <div className="flex gap-3">
            {[
              { val: 1, label: 'Baixa', color: 'bg-green-500' },
              { val: 2, label: 'Média', color: 'bg-yellow-500' },
              { val: 3, label: 'Alta', color: 'bg-orange-500' },
              { val: 4, label: 'Urgente', color: 'bg-red-500' },
            ].map((opt) => (
              <button
                key={opt.val}
                type="button"
                onClick={() => setUrgencia(opt.val)}
                className={`flex-1 py-2 px-3 rounded-lg text-sm font-medium border-2 transition-all ${
                  parseInt(urgencia) === opt.val
                    ? `border-${opt.color.split('-')[1]}-500 ${opt.color} text-white`
                    : 'border-gray-200 text-gray-600 hover:border-gray-300'
                }`}
                style={
                  parseInt(urgencia) === opt.val
                    ? {
                        backgroundColor:
                          opt.val === 1 ? '#22c55e' : opt.val === 2 ? '#eab308' : opt.val === 3 ? '#f97316' : '#ef4444',
                        borderColor:
                          opt.val === 1 ? '#22c55e' : opt.val === 2 ? '#eab308' : opt.val === 3 ? '#f97316' : '#ef4444',
                        color: '#fff',
                      }
                    : {}
                }
              >
                {opt.label}
              </button>
            ))}
          </div>
        </div>

        <div className="flex gap-3 pt-4">
          <button
            type="button"
            onClick={() => navigate('/cidadao')}
            className="flex-1 px-4 py-2.5 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors text-sm font-medium"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={loading}
            className="flex-1 flex items-center justify-center gap-2 bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors text-sm font-medium disabled:opacity-70"
          >
            <Send className="w-4 h-4" />
            {loading ? 'Enviando...' : 'Enviar Denúncia'}
          </button>
        </div>
      </form>
    </div>
  );
}
