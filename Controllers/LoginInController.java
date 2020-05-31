package Controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.Map;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import javafx.scene.control.Alert;
import java.io.File;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import javafx.event.ActionEvent;
import org.neo4j.graphdb.GraphDatabaseService;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

public class LoginInController
{
    @FXML
    private Controllers.Main main;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField passwordTextField;
    private String name;
    private String password;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void login(final ActionEvent event) throws IOException {
        final Boolean verificado = this.verificarDatos();
        if (verificado) {
            final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
            registerShutdownHook(db);
            final Transaction tx = db.beginTx();
            String userName = "";
            try {
                final Result result = db.execute("MATCH (u:User)WHERE u.name='" + this.name + "'  and u.password='" + this.password + "'" + "RETURN u.name");
                tx.success();
                if (result.hasNext()) {
                    final Map<String, Object> user = (Map<String, Object>)result.next();
                    this.main = new Controllers.Main();
                    userName = user.get("u.name");
                }
                else {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error al iniciar sesion");
                    alert.setContentText("Verifica que los datos esten correctos");
                    alert.showAndWait();
                }
            }
            finally {
                tx.close();
                db.shutdown();
                if (this.main != null) {
                    this.main.changeToUserEdit(userName);
                }
            }
            tx.close();
            db.shutdown();
            if (this.main != null) {
                this.main.changeToUserEdit(userName);
            }
        }
        else if (!verificado) {
            final Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Error");
            alert2.setHeaderText("Error en datos ingresado");
            alert2.setContentText("Verifica tus datos ingresados");
            alert2.showAndWait();
        }
    }

    public Boolean verificarDatos() {
        try {
            this.name = this.nameTextField.getText();
            if (this.name == null) {
                return false;
            }
            this.password = this.passwordTextField.getText();
            if (this.password == null) {
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void crearCuenta(final ActionEvent event) throws IOException {
        final Parent newScene = (Parent)FXMLLoader.load(this.getClass().getResource("/Views/SignIn.fxml"));
        final Scene scene = new Scene(newScene, 400.0, 550.0);
        final Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
