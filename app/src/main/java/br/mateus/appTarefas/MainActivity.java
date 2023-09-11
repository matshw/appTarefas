package br.mateus.appTarefas;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.mateus.appTarefas.model.ListaTarefaAdapter;
import br.mateus.appTarefas.model.Tarefa;
import br.mateus.appTarefas.persistance.TarefaBD;
import br.mateus.appTarefas.persistance.TarefaDAO;

public class MainActivity extends AppCompatActivity {
    private EditText titulo;
    private EditText descricao;
    private ImageButton botaoSalvar;
    private ListView listar;
    private List<Tarefa> item;
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
        arrayTarefa = new ListaTarefaAdapter(getApplicationContext(), item);
        listar.setAdapter(arrayTarefa);

        check();
    }

    private void mapearXML() {
        titulo = findViewById(R.id.idTitulo);
        descricao = findViewById(R.id.idDescricao);
        botaoSalvar = findViewById(R.id.idSalvar);
        listar = findViewById(R.id.idLista);
    }

    private void verificar() {
        if (dao == null) {
            dao = new TarefaBD(this);
        }
        item = dao.listar();
    }

    private void check() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("checkBoxState", false);
        View checkLayout = getLayoutInflater().inflate(R.layout.linha, null);
        CheckBox check = checkLayout.findViewById(R.id.idCheckBox);
        check.setChecked(isChecked);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                editor.putBoolean("checkBoxState", isChecked);
                editor.apply();
            }
        });
    }

    private void click() {
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloTarefa = titulo.getText().toString().trim();
                String descricaoTarefa = descricao.getText().toString().trim();
                if (tituloTarefa.isEmpty()) {
                    titulo.setError("Este campo não pode estar vazio.");
                } else if (descricaoTarefa.isEmpty()) {
                    descricao.setError("Este campo não pode estar vazio.");
                } else {
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

        listar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Tarefa tarefaSelecionada = item.get(position);

                LinearLayout itemLayout = view.findViewById(R.id.idItem);
                itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostrarDialogoEditarExcluir(tarefaSelecionada);
                    }
                });
            }
        });


    }

    private void mostrarDialogoEditarExcluir(final Tarefa tarefa) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Opções");
        dialogBuilder.setMessage("Escolha uma opção para a tarefa:");

        dialogBuilder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abrirDialogoEditar(tarefa);
            }
        });

        dialogBuilder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mostrarDialogoConfirmacaoExcluir(tarefa);
            }
        });

        dialogBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        dialogBuilder.create().show();
    }

    private void abrirDialogoEditar(final Tarefa tarefa) {
        AlertDialog.Builder editarDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        editarDialogBuilder.setTitle("Editar Tarefa");

        View editarView = getLayoutInflater().inflate(R.layout.dialog_editar, null);
        editarDialogBuilder.setView(editarView);

        final EditText editarTitulo = editarView.findViewById(R.id.idTitulo);
        final EditText editarDescricao = editarView.findViewById(R.id.idDescricao);

        editarTitulo.setText(tarefa.getTitulo());
        editarDescricao.setText(tarefa.getDescricao());

        editarDialogBuilder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String novoTitulo = editarTitulo.getText().toString();
                String novaDescricao = editarDescricao.getText().toString();

                tarefa.setTitulo(novoTitulo);
                tarefa.setDescricao(novaDescricao);

                dao.editar(tarefa);

                atualizarItens();
            }
        });

        editarDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        editarDialogBuilder.create().show();
    }

    private void mostrarDialogoConfirmacaoExcluir(final Tarefa tarefa) {
        AlertDialog.Builder confirmarExclusaoDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        confirmarExclusaoDialogBuilder.setTitle("Confirmar Exclusão");
        confirmarExclusaoDialogBuilder.setMessage("Tem certeza de que deseja excluir esta tarefa?");

        confirmarExclusaoDialogBuilder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.remove(tarefa);

                atualizarItens();
            }
        });

        confirmarExclusaoDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        confirmarExclusaoDialogBuilder.create().show();
    }

    private void atualizarItens() {
        item.clear();
        item.addAll(dao.listar());
        arrayTarefa.notifyDataSetChanged();
    }

    private void limparCampos() {
        titulo.setText(" ");
        descricao.setText(" ");
        t = null;
    }

    public void cancelar(View view) {
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