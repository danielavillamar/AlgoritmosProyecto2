package Controllers;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.Map;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import javafx.scene.control.Alert;
import javafx.scene.shape.Line;
import javafx.scene.layout.Region;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import java.io.File;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class SearchMovieController
{
    private Controllers.Main main;
    @FXML
    private FlowPane searchFlowPane;
    @FXML
    private TextField movieNameTextField;
    @FXML
    private Button account;
    @FXML
    private Button movie;
    @FXML
    private Button search;
    @FXML
    private Button list;
    private String movieName;
    private String userLoggedIn;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void buscarPeliculas() {
        final Boolean verificado = this.verificarDatos();
        if (verificado) {
            final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
            registerShutdownHook(db);
            final Transaction tx = db.beginTx();
            try {
                final Result result = db.execute("MATCH (m:Movie)WHERE m.name =~ '." + this.movieName + ".'" + "RETURN m.name, m.id");
                tx.success();
                if (result.hasNext()) {
                    this.searchFlowPane.getChildren().clear();
                    while (result.hasNext()) {
                        final Map<String, Object> user = (Map<String, Object>)result.next();
                        final String movieName = user.get("m.name");
                        final String movieId = user.get("m.id");
                        final Label label = new Label(String.valueOf(movieName) + "   ");
                        label.setFont(new Font(18.0));
                        final Button button = new Button();
                        button.setId(movieId);
                        button.setText("Ver Pel\u00edcula");
                        button.setStyle("-fx-background-color: lime;");
                        button.setOnAction((EventHandler)new EventHandler<ActionEvent>() {
                            public void handle(final ActionEvent event) {
                                final Button currentButton = (Button)event.getSource();
                                final String movieId = currentButton.getId();
                                SearchMovieController.access$0(SearchMovieController.this, new Main());
                                SearchMovieController.this.main.changeToSelectedMovie(SearchMovieController.this.userLoggedIn, movieId);
                            }
                        });
                        final Region p = new Region();
                        p.setPrefSize(497.0, 4.0);
                        final Line line = new Line(0.0, 0.0, 500.0, 0.0);
                        final Region p2 = new Region();
                        p2.setPrefSize(497.0, 4.0);
                        this.searchFlowPane.getChildren().add((Object)label);
                        this.searchFlowPane.getChildren().add((Object)button);
                        this.searchFlowPane.getChildren().add((Object)p);
                        this.searchFlowPane.getChildren().add((Object)line);
                        this.searchFlowPane.getChildren().add((Object)p2);
                    }
                }
                else {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error al buscar la pel\u00edcula");
                    alert.setContentText("No hay ninguna pal\u00edcula con ese nombre");
                    alert.showAndWait();
                }
            }
            finally {
                tx.close();
                db.shutdown();
            }
            tx.close();
            db.shutdown();
        }
        else if (!verificado) {
            final Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Error");
            alert2.setHeaderText("Error en datos ingresados");
            alert2.setContentText("Verifica tus datos ingresados");
            alert2.showAndWait();
        }
    }

    public String getUserLoggedIn() {
        return this.userLoggedIn;
    }

    public void setUserLoggedIn(final String userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public Boolean verificarDatos() {
        try {
            this.movieName = this.movieNameTextField.getText();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void goToAccount() {
        (this.main = new Controllers.Main()).changeToUserEdit(this.userLoggedIn);
    }

    public void goToMovie() {
        (this.main = new Controllers.Main()).changeToMovie(this.userLoggedIn);
    }

    public void goToSearch() {
        (this.main = new Controllers.Main()).changeToSearch(this.userLoggedIn);
    }

    public void goToList() {
        (this.main = new Controllers.Main()).changeToMyList(this.userLoggedIn);
    }

    public void llenarClase() {
        try {
            final ImageView accountImage = new ImageView(new Image(this.getClass().getResource("/images/account.png").toString()));
            accountImage.setFitHeight(50.0);
            accountImage.setFitWidth(50.0);
            this.account.setGraphic((Node)accountImage);
            final ImageView movieImage = new ImageView(new Image(this.getClass().getResource("/images/movie.png").toString()));
            movieImage.setFitHeight(50.0);
            movieImage.setFitWidth(50.0);
            this.movie.setGraphic((Node)movieImage);
            this.movie.setDisable(true);
            final ImageView searchImage = new ImageView(new Image(this.getClass().getResource("/images/search.png").toString()));
            searchImage.setFitHeight(50.0);
            searchImage.setFitWidth(50.0);
            this.search.setGraphic((Node)searchImage);
            final ImageView listImage = new ImageView(new Image(this.getClass().getResource("/images/list.png").toString()));
            listImage.setFitHeight(50.0);
            listImage.setFitWidth(50.0);
            this.list.setGraphic((Node)listImage);
        }
        catch (Exception ex) {}
    }

    static /* synthetic */ void access$0(final SearchMovieController searchMovieController, final Controllers.Main main) {
        searchMovieController.main = main;
    }
}
