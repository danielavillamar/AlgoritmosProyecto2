import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Map;

public class UserEditController
{
    private Main main;
    @FXML
    private TextField username;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private Button account;
    @FXML
    private Button movie;
    @FXML
    private Button search;
    @FXML
    private Button list;
    private String nombre;
    private String numero;
    private String correoe;
    private String userLoggedIn;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void guardarUsuario() {
        final Boolean verificado = this.verificarDatos();
        if (verificado) {
            final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
            registerShutdownHook(db);
            final Transaction tx = db.beginTx();
            try {
                final Result result = db.execute("MATCH (u:User)WHERE u.name='" + this.nombre + "'" + "RETURN u.name");
                tx.success();
                if (result.hasNext()) {
                    final Map<String, Object> user = (Map<String, Object>)result.next();
                    final String userName = user.get("u.name");
                    if (!this.userLoggedIn.equals(userName)) {
                        final Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error en actualizar");
                        alert.setContentText("El nombre del uzuario ya ha sido utilizado");
                        alert.showAndWait();
                    }
                    else {
                        final Result result2 = db.execute("MATCH (u:User)WHERE u.name='" + this.userLoggedIn + "'" + " SET u.name='" + this.nombre + "'" + " SET u.phone='" + this.numero + "'" + " SET u.email='" + this.correoe + "'" + " RETURN u.name");
                        tx.success();
                        if (result2.hasNext()) {
                            this.userLoggedIn = this.nombre;
                            final Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("\u00c9xito");
                            alert2.setHeaderText("Se ha actualizado la informaci\u00f3n del usuario");
                            alert2.showAndWait();
                        }
                    }
                }
                else {
                    final Result result3 = db.execute("MATCH (u:User)WHERE u.name='" + this.userLoggedIn + "'" + " SET u.name='" + this.nombre + "'" + " SET u.phone='" + this.numero + "'" + " SET u.email='" + this.correoe + "'" + " RETURN u.name");
                    tx.success();
                    if (result3.hasNext()) {
                        this.userLoggedIn = this.nombre;
                        final Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setTitle("\u00c9xito");
                        alert3.setHeaderText("Se ha actualizado la informaci\u00f3n del usuario");
                        alert3.showAndWait();
                    }
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
            final Alert alert4 = new Alert(Alert.AlertType.ERROR);
            alert4.setTitle("Error");
            alert4.setHeaderText("Error en datos ingresados");
            alert4.setContentText("Verifica tus datos ingresados");
            alert4.showAndWait();
        }
    }

    public String getUserLoggedIn() {
        return this.userLoggedIn;
    }

    public void setUserLoggedIn(final String userName) {
        this.userLoggedIn = userName;
    }

    public Boolean verificarDatos() {
        try {
            this.nombre = this.username.getText();
            final int numeroInt = Integer.parseInt(this.phone.getText());
            this.numero = this.phone.getText();
            this.correoe = this.email.getText();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void goToAccount() {
        (this.main = new Main()).changeToUserEdit(this.userLoggedIn);
    }

    public void salirUsuario() {
        (this.main = new Main()).changeToLogin();
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
        final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("moviesDb/"));
        registerShutdownHook(db);
        final Transaction tx = db.beginTx();
        try {
            final Result result = db.execute("MATCH (u:User)WHERE u.name='" + this.userLoggedIn + "'" + "RETURN u.name, u.phone, u.email");
            tx.success();
            if (result.hasNext()) {
                final Map<String, Object> user = (Map<String, Object>)result.next();
                this.username.setText((String)user.get("u.name"));
                this.phone.setText((String)user.get("u.phone"));
                this.email.setText((String)user.get("u.email"));
            }
        }
        finally {
            tx.close();
            db.shutdown();
            try {
                final ImageView accountImage = new ImageView(new Image(this.getClass().getResource("/images/account.png").toString()));
                accountImage.setFitHeight(50.0);
                accountImage.setFitWidth(50.0);
                this.account.setGraphic((Node)accountImage);
                this.account.setDisable(true);
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
        }
        tx.close();
        db.shutdown();
        try {
            final ImageView accountImage = new ImageView(new Image(this.getClass().getResource("/images/account.png").toString()));
            accountImage.setFitHeight(50.0);
            accountImage.setFitWidth(50.0);
            this.account.setGraphic((Node)accountImage);
            this.account.setDisable(true);
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
        catch (Exception ex2) {}
    }
}