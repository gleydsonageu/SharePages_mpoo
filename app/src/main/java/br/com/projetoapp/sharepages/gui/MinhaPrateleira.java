package br.com.projetoapp.sharepages.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.projetoapp.sharepages.R;
import br.com.projetoapp.sharepages.dominio.UnidadeLivro;
import br.com.projetoapp.sharepages.infra.AdapterListLivro;
import br.com.projetoapp.sharepages.infra.SessaoUsuario;
import br.com.projetoapp.sharepages.infra.SharepagesException;
import br.com.projetoapp.sharepages.negocio.UnidadeLivroService;

public class MinhaPrateleira extends Activity {

    private ListView listLivro;
    private AdapterListLivro adapterListLivro;

    private ImageButton botaoEditarLivro;

    private UnidadeLivroService unidadeLivroService = UnidadeLivroService.getInstancia(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_prateleira);

        listLivro = (ListView) findViewById(R.id.listaLivros);

        try {
            listaLivrosDeUsuarioLogado();
        } catch (SharepagesException e) {
            Toast.makeText(getApplication(), "Erro ao listar livro. ", Toast.LENGTH_LONG).show();
        }

    }

    public void listaLivrosDeUsuarioLogado() throws SharepagesException {
        int id = SessaoUsuario.getInstancia().getUsuarioLogado().getId();

        AdapterListLivro adapterListView = null;

        try {
            List<UnidadeLivro> listaLivros = unidadeLivroService.buscarLivroPorUsuario(id);
            //Log.i("SCRIPT","buscando livro depois do livro service "+ listaLivros);
            adapterListView = new AdapterListLivro(MinhaPrateleira.this, listaLivros);
            //Log.i("SCRIPT","buscando livro "+ adapterListView);
            listLivro.setAdapter(adapterListView);
        }catch (RuntimeException e){
            e.printStackTrace();
            Toast.makeText(getApplication(), "Erro ao buscar", Toast.LENGTH_LONG).show();
        }
    }

}
