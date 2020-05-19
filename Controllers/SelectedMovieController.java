package Controllers;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import java.util.Map;
import javafx.event.Event;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.neo4j.graphdb.GraphDatabaseService;

public class SelectedMovieController
{
    private Main main;
    private GraphDatabaseService db;
    @FXML
    private Button account;
    @FXML
    private Button movie;
    @FXML
    private Button search;
    @FXML
    private Button list;
    @FXML
    private Button agregarLista;
    @FXML
    private Label name;
    @FXML
    private Label date;
    @FXML
    private Label actor;
    @FXML
    private Label genre;
    @FXML
    private Label director;
    @FXML
    private Label producer;
    @FXML
    private Label qualification;
    @FXML
    private Label popularity;
    private String movieId;
    private String userLoggedIn;
    
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
    
    public void buscarPelicula() {
        registerShutdownHook(this.db);
        final Transaction tx = this.db.beginTx();
        try {
            final Result result = this.db.execute("MATCH (m:Movie)WHERE m.id = '" + this.movieId + "'" + "RETURN m.name, m.genre, m.date, m.actor, m.director, m.producer, m.qualification, m.popularity");
            tx.success();
            if (result.hasNext()) {
                final Map<String, Object> movie = (Map<String, Object>)result.next();
                this.name.setText((String)movie.get("m.name"));
                this.date.setText((String)movie.get("m.date"));
                this.actor.setText("Actor: " + movie.get("m.actor"));
                this.genre.setText("G\u00e9nero: " + movie.get("m.genre"));
                this.director.setText("Director: " + movie.get("m.director"));
                this.producer.setText("Productor: " + movie.get("m.producer"));
                this.qualification.setText("Calificaci\u00f3n: " + movie.get("m.qualification"));
                this.popularity.setText("Popularidad: " + movie.get("m.popularity"));
                final Result result2 = this.db.execute("MATCH (u:User) -[:LIKES]-> (m:Movie)WHERE m.id='" + this.movieId + "' and u.name='" + this.userLoggedIn + "'" + "RETURN u.name");
                tx.success();
                if (result2.hasNext()) {
                    this.agregarLista.setText("Eliminar de mi Lista");
                    this.agregarLista.setStyle("-fx-background-color: red;");
                    this.agregarLista.setOnAction((EventHandler)new EventHandler<ActionEvent>() {
                        public void handle(final ActionEvent event) {
                            registerShutdownHook(SelectedMovieController.this.db);
                            final Transaction tx = SelectedMovieController.this.db.beginTx();
                            try {
                                final Result result3 = SelectedMovieController.this.db.execute("MATCH (u:User) -[l:LIKES]-> (m:Movie)WHERE m.id='" + SelectedMovieController.this.movieId + "' and u.name='" + SelectedMovieController.this.userLoggedIn + "'" + "DELETE l " + "RETURN u.name");
                                tx.success();
                                if (result3.hasNext()) {
                                    SelectedMovieController.this.buscarPelicula();
                                }
                            }
                            finally {
                                tx.close();
                            }
                            tx.close();
                        }
                    });
                }
                else {
                    this.agregarLista.setText("Agregar a mi Lista");
                    this.agregarLista.setStyle("-fx-background-color: lime;");
                    this.agregarLista.setOnAction((EventHandler)new EventHandler<ActionEvent>() {
                        public void handle(final ActionEvent event) {
                            registerShutdownHook(SelectedMovieController.this.db);
                            final Transaction tx = SelectedMovieController.this.db.beginTx();
                            try {
                                final Result result3 = SelectedMovieController.this.db.execute("MATCH (u:User) WHERE u.name='" + SelectedMovieController.this.userLoggedIn + "'" + "MATCH (m:Movie) WHERE m.id='" + SelectedMovieController.this.movieId + "'" + "CREATE (u) -[:LIKES]-> (m)" + "RETURN u.name");
                                tx.success();
                                if (result3.hasNext()) {
                                    SelectedMovieController.this.buscarPelicula();
                                }
                            }
                            finally {
                                tx.close();
                            }
                            tx.close();
                        }
                    });
                }
            }
        }
        finally {
            tx.close();
        }
        tx.close();
    }
    
    public String getUserLoggedIn() {
        return this.userLoggedIn;
    }
    
    public void setUserLoggedIn(final String userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }
    
    public String getMovieId() {
        return this.movieId;
    }
    
    public void setMovieId(final String movieId) {
        this.movieId = movieId;
    }
    
    public void goToRecommendedByMovie() {
        this.db.shutdown();
        (this.main = new Main()).changeToRecommendedByMovie(this.userLoggedIn, this.movieId);
    }
    
    public void goToAccount() {
        this.db.shutdown();
        (this.main = new Main()).changeToUserEdit(this.userLoggedIn);
    }
    
    public void goToMovie() {
        this.db.shutdown();
        (this.main = new Main()).changeToMovie(this.userLoggedIn);
    }
    
    public void goToSearch() {
        this.db.shutdown();
        (this.main = new Main()).changeToSearch(this.userLoggedIn);
    }
    
    public void goToList() {
        this.db.shutdown();
        (this.main = new Main()).changeToMyList(this.userLoggedIn);
    }
    
    public void llenarClase() {
        this.db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
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
        this.buscarPelicula();
    }
}
