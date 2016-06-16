package br.com.projetoapp.sharepages.gui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.projetoapp.sharepages.R;
import br.com.projetoapp.sharepages.dominio.Disponibilidade;
import br.com.projetoapp.sharepages.dominio.Foto;
import br.com.projetoapp.sharepages.dominio.Livro;
import br.com.projetoapp.sharepages.dominio.Tema;
import br.com.projetoapp.sharepages.dominio.UnidadeLivro;
import br.com.projetoapp.sharepages.dominio.Usuario;
import br.com.projetoapp.sharepages.infra.ModeloArrayAdapter;
import br.com.projetoapp.sharepages.infra.SessaoUsuario;
import br.com.projetoapp.sharepages.infra.SharepagesException;
import br.com.projetoapp.sharepages.negocio.DisponibilidadeServices;
import br.com.projetoapp.sharepages.negocio.FotoServices;
import br.com.projetoapp.sharepages.negocio.LivroServices;
import br.com.projetoapp.sharepages.negocio.TemaServices;
import br.com.projetoapp.sharepages.negocio.UnidadeLivroService;

public class PerfilDeLivro extends Activity {

    private EditText campoNomeLivroPerfil, campoAutorPerfil, campoEditoraPerfil, campoDePaginasPerfil,
            campoEdicaoPerfil, campoDescricaoPerfil, campoIdiomaPerfil, editTema;
    private Button selecionarFoto, atualizarLivro, tirarFoto;
    private Spinner disponibilidadeSpinnerPerfil, temaSpinnerPerfil;
    private String caminhoFoto;
    private ImageView preVisuFoto;
    public static final int CODE_CAMERA_TIRAR = 123456;
    public static final int CODE_CAMERA_SELECIONAR = 123;
    public static final int CODE_EXTERNAL_STORAGE_PERMISSION = 3232;

    LivroServices livroServices = LivroServices.getInstancia(this);
    UnidadeLivroService unidadeLivroService = UnidadeLivroService.getInstancia(this);
    FotoServices fotoServices = FotoServices.getInstancia(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_de_livro);
        int idUnidadeLivro = getIntent().getIntExtra("UnidadeLivro", 0);

        UnidadeLivro unidadeLivro = unidadeLivroService.buscarUnidadeLivroPorId(idUnidadeLivro);



        campoNomeLivroPerfil = (EditText) findViewById(R.id.campoNomeLivroPerfil);
        campoAutorPerfil = (EditText) findViewById(R.id.campoAutorPerfil);
        campoEdicaoPerfil = (EditText) findViewById(R.id.campoEdicaoPerfil);
        campoEditoraPerfil = (EditText) findViewById(R.id.campoEditoraPerfil);
        campoDePaginasPerfil = (EditText) findViewById(R.id.campoNdePaginasPerfil);
        campoDescricaoPerfil = (EditText) findViewById(R.id.campoDescricaoPerfil);
        campoIdiomaPerfil = (EditText) findViewById(R.id.campoIdiomaPerfil);
        atualizarLivro = (Button) findViewById(R.id.atualizarLivro);
        selecionarFoto = (Button) findViewById(R.id.selecionarFoto);
        tirarFoto = (Button) findViewById(R.id.tirarFoto);
        preVisuFoto = (ImageView) findViewById(R.id.preVisuFoto);
        disponibilidadeSpinnerPerfil = (Spinner) findViewById(R.id.disponibilidadeSpinnerPerfil);
        editTema = (EditText) findViewById(R.id.editTema);


