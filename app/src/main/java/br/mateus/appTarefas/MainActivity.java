package br.mateus.appTarefas;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.mateus.appTarefas.model.ListaTarefaAdapter;
import br.mateus.appTarefas.model.Tarefa;
import br.mateus.appTarefas.persistance.TarefaBD;
import br.mateus.appTarefas.persistance.TarefaDAO;

public class MainActivity extends AppCompatActivity{

    private EditText titulo;
    private EditText descricao;
    private ImageButton botaoSalvar;
    private ImageButton botaoCancelar;
    private ListView listar;
    private List<Tarefa>item;
    private ListaTarefaAdapter arrayTarefa;
    private TarefaDAO dao;
    private Tarefa t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_App);
        setContentView(R.layout.activity_main);
        mapearXML();
        verificar();
        click();
        View checkboxLayout = getLayoutInflater().inflate(R.layout.linha,null);
        CheckBox checkBox = checkboxLayout.findViewById(R.id.idCheckBox);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean checkBoxState = preferences.getBoolean("checkBoxState", true);
        checkBox.setChecked(checkBoxState);
        arrayTarefa = new ListaTarefaAdapter(getApplicationContext(),item);
        listar.setAdapter(arrayTarefa);
    }

    private void mapearXML(){
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

    private void click(){


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloTarefa = titulo.getText().toString().trim();
                String descricaoTarefa = descricao.getText().toString().trim();
                if (tituloTarefa.isEmpty() ) {
                    titulo.setError("Este campo não pode estar vazio.");
                }else if(descricaoTarefa.isEmpty()){
                    descricao.setError("Este campo não pode estar vazio.");
                }else{
                    if (t == null) {
                        t = new Tarefa();
                    }
                    t.setTitulo(tituloTarefa);
                    t.setDescricao(descricaoTarefa);
                    if (t.getId() == null) {
                        dao.salvar(t);
                    } else {
                        dao.editar(t);
                    }
                    limparCampos();
                    atualizarItens();
                }
            }
            });

    };


    private void atualizarItens(){
        item.clear();
        item.addAll(dao.listar());
        arrayTarefa.notifyDataSetChanged();
    }

    private void limparCampos(){
        titulo.setText(" ");
        descricao.setText(" ");
        t=null;
    }

    public void cancelar(View view){
        AlertDialog.Builder cancela = new AlertDialog.Builder(this);
        cancela.setTitle("Deseja mesmo sair?");
        cancela.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        cancela.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        cancela.create().show();
    }
}