package br.mateus.appTarefas.persistance;
import java.util.List;

import br.mateus.appTarefas.model.Tarefa;
public interface TarefaDAO {
    public void salvar (Tarefa t);
    public void editar(Tarefa t);
    public void remove(Tarefa t);
    public List listar();
}
