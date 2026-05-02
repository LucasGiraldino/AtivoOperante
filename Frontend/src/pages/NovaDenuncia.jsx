import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Send, ArrowLeft, Upload, X } from 'lucide-react';
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
  const [foto, setFoto] = useState(null);
  const [fotoPreview, setFotoPreview] = useState(null);
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

  const handleFotoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        toast.error('A imagem deve ter no máximo 5MB.');
        return;
      }
      setFoto(file);
      setFotoPreview(URL.createObjectURL(file));
    }
  };

  const removeFoto = () => {
    setFoto(null);
    if (fotoPreview) {
      URL.revokeObjectURL(fotoPreview);
      setFotoPreview(null);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!orgaoId || !tipoId) {
      toast.error('Selecione o orgão competente e o tipo de problema.');
      return;
    }

    const user = JSON.parse(localStorage.getItem('user') || '{}');

    setLoading(true);
    try {
      const response = await api.post('/apis/denuncia', {
        titulo,
        texto,
        urgencia: parseInt(urgencia),
        orgao: { org_id: parseInt(orgaoId) },
        tipo: { id: parseInt(tipoId) },
        usuario: { id: user.id },
        data: new Date().toISOString().split('T')[0],
      });

      if (foto && response.data && response.data.id) {
        const formData = new FormData();
        formData.append('foto', foto);
        await api.post(`/apis/denuncia/${response.data.id}/foto`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
      }

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
      <button
        onClick={() => navigate('/cidadao')}
        className="flex items-center gap-1 text-gray-500 hover:text-gray-700 transition-colors"
      >
        <ArrowLeft className="w-4 h-4" />
        Voltar
      </button>
      <div className="flex items-center gap-4">
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
              { val: 1, label: 'Baixa', color: '#22c55e' },
              { val: 2, label: 'Média', color: '#eab308' },
              { val: 3, label: 'Alta', color: '#f97316' },
              { val: 4, label: 'Urgente', color: '#ef4444' },
              { val: 5, label: 'Muito Urgente', color: '#dc2626' },
            ].map((opt) => (
              <button
                key={opt.val}
                type="button"
                onClick={() => setUrgencia(opt.val)}
                className="flex-1 py-2 px-2 rounded-lg text-xs sm:text-sm font-medium border-2 transition-all"
                style={
                  parseInt(urgencia) === opt.val
                    ? { backgroundColor: opt.color, borderColor: opt.color, color: '#fff' }
                    : { borderColor: '#e5e7eb', color: '#6b7280' }
                }
              >
                {opt.label}
              </button>
            ))}
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Foto (opcional)</label>
          {!fotoPreview ? (
            <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50 transition-colors">
              <div className="flex flex-col items-center justify-center py-4">
                <Upload className="w-8 h-8 text-gray-400 mb-2" />
                <p className="text-sm text-gray-500">Clique para selecionar uma imagem</p>
                <p className="text-xs text-gray-400 mt-1">PNG, JPG até 5MB</p>
              </div>
              <input
                type="file"
                accept="image/*"
                onChange={handleFotoChange}
                className="hidden"
              />
            </label>
          ) : (
            <div className="relative w-full h-48 rounded-lg overflow-hidden border border-gray-200">
              <img src={fotoPreview} alt="Preview" className="w-full h-full object-cover" />
              <button
                type="button"
                onClick={removeFoto}
                className="absolute top-2 right-2 p-1.5 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors"
              >
                <X className="w-4 h-4" />
              </button>
            </div>
          )}
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
