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
import java.util.ArrayList;
import java.io.File;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

    public class RecommendedByListController
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

        public void recomendarPorLista() {
            final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
            registerShutdownHook(db);
            final Transaction tx = db.beginTx();
            try {
                final Result result = db.execute("MATCH (m2:Movie), (u:User) -[:LIKES]-> (m:Movie)WHERE (m2.genre = m.genre or m2.actor = m.actor or m2.director = m.director or m2.producer = m.producer) and m2.id <> m.id and m2.popularity >= m.popularity and u.name = '" + this.userLoggedIn + "' " + "RETURN m2.id, m2.name LIMIT 10");
                tx.success();
                if (result.hasNext()) {
                    this.searchFlowPane.getChildren().clear();
                    final ArrayList<String> moviesId = new ArrayList<String>();
                    while (result.hasNext()) {
                        final Map<String, Object> movie = (Map<String, Object>)result.next();
                        final String movieId = movie.get("m2.id");
                        final String movieName = movie.get("m2.name");
                        if (!moviesId.contains(movieId) && moviesId.size() < 10) {
                            moviesId.add(movieId);
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
                                    RecommendedByListController.access$0(RecommendedByListController.this, new Main());
                                    RecommendedByListController.this.main.changeToSelectedMovie(RecommendedByListController.this.userLoggedIn, movieId);
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
                final ImageView listImage = new ImageView(new Image(this.getClass().getResource("/images/list.png").toString()));
                listImage.setFitHeight(50.0);
                listImage.setFitWidth(50.0);
                this.list.setGraphic((Node)listImage);
            }
            catch (Exception ex) {}
            this.recomendarPorLista();
        }

        static /* synthetic */ void access$0(final RecommendedByListController recommendedByListController, final Main main) {
            recommendedByListController.main = main;
        }
    }
