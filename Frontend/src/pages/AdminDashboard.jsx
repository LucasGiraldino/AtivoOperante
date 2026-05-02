import { useState, useEffect, useCallback } from 'react';
import { Trash2, MessageSquare, X, Send, AlertTriangle, Clock } from 'lucide-react';
import toast from 'react-hot-toast';
import api from '../services/api';

export default function AdminDashboard() {
  const [denuncias, setDenuncias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [feedbackModal, setFeedbackModal] = useState(null);
  const [feedbackTexto, setFeedbackTexto] = useState('');
  const [sending, setSending] = useState(false);

  const loadDenuncias = useCallback(async () => {
    try {
      const response = await api.get('/apis/denuncia/');
      setDenuncias(Array.isArray(response.data) ? response.data : []);
    } catch {
      setDenuncias([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    void loadDenuncias();
  }, [loadDenuncias]);

  const handleDelete = async (id) => {
    if (!confirm('Tem certeza que deseja excluir esta denúncia?')) return;
    try {
      await api.delete(`/apis/denuncia/${id}`);
      toast.success('Denúncia excluída com sucesso!');
      loadDenuncias();
    } catch {
      toast.error('Erro ao excluir denúncia.');
    }
  };

  const openFeedback = (denuncia) => {
    setFeedbackModal(denuncia);
    setFeedbackTexto(denuncia.feedBack?.fee_texto || '');
  };

  const closeFeedback = () => {
    setFeedbackModal(null);
    setFeedbackTexto('');
  };

  const submitFeedback = async () => {
    if (!feedbackTexto.trim()) {
      toast.error('O feedback não pode estar vazio.');
      return;
    }
    setSending(true);
    try {
      if (feedbackModal.feedBack) {
        await api.put('/apis/feedback', {
          fee_id: feedbackModal.feedBack.fee_id,
          fee_texto: feedbackTexto,
          denuncia: { id: feedbackModal.id },
        });
        toast.success('Feedback atualizado!');
      } else {
        await api.post('/apis/feedback', {
          fee_texto: feedbackTexto,
          denuncia: { id: feedbackModal.id },
        });
        toast.success('Feedback registrado!');
      }
      closeFeedback();
      loadDenuncias();
    } catch {
      toast.error('Erro ao registrar feedback.');
    } finally {
      setSending(false);
    }
  };

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
      <div>
        <h1 className="text-2xl font-bold text-gray-800">Todas as Denúncias</h1>
        <p className="text-gray-500 mt-1">{denuncias.length} denúncia(s) registrada(s)</p>
      </div>

      {denuncias.length === 0 ? (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
          <AlertTriangle className="w-12 h-12 text-gray-300 mx-auto mb-4" />
          <h3 className="text-lg font-semibold text-gray-700 mb-2">Nenhuma denúncia encontrada</h3>
          <p className="text-gray-500">As denúncias registradas pelos cidadãos aparecerão aqui.</p>
        </div>
      ) : (
        <div className="grid gap-4">
          {denuncias.map((den) => {
            const urg = urgenciaLabel(den.urgencia);
            const hasFeedback = !!den.feedBack;
            return (
              <div
                key={den.id}
                className={`bg-white rounded-xl shadow-sm border p-6 transition-shadow ${
                  hasFeedback ? 'border-green-200' : 'border-gray-200'
                } hover:shadow-md`}
              >
                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <h3 className="text-lg font-semibold text-gray-800">{den.titulo}</h3>
                      {hasFeedback && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-700">
                          Com feedback
                        </span>
                      )}
                    </div>
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

                <div className="flex items-center gap-2 pt-3 border-t border-gray-100">
                  <button
                    onClick={() => openFeedback(den)}
                    className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
                      hasFeedback
                        ? 'bg-green-50 text-green-700 hover:bg-green-100'
                        : 'bg-indigo-50 text-indigo-700 hover:bg-indigo-100'
                    }`}
                  >
                    <MessageSquare className="w-4 h-4" />
                    {hasFeedback ? 'Editar Feedback' : 'Dar Feedback'}
                  </button>
                  <button
                    onClick={() => handleDelete(den.id)}
                    className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-sm font-medium bg-red-50 text-red-600 hover:bg-red-100 transition-colors"
                  >
                    <Trash2 className="w-4 h-4" />
                    Excluir
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {feedbackModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden">
            <div className="flex items-center justify-between p-4 border-b">
              <h3 className="text-lg font-semibold text-gray-800">
                {feedbackModal.feedBack ? 'Editar Feedback' : 'Registrar Feedback'}
              </h3>
              <button onClick={closeFeedback} className="text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="p-4">
              <p className="text-sm text-gray-500 mb-3">
                Denúncia: <span className="font-medium text-gray-700">{feedbackModal.titulo}</span>
              </p>
              <textarea
                value={feedbackTexto}
                onChange={(e) => setFeedbackTexto(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors resize-none"
                rows={4}
                placeholder="Escreva o feedback para esta denúncia..."
              />
            </div>
            <div className="flex gap-3 p-4 border-t bg-gray-50">
              <button
                onClick={closeFeedback}
                className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors text-sm font-medium"
              >
                Cancelar
              </button>
              <button
                onClick={submitFeedback}
                disabled={sending}
                className="flex-1 flex items-center justify-center gap-2 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors text-sm font-medium disabled:opacity-70"
              >
                <Send className="w-4 h-4" />
                {sending ? 'Enviando...' : feedbackModal.feedBack ? 'Atualizar' : 'Enviar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
