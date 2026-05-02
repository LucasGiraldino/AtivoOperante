import { useState, useEffect, useCallback } from 'react';
import { Plus, Pencil, Trash2, X } from 'lucide-react';
import toast from 'react-hot-toast';
import api from '../services/api';

export default function GerenciarOrgaos() {
  const [orgaos, setOrgaos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [nome, setNome] = useState('');
  const [editId, setEditId] = useState(null);
  const [saving, setSaving] = useState(false);

  const loadOrgaos = useCallback(async () => {
    try {
      const response = await api.get('/apis/orgaos');
      setOrgaos(Array.isArray(response.data) ? response.data : []);
    } catch {
      setOrgaos([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    void loadOrgaos();
  }, [loadOrgaos]);

  const openCreate = () => {
    setModal('create');
    setEditId(null);
    setNome('');
  };

  const openEdit = (orgao) => {
    setModal('edit');
    setEditId(orgao.org_id);
    setNome(orgao.org_nome);
  };

  const closeModal = () => {
    setModal(null);
    setEditId(null);
    setNome('');
  };

  const handleSave = async () => {
    if (!nome.trim()) {
      toast.error('O nome do órgão é obrigatório.');
      return;
    }
    setSaving(true);
    try {
      if (editId) {
        await api.put('/apis/orgaos', { org_id: editId, org_nome: nome });
        toast.success('Órgão atualizado!');
      } else {
        await api.post('/apis/orgaos', { org_nome: nome });
        toast.success('Órgão criado!');
      }
      closeModal();
      loadOrgaos();
    } catch {
      toast.error('Erro ao salvar órgão.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Tem certeza que deseja excluir este órgão?')) return;
    try {
      await api.delete(`/apis/orgaos/${id}`);
      toast.success('Órgão excluído!');
      loadOrgaos();
    } catch {
      toast.error('Erro ao excluir órgão.');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Órgãos Competentes</h1>
          <p className="text-gray-500 mt-1">{orgaos.length} órgão(s) cadastrado(s)</p>
        </div>
        <button
          onClick={openCreate}
          className="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors shadow-sm"
        >
          <Plus className="w-4 h-4" />
          Novo Órgão
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase">ID</th>
              <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase">Nome</th>
              <th className="text-right px-6 py-3 text-xs font-semibold text-gray-500 uppercase">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y">
            {orgaos.map((o) => (
              <tr key={o.org_id} className="hover:bg-gray-50">
                <td className="px-6 py-4 text-sm text-gray-500">{o.org_id}</td>
                <td className="px-6 py-4 text-sm font-medium text-gray-800">{o.org_nome}</td>
                <td className="px-6 py-4 text-right">
                  <div className="flex items-center justify-end gap-2">
                    <button
                      onClick={() => openEdit(o)}
                      className="p-1.5 rounded-lg text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 transition-colors"
                    >
                      <Pencil className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleDelete(o.org_id)}
                      className="p-1.5 rounded-lg text-gray-400 hover:text-red-600 hover:bg-red-50 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
            {orgaos.length === 0 && (
              <tr>
                <td colSpan={3} className="px-6 py-12 text-center text-gray-400">
                  Nenhum órgão cadastrado
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden">
            <div className="flex items-center justify-between p-4 border-b">
              <h3 className="text-lg font-semibold text-gray-800">
                {modal === 'create' ? 'Novo Órgão' : 'Editar Órgão'}
              </h3>
              <button onClick={closeModal} className="text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="p-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">Nome do Órgão</label>
              <input
                type="text"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 text-sm outline-none transition-colors"
                placeholder="Ex: SEDUC, SETRAN..."
                maxLength={30}
                onKeyDown={(e) => e.key === 'Enter' && handleSave()}
              />
            </div>
            <div className="flex gap-3 p-4 border-t bg-gray-50">
              <button
                onClick={closeModal}
                className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors text-sm font-medium"
              >
                Cancelar
              </button>
              <button
                onClick={handleSave}
                disabled={saving}
                className="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors text-sm font-medium disabled:opacity-70"
              >
                {saving ? 'Salvando...' : 'Salvar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
