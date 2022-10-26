package com.software.agenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.bancoDados.ContatoDB;
import com.software.bancoDados.DBHelper;
import com.software.entidades.Contato;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText campoNome;
    EditText campoTelefone;
    Button botaoSalvar;
    Button botaoCancelar;

    List<Contato> dadosContatos;
    ListView listagemContatos;

    DBHelper dbHelper;
    ContatoDB contatoDB;
    ArrayAdapter adapter;

    Contato contato;
    Boolean verificarBotaoCancelar;
    Boolean verificarEditarContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        contatoDB = new ContatoDB(dbHelper);

        verificarBotaoCancelar = false;
        verificarEditarContato = false;

        campoNome = findViewById(R.id.campoNome);
        campoTelefone = findViewById(R.id.campoTelefone);
        botaoSalvar = findViewById(R.id.botaoSalvar);
        botaoCancelar = findViewById(R.id.botaoCancelar);
        listagemContatos = findViewById(R.id.listagemContatos);

        botaoCancelar.setBackgroundResource(R.color.desativado);

        dadosContatos = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.support.constraint.R.layout.support_simple_spinner_dropdown_item, dadosContatos);

        listagemContatos.setAdapter(adapter);
        contatoDB.listar(dadosContatos);
        acaoComponente();
    }

    private void acaoComponente() {
        //Acionar o botão cancelar quando começar a escrever no campo nome.
        campoNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                botaoCancelar.setBackgroundResource(R.color.ativo);
                verificarBotaoCancelar = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Acionar o botão cancelar quando começar a escrever no campo telefone.
        campoTelefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                botaoCancelar.setBackgroundResource(R.color.ativo);
                verificarBotaoCancelar = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        listagemContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Selecione uma Opção:")
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                botaoCancelar.setBackgroundResource(R.color.ativo);
                                verificarBotaoCancelar = true;
                                verificarEditarContato = true;

                                contato = new Contato();
                                contato.setId(dadosContatos.get(i).getId());

                                campoNome.setText(dadosContatos.get(i).getNome());
                                campoTelefone.setText(dadosContatos.get(i).getTelefone());
                            }
                        })
                        .setNegativeButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Deseja remover o contato?")
                                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int k) {
                                                contatoDB.remover(dadosContatos.get(i).getId());
                                                contatoDB.listar(dadosContatos);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create().show();
                            }
                        })
                        .create().show();

                return false;
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarBotaoCancelar) {
                    contato = new Contato();

                    campoNome.setText("");
                    campoTelefone.setText("");

                    verificarEditarContato = false;
                    verificarBotaoCancelar = false;
                    botaoCancelar.setBackgroundResource(R.color.desativado);
                }
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoNome.getText().toString().isEmpty() || campoTelefone.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Contato Inválido!", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificarEditarContato == false) {
                        contato = new Contato();
                    }

                    contato.setNome(campoNome.getText().toString());
                    contato.setTelefone(campoTelefone.getText().toString());

                    if (verificarEditarContato) {
                        contatoDB.editarContato(contato);
                    } else {
                        contatoDB.inserirContato(contato);
                    }

                    //Atualizar lista.
                    contatoDB.listar(dadosContatos);
                    adapter.notifyDataSetChanged();

                    //Resetar os campos.
                    contato = null;
                    campoNome.setText("");
                    campoTelefone.setText("");
                    verificarEditarContato = false;
                    verificarBotaoCancelar = false;
                    botaoCancelar.setBackgroundResource(R.color.desativado);

                    Toast.makeText(MainActivity.this, "Salvo com Sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}