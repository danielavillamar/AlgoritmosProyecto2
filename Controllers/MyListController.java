import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Map;

public class MyListController
{
    private Main main;
    @FXML
    private FlowPane searchFlowPane;
    @FXML
    private Button account;
    @FXML
    private Button movie;
    @FXML
    private Button search;
    @FXML
    private Button list;
    @FXML
    private Button button;
    private String userLoggedIn;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void verMiLista() {
        final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
        registerShutdownHook(db);
        final Transaction tx = db.beginTx();
        try {
            final Result result = db.execute("MATCH (u:User) -[:LIKES]-> (m:Movie)WHERE u.name = '" + this.userLoggedIn + "'" + "RETURN m.id, m.name");
            tx.success();
            if (result.hasNext()) {
                this.searchFlowPane.getChildren().clear();
                while (result.hasNext()) {
                    final Map<String, Object> movie = (Map<String, Object>)result.next();
                    final String movieId = movie.get("m.id");
                    final String movieName = movie.get("m.name");
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
                            MyListController.access$0(MyListController.this, new Main());
                            MyListController.this.main.changeToSelectedMovie(MyListController.this.userLoggedIn, movieId);
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
                alert.setHeaderText("No se encontraron peliculas en tu lista");
                alert.setContentText("Agrega pel\u00edculas a tu lista");
                alert.showAndWait();
                this.button.setDisable(true);
            }
        }
        finally {
            tx.close();
            db.shutdown();
        }
        tx.close();
        db.shutdown();
    }

    public String getUserLoggedIn() {
        return this.userLoggedIn;
    }

    public void setUserLoggedIn(final String userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public void goToRecommendedByList() {
        (this.main = new Main()).changeToRecommendedByList(this.userLoggedIn);
    }

    public void goToAccount() {
        (this.main = new Main()).changeToUserEdit(this.userLoggedIn);
    }

    public void goToMovie() {
        (this.main = new Main()).changeToMovie(this.userLoggedIn);
    }

    public void goToSearch() {
        (this.main = new Main()).changeToSearch(this.userLoggedIn);
    }

    public void goToList() {
        (this.main = new Main()).changeToMyList(this.userLoggedIn);
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
            final ImageView searchImage = new ImageView(new Image(this.getClass().getResource("/images/search.png").toString()));
            searchImage.setFitHeight(50.0);
            searchImage.setFitWidth(50.0);
            this.search.setGraphic((Node)searchImage);
            final ImageView listImage = new ImageView(new Image(this.getClass().getResource("/images/list.png").toString()));
            listImage.setFitHeight(50.0);
            listImage.setFitWidth(50.0);
            this.list.setGraphic((Node)listImage);
            this.list.setDisable(true);
        }
        catch (Exception ex) {}
        this.verMiLista();
    }

    static /* synthetic */ void access$0(final MyListController myListController, final Main main) {
        myListController.main = main;
    }
}