package br.mateus.appTarefas.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.mateus.appTarefas.model.Tarefa;

public class TarefaBD implements TarefaDAO{
    BancoDados bd;

    public TarefaBD(Context context) {
        this.bd = new BancoDados(context);
    }


    @Override
    public void salvar(Tarefa t) {
        ContentValues item = new ContentValues();
        item.put("titulo",t.getTitulo());
        item.put("descricao",t.getDescricao());
        bd.getWritableDatabase().insertOrThrow("tarefa",null,item);
        bd.close();
    }

    @Override
    public void editar(Tarefa t) {
        ContentValues item = new ContentValues();
        item.put("titulo",t.getTitulo());
        item.put("descricao",t.getDescricao());
        bd.getWritableDatabase().update("tarefa",item,"id=?",new String[]{t.getId()+""});
        bd.close();
    }

    @Override
    public void remover(Tarefa t) {
        bd.getWritableDatabase().delete("tarefa","id=?",new String[]{t.getId()+""});
        bd.close();
    }



    @Override
    public List listar() {
        List itens = new ArrayList();
        String colunas[]={"id","titulo","descricao"};
        Cursor cursor= bd.getReadableDatabase().query("tarefa",colunas,null,null,null,null,"titulo");

        final int COLUMN_ID=0, COLUMN_TITULO=1, COLUMN_DESCRICAO=2;
        while(cursor.moveToNext()){
            Tarefa t = new Tarefa();
            t.setId(cursor.getInt(COLUMN_ID));
            t.setDescricao(cursor.getString(COLUMN_DESCRICAO));
            t.setTitulo(cursor.getString(COLUMN_TITULO));
            itens.add(t);
        }
        bd.close();
        return itens;
    }
}
