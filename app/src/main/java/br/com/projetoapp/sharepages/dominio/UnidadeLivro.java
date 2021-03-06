package br.com.projetoapp.sharepages.dominio;

import java.util.List;

public class UnidadeLivro {
    private int id;
    private String descricao;
    private String idioma;
    private String edicao;
    private int numeroPaginas;
    private String editora;
    private Livro livro;
    private int idLivro;
    private List<Foto> fotos;
    private Disponibilidade disponibilidade;
    private int idDisponibilidade;
    private Usuario usuario;
    private int idUsuario;
    private EncerramentoDoAnuncio situacao;

    public UnidadeLivro(String editora, int nDePaginas, String edicao, String descricao, String idioma, Disponibilidade disponibilidade, int idDisponibilidade, int idUsuario) {
        this.editora = editora;
        this.numeroPaginas = nDePaginas;
        this.edicao = edicao;
        this.descricao = descricao;
        this.idioma = idioma;
        this.disponibilidade = disponibilidade;
        this.idDisponibilidade = idDisponibilidade;
        this.idUsuario = idUsuario;
    }

    public  UnidadeLivro(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getIdDisponibilidade() {
        return idDisponibilidade;
    }

    public void setIdDisponibilidade(int idDisponibilidade) {
        this.idDisponibilidade = idDisponibilidade;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public EncerramentoDoAnuncio getSituacao() {
        return situacao;
    }

    public void setSituacao(EncerramentoDoAnuncio situacao) {
        this.situacao = situacao;
    }
}
