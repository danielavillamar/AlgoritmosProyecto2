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

public class SearchUserController
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
    private String userLoggedIn;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void recomendarUsuarios() {
        final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
        registerShutdownHook(db);
        final Transaction tx = db.beginTx();
        try {
            final Result result = db.execute("MATCH (u:User) -[:LIKES]-> (m:Movie)WHERE u.name = '" + this.userLoggedIn + "'" + "MATCH (u2:User) -[:LIKES]-> (m:Movie)" + "WHERE u2.name <> u.name " + "RETURN u2.name");
            tx.success();
            if (result.hasNext()) {
                this.searchFlowPane.getChildren().clear();
                while (result.hasNext()) {
                    final Map<String, Object> user = (Map<String, Object>)result.next();
                    final String userName = user.get("u2.name");
                    final Label label = new Label(String.valueOf(userName) + "   ");
                    label.setFont(new Font(18.0));
                    final Button button = new Button();
                    button.setId(userName);
                    button.setText("Ver lista");
                    button.setStyle("-fx-background-color: lime;");
                    button.setOnAction((EventHandler)new EventHandler<ActionEvent>() {
                        public void handle(final ActionEvent event) {
                            final Button currentButton = (Button)event.getSource();
                            final String userName = currentButton.getId();
                            SearchUserController.access$0(SearchUserController.this, new Main());
                            SearchUserController.this.main.changeToList(SearchUserController.this.userLoggedIn, userName);
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
                alert.setHeaderText("No se encontraron usuarios recomendados");
                alert.setContentText("Prueba agregando m\u00e1s pel\u00edculas a tu lista");
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

    public String getUserLoggedIn() {
        return this.userLoggedIn;
    }

    public void setUserLoggedIn(final String userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
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
            this.search.setDisable(true);
            final ImageView listImage = new ImageView(new Image(this.getClass().getResource("/images/list.png").toString()));
            listImage.setFitHeight(50.0);
            listImage.setFitWidth(50.0);
            this.list.setGraphic((Node)listImage);
        }
        catch (Exception ex) {}
        this.recomendarUsuarios();
    }

    static /* synthetic */ void access$0(final SearchUserController searchUserController, final Main main) {
        searchUserController.main = main;
    }
}