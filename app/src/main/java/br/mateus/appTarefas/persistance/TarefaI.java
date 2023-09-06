package br.mateus.appTarefas.persistance;

import java.util.ArrayList;
import java.util.List;

import br.mateus.appTarefas.model.Tarefa;

public class TarefaI implements TarefaDAO{
    private List itens;

    public TarefaI(){
        itens = new ArrayList();
    }

    public void salvar(Tarefa t) {
        itens.add(t);
    }

    public void editar(Tarefa t){
        if(itens.contains(t)){
            itens.add(itens.indexOf(t),t);
        }
    }
    @Override
    public void remove(Tarefa t){
        itens.remove(t);
    }
    public List listar(){
        return itens;
    }

}
