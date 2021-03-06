package br.com.projetoapp.sharepages.persistencia;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.projetoapp.sharepages.dominio.Cidade;
import br.com.projetoapp.sharepages.infra.SessaoUsuario;
import br.com.projetoapp.sharepages.infra.SharepagesException;

/**
 * Esta classe realiza a consulta na Tabela Cidade no DatabaseHelper.
 */

public class CidadeDAO {

    public static CidadeDAO getInstancia() {
        CidadeDAO instancia = new CidadeDAO();
        return instancia;
    }

    /**
     * Usando uma query consulta a tabela cidades, carrega uma lista com as cidades que contem no banco.
     *
     * @return lista de cidades
     * @throws SharepagesException
     */
    public ArrayList<Cidade> getCidades() throws SharepagesException{
        Cidade cidade = null;

        SessaoUsuario sessaoUsuario = SessaoUsuario.getInstancia();
        DatabaseHelper databaseHelper = new DatabaseHelper(sessaoUsuario.getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        try {
            ArrayList<Cidade> listaCidades = new ArrayList<Cidade>();

                Cursor cursor = database.query(DatabaseHelper.TABLE_CIDADES, DatabaseHelper.CIDADE_COLUNAS, null, null, null, null, null);

                if(cursor.getCount()> 0){
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()){
                        cidade = objetoCidade(cursor);
                        listaCidades.add(cidade);

                        cursor.moveToNext();
                    }
                }
            database.close();
            return listaCidades;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param cursor cursor para carregar o objeto cidade
     * @return cidade
     */
    public Cidade objetoCidade(Cursor cursor){
        Cidade cidade = null;

        cidade = new Cidade();
        cidade.setId(cursor.getInt(0));
        cidade.setNome(cursor.getString(1));

        return cidade;
    }

}