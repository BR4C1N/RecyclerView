package com.software.agenda;

import android.service.autofill.TextValueSanitizer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.software.entidades.Contato;

import org.w3c.dom.Text;

import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoHolder> {

    List<Contato> lista;

    public ContatoAdapter(List<Contato> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ContatoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.linha, viewGroup, false);

        return new ContatoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoHolder contatoHolder, int i) {
        contatoHolder.nomeContato.setText(lista.get(i).getNome());
        contatoHolder.telefoneContato.setText(lista.get(i).getTelefone());
    }

    @Override
    public int getItemCount() {
        return (lista.size());
    }

    public class ContatoHolder extends RecyclerView.ViewHolder {

        public TextView nomeContato;
        public TextView telefoneContato;
        public Button botaoEditar;
        public Button botaoRemover;

        public ContatoHolder(@NonNull View view) {
            super(view);

            nomeContato = view.findViewById(R.id.textViewNomeContato);
            telefoneContato = view.findViewById(R.id.textViewTelefoneContato);
            botaoEditar = view.findViewById(R.id.buttonEditar);
            botaoRemover = view.findViewById(R.id.buttonRemover);
        }
    }
}