        campoNomeLivroPerfil.setText(unidadeLivro.getLivro().getNome());
        campoNomeLivroPerfil.setEnabled(false);
        campoAutorPerfil.setText(unidadeLivro.getLivro().getAutor());
        campoAutorPerfil.setEnabled(false);
        campoEdicaoPerfil.setText(unidadeLivro.getEdicao());
        campoEdicaoPerfil.setEnabled(false);
        campoEditoraPerfil.setText(unidadeLivro.getEditora());
        campoEditoraPerfil.setEnabled(false);
        campoDePaginasPerfil.setText("" + unidadeLivro.getNumeroPaginas());
        campoDePaginasPerfil.setEnabled(false);
        campoDescricaoPerfil.setText(unidadeLivro.getDescricao());
        campoIdiomaPerfil.setText(unidadeLivro.getIdioma());
        campoIdiomaPerfil.setEnabled(false);
        editTema.setText(unidadeLivro.getLivro().getTema().getNome());
        editTema.setEnabled(false);
        try {
            adcDisponibilidadesNoSpinner();
            selectDisponibilidadeSpinnerItemById(unidadeLivro.getIdDisponibilidade());
        } catch (SharepagesException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri visualizacao = Uri.fromFile(new File(unidadeLivro.getFoto().getCaminho()));
        preVisuFoto.setImageURI(visualizacao);



        chamarBotaoTirarFoto();
        chamarBotaoSelecionarFoto();
        chamarBotaoCadastrarLivro();


    }

    public void chamarBotaoCadastrarLivro() {

        atualizarLivro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nome = campoNomeLivroPerfil.getText().toString();
                String autor = campoAutorPerfil.getText().toString();
                String editora = campoEditoraPerfil.getText().toString();
                String edicao = campoEdicaoPerfil.getText().toString();
                String descricao = campoDescricaoPerfil.getText().toString();
                String idioma = campoIdiomaPerfil.getText().toString();
                Disponibilidade disponibilidade = (Disponibilidade) disponibilidadeSpinnerPerfil.getSelectedItem();
                Tema tema = (Tema) temaSpinnerPerfil.getSelectedItem();
                try {
                    int nDePaginas = Integer.parseInt(campoDePaginasPerfil.getText().toString());

                    Livro livro = new Livro(nome, autor, tema, tema.getId());
                    UnidadeLivro unidadeLivro = new UnidadeLivro(editora, nDePaginas, edicao, descricao, idioma, disponibilidade, disponibilidade.getId(), SessaoUsuario.getInstancia().getUsuarioLogado().getId());

                    Foto foto = new Foto(caminhoFoto);


                    List<String> listaCamposNaoPreenchidos = validarCamposPreenchidosLivro(livro, unidadeLivro, foto);
                    if (listaCamposNaoPreenchidos.size() >= 1) {
                        String msgError = TextUtils.join(", ", listaCamposNaoPreenchidos);
                        Toast.makeText(getApplication(), "Favor preencher todos os campos: " + msgError, Toast.LENGTH_LONG).show();
                    } else {
                        cadastrarLivro(livro, unidadeLivro, foto);
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplication(), "insira numeros de paginas", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    //validação de campos preenchidos
    public List<String> validarCamposPreenchidosLivro(Livro livro, UnidadeLivro unidadeLivro, Foto foto) {

        List<String> listaCampos = new ArrayList<String>();
        Log.i("SCRIPT", "Chamada do metodo validar campos vazios ");
        if (livro.getNome() == null || livro.getNome().equals("")) {
            listaCampos.add("nome");
            campoNomeLivroPerfil.setError(getString(R.string.campo_obrigatorio));
        }
        if (livro.getAutor() == null || livro.getAutor().equals("")) {
            listaCampos.add("autor");
            campoAutorPerfil.setError(getString(R.string.campo_obrigatorio));
        }
        if (unidadeLivro.getEdicao() == null || unidadeLivro.getEdicao().equals("")) {
            listaCampos.add("edicao");
            campoEdicaoPerfil.setError(getString(R.string.campo_obrigatorio));
        }
        if (unidadeLivro.getEditora() == null || unidadeLivro.getEditora().equals("")) {
            listaCampos.add("editora");
            campoEditoraPerfil.setError(getString(R.string.campo_obrigatorio));
        }
        if (unidadeLivro.getIdioma() == null || unidadeLivro.getIdioma().equals("")) {
            listaCampos.add("idioma");
            campoIdiomaPerfil.setError(getString(R.string.campo_obrigatorio));
        }
        if (foto.getCaminho() == null) {
            listaCampos.add("foto(Tire uma foto ou escolhe uma de sua galeria");
        }
        return listaCampos;
    }


    private void adcDisponibilidadesNoSpinner() throws Exception {
        disponibilidadeSpinnerPerfil = (Spinner) findViewById(R.id.disponibilidadeSpinnerPerfil);

        ArrayList<Disponibilidade> disponibilidades = DisponibilidadeServices.getInstancia(this).pegarDisponibilidades();

        Log.d("peteca", "" + disponibilidades.get(0));
        Log.d("peteca", "" + disponibilidades.get(1));



        ModeloArrayAdapter<Disponibilidade> dataAdapter = new ModeloArrayAdapter<Disponibilidade>(this, android.R.layout.simple_spinner_item, disponibilidades);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disponibilidadeSpinnerPerfil.setAdapter(dataAdapter);

        disponibilidadeSpinnerPerfil.getSelectedItem();

    }

    public void selectDisponibilidadeSpinnerItemById(int id) {

        ModeloArrayAdapter<Disponibilidade> adapter = (ModeloArrayAdapter<Disponibilidade>) disponibilidadeSpinnerPerfil.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).getId() == id) {
                disponibilidadeSpinnerPerfil.setSelection(position);
            }
        }
    }


