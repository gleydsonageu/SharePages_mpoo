package br.com.projetoapp.sharepages.infra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.projetoapp.sharepages.R;
import br.com.projetoapp.sharepages.dominio.Disponibilidade;
import br.com.projetoapp.sharepages.dominio.Livro;
import br.com.projetoapp.sharepages.dominio.Tema;
import br.com.projetoapp.sharepages.dominio.UnidadeLivro;

public class AdapterListLivroPorTema extends BaseAdapter {

    private LayoutInflater inflater;
    private List<UnidadeLivro> itens;

    public AdapterListLivroPorTema(Context context, List<UnidadeLivro> itens){
        this.itens = itens;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public UnidadeLivro getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        UnidadeLivro item = itens.get(position);

        Livro livro = item.getLivro();
        Tema tema = item.getLivro().getTema();
        Disponibilidade disponibilidade = item.getDisponibilidade();


        view = inflater.inflate(R.layout.item_lista_livro_tema, null);

        ((TextView) view.findViewById(R.id.livroT)).setText(livro.getNome());
        ((TextView) view.findViewById(R.id.disponibilidadeT)).setText(disponibilidade.getNome());
        ((TextView) view.findViewById(R.id.temaT)).setText(tema.getNome());

        return view;
    }
}
