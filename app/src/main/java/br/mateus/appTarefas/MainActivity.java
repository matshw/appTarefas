package br.mateus.appTarefas;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import br.mateus.appTarefas.model.ListaTarefaAdapter;
import br.mateus.appTarefas.model.Tarefa;
import br.mateus.appTarefas.persistance.TarefaBD;
import br.mateus.appTarefas.persistance.TarefaDAO;
import br.mateus.appTarefas.persistance.BancoDados;
import br.mateus.appTarefas.persistance.TarefaI;
public class MainActivity extends AppCompatActivity{

    private EditText titulo;
    private EditText descricao;
    private Button botaoSalvar;
    private Button botaoCancelar;
    private ListView listar;
    private List<Tarefa>item;
    private ListaTarefaAdapter arrayTarefa;
    private TarefaDAO dao;
    private Tarefa t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_App);
        mapearXML();
        verificar();
        listAdapter();
        click();

        arrayTarefa = new ListaTarefaAdapter(getApplicationContext(),item);
        listar.setAdapter(arrayTarefa);
    }

    private void mapearXML(){
        setContentView(R.layout.activity_main);
        titulo = findViewById(R.id.idTitulo);
        descricao = findViewById(R.id.idDescricao);
        botaoSalvar = findViewById(R.id.idSalvar);
        botaoCancelar = findViewById(R.id.idCancelar);
        listar = findViewById(R.id.idLista);
    }

    private void verificar(){
        if(dao==null){
            dao = new TarefaBD(this);
        }
        item=dao.listar();
    }

    private void listAdapter(){
        listar.setAdapter(
            new ArrayAdapter(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, item)
        );
    }

    private void click(){


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t==null){
                    t = new Tarefa();
                }
                t.setTitulo(titulo.getText().toString());
                t.setDescricao(descricao.getText().toString());
                if(t.getId()==null){
                    dao.salvar(t);
                }else{
                    dao.editar(t);
                }
                limparCampos();
                atualizarItens();
            }
        });

        listar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int indice, long l) {
                new AlertDialog.Builder(listar.getContext())
                        .setTitle("O que deseja fazer?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dao.remove(item.get(indice));
                                atualizarItens();
                            }
                        })
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                t=item.get(i);
                                titulo.setText(t.getTitulo());
                                descricao.setText(t.getDescricao());
                            }
                        })

                        .create().show();
                return false;
            }
        });
    }

    private void atualizarItens(){
        item.clear();
        item.addAll(dao.listar());
        ((ArrayAdapter) listar.getAdapter()).notifyDataSetChanged();
    }

    private void limparCampos(){
        titulo.setText(" ");
        descricao.setText(" ");
        t=null;
    }

    public void cancelar(View view){
        AlertDialog.Builder cancela = new AlertDialog.Builder(this);
        cancela.setTitle("Deseja sair?");
        cancela.setItems(new CharSequence[]{"Sair"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        cancela.create().show();
    }
}