    public void cadastrarLivro(Livro livro, UnidadeLivro unidadeLivro, Foto foto) {

        try {
            livro = livroServices.inserirLivro(livro);
            unidadeLivro.setIdLivro(livro.getId());
            unidadeLivroService.inserirUnidadeLivro(unidadeLivro);
            foto.setIdUnidadeLivro(unidadeLivro.getId());
            fotoServices.inserirFoto(foto);
            Toast.makeText(getApplication(), "Livro cadastrado", Toast.LENGTH_LONG).show();
            finish();
        } catch (SharepagesException e) {

        }
    }

    public void chamarBotaoTirarFoto() {

        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File localDaFoto = new File(Environment.getExternalStorageDirectory() + "/"
                        + System.currentTimeMillis() + ".jpg");

                caminhoFoto = localDaFoto.getAbsolutePath();

                File arquivo = new File(String.valueOf(localDaFoto));
                Uri localFotoUri = Uri.fromFile(arquivo);

                Intent irParaCamera = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFotoUri);
                startActivityForResult(irParaCamera, CODE_CAMERA_TIRAR);


            }

        });

    }

    private String imagePath;

    public void chamarBotaoSelecionarFoto() {

        selecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requisitarPermissaoParaExternalStorage();
            }
        });
    }

    public String getImagePath(Uri contentUri) {

        String[] campos = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, campos, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void iniciarSelecaoDeImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), CODE_CAMERA_SELECIONAR);
    }

    private void requisitarPermissaoParaExternalStorage() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_EXTERNAL_STORAGE_PERMISSION);

        } else {
            iniciarSelecaoDeImagem();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CODE_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    iniciarSelecaoDeImagem();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CODE_CAMERA_TIRAR) {
            if (resultCode != Activity.RESULT_OK) {
                caminhoFoto = null;
                Toast.makeText(getApplication(), "Erro ao tirar foto", Toast.LENGTH_LONG).show();

            }else {
                Uri visualizacao = Uri.fromFile(new File(caminhoFoto));
                preVisuFoto.setImageURI(visualizacao);
                Toast.makeText(getApplication(), "Foto registrada", Toast.LENGTH_LONG).show();

            }

        }else if (requestCode == CODE_CAMERA_SELECIONAR) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = intent.getData();
                imagePath = getImagePath(imageUri);
                caminhoFoto = imagePath;
                Uri visualizacao = Uri.fromFile(new File(caminhoFoto));
                preVisuFoto.setImageURI(visualizacao);
                Log.d("AQUI", imagePath);
                Toast.makeText(getApplication(), "Foto registrada", Toast.LENGTH_LONG).show();

            } else {
                caminhoFoto = null;
                Toast.makeText(getApplication(), "Erro ao selecionar foto", Toast.LENGTH_LONG).show();
            }
        }

    }

}