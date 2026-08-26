package estoque;

import estoque.controller.ProdutoController;
import estoque.database.Database;
import estoque.model.Produto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private TextField campoNome;
    private TextField campoPreco;
    private TextField campoQuantidade;

    private TableView<Produto> tabela;

    private Produto produtoSelecionado;

    private final ProdutoController controller =
            new ProdutoController();


    @Override
    public void start(Stage stage) {

        Database.criarTabela();

        Label titulo =
                new Label("📦 Sistema de Controle de Estoque");

        titulo.getStyleClass().add("titulo");


        campoNome = new TextField();
        campoNome.setPromptText("Nome do produto");

        campoPreco = new TextField();
        campoPreco.setPromptText("Preço R$");

        campoQuantidade = new TextField();
        campoQuantidade.setPromptText("Quantidade");


        Button botaoCadastrar =
                new Button("Cadastrar");

        Button botaoAtualizar =
                new Button("Atualizar");

        Button botaoExcluir =
                new Button("Excluir");

        Button botaoLimpar =
                new Button("Limpar");


        HBox botoes = new HBox(
                10,
                botaoCadastrar,
                botaoAtualizar,
                botaoExcluir,
                botaoLimpar
        );

        botoes.setAlignment(Pos.CENTER);


        VBox formulario = new VBox(
                10,
                campoNome,
                campoPreco,
                campoQuantidade,
                botoes
        );

        formulario.setPadding(new Insets(20));


        tabela = new TableView<>();


        TableColumn<Produto, Integer> colunaId =
                new TableColumn<>("ID");

        colunaId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colunaId.setPrefWidth(70);


        TableColumn<Produto, String> colunaNome =
                new TableColumn<>("Produto");

        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );

        colunaNome.setPrefWidth(250);


        TableColumn<Produto, Double> colunaPreco =
                new TableColumn<>("Preço R$");

        colunaPreco.setCellValueFactory(
                new PropertyValueFactory<>("preco")
        );

        colunaPreco.setPrefWidth(150);


        TableColumn<Produto, Integer> colunaQuantidade =
                new TableColumn<>("Quantidade");

        colunaQuantidade.setCellValueFactory(
                new PropertyValueFactory<>("quantidade")
        );

        colunaQuantidade.setPrefWidth(150);


        tabela.getColumns().addAll(
                colunaId,
                colunaNome,
                colunaPreco,
                colunaQuantidade
        );


        atualizarTabela();


        tabela.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, antigo, produto) -> {

                            if (produto != null) {

                                produtoSelecionado = produto;

                                campoNome.setText(
                                        produto.getNome()
                                );

                                campoPreco.setText(
                                        String.valueOf(
                                                produto.getPreco()
                                        )
                                );

                                campoQuantidade.setText(
                                        String.valueOf(
                                                produto.getQuantidade()
                                        )
                                );
                            }
                        }
                );


        botaoCadastrar.setOnAction(event -> {

            try {

                Produto produto = new Produto(
                        campoNome.getText(),
                        Double.parseDouble(
                                campoPreco.getText()
                        ),
                        Integer.parseInt(
                                campoQuantidade.getText()
                        )
                );

                controller.cadastrar(produto);

                atualizarTabela();

                limparCampos();

                mostrarMensagem(
                        "Sucesso",
                        "Produto cadastrado com sucesso!"
                );

            } catch (Exception e) {

                mostrarMensagem(
                        "Erro",
                        "Preencha os campos corretamente!"
                );
            }

        });


        botaoAtualizar.setOnAction(event -> {

            if (produtoSelecionado == null) {

                mostrarMensagem(
                        "Atenção",
                        "Selecione um produto na tabela!"
                );

                return;
            }

            try {

                produtoSelecionado.setNome(
                        campoNome.getText()
                );

                produtoSelecionado.setPreco(
                        Double.parseDouble(
                                campoPreco.getText()
                        )
                );

                produtoSelecionado.setQuantidade(
                        Integer.parseInt(
                                campoQuantidade.getText()
                        )
                );

                controller.atualizar(
                        produtoSelecionado
                );

                atualizarTabela();

                limparCampos();

                mostrarMensagem(
                        "Sucesso",
                        "Produto atualizado!"
                );

            } catch (Exception e) {

                mostrarMensagem(
                        "Erro",
                        "Verifique os campos!"
                );
            }

        });


        botaoExcluir.setOnAction(event -> {

            if (produtoSelecionado == null) {

                mostrarMensagem(
                        "Atenção",
                        "Selecione um produto!"
                );

                return;
            }


            Alert confirmacao =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmacao.setTitle("Confirmar exclusão");

            confirmacao.setHeaderText(
                    "Deseja excluir este produto?"
            );

            confirmacao.setContentText(
                    produtoSelecionado.getNome()
            );


            if (
                    confirmacao.showAndWait().get()
                            == ButtonType.OK
            ) {

                controller.excluir(
                        produtoSelecionado.getId()
                );

                atualizarTabela();

                limparCampos();

                mostrarMensagem(
                        "Sucesso",
                        "Produto excluído!"
                );
            }

        });


        botaoLimpar.setOnAction(event -> {

            limparCampos();

        });


        VBox layout = new VBox(
                10,
                titulo,
                formulario,
                tabela
        );

        layout.setPadding(new Insets(20));

        VBox.setVgrow(
                tabela,
                Priority.ALWAYS
        );


        Scene scene =
                new Scene(layout, 700, 600);

        var css = getClass().getResource("/application.css");

        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }


        stage.setTitle(
                "Sistema de Estoque"
        );

        stage.setScene(scene);

        stage.show();
    }


    private void atualizarTabela() {

        ObservableList<Produto> produtos =
                FXCollections.observableArrayList(
                        controller.listar()
                );

        tabela.setItems(produtos);
    }


    private void limparCampos() {

        campoNome.clear();

        campoPreco.clear();

        campoQuantidade.clear();

        produtoSelecionado = null;

        tabela.getSelectionModel()
                .clearSelection();
    }


    private void mostrarMensagem(
            String titulo,
            String mensagem
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensagem);

        alert.showAndWait();
    }


    public static void main(String[] args) {

        launch(args);
    }
}