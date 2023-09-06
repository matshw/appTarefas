package br.mateus.appTarefas.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.mateus.appTarefas.R;

public class ListaTarefaAdapter extends BaseAdapter {
    private Context context;
    private List<Tarefa> ListaTarefa;

    public ListaTarefaAdapter(Context context, List<Tarefa> listaTarefa) {
        this.context = context;
        ListaTarefa = listaTarefa;
    }

    @Override
    public int getCount() {
        return ListaTarefa.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaTarefa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.linha,null);
        TextView textTitulo = (TextView)v.findViewById(R.id.textTitulo);
        TextView textDescricao = (TextView)v.findViewById(R.id.textDescricao);

        textTitulo.setText(ListaTarefa.get(position).getTitulo());
        textDescricao.setText(ListaTarefa.get(position).getDescricao());

        v.setTag(ListaTarefa.get(position).getId());
        return v;
    }
}
