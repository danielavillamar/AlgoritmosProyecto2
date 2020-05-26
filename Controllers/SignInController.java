import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class SignInController implements Initializable
{
    private Main main;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private DatePicker birthDay;
    private String name;
    private String phone;
    private String password;
    private String email;
    private String birth;

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void crearCuenta(final ActionEvent event) throws IOException {
        final Boolean verificado = this.verificarDatos();
        if (verificado) {
            final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
            registerShutdownHook(db);
            final Transaction tx = db.beginTx();
            try {
                final Result result = db.execute("MATCH (u:User)WHERE u.name='" + this.name + "'" + "RETURN u.name");
                tx.success();
                if (result.hasNext()) {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al crear usuario");
                    alert.setHeaderText("Cambia el nombre del usuario");
                    alert.setContentText("El nombre del usuario tiene que ser unico");
                    alert.showAndWait();
                }
                else {
                    final Node node = db.createNode(new Label[] { Label.label("User") });
                    node.setProperty("name", (Object)this.name);
                    node.setProperty("phone", (Object)this.phone);
                    node.setProperty("password", (Object)this.password);
                    node.setProperty("email", (Object)this.email);
                    node.setProperty("birth", (Object)this.birth);
                    tx.success();
                    this.main = new Main();
                }
            }
            finally {
                tx.close();
                db.shutdown();
                this.main.changeToUserEdit(this.name);
            }
            tx.close();
            db.shutdown();
            this.main.changeToUserEdit(this.name);
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
            this.email = this.emailTextField.getText();
            if (this.email == null) {
                return false;
            }
            this.password = this.passwordTextField.getText();
            if (this.password == null) {
                return false;
            }
            final int phoneInt = Integer.parseInt(this.phoneTextField.getText());
            this.phone = this.phoneTextField.getText();
            final LocalDate localDate = (LocalDate)this.birthDay.getValue();
            final Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            this.birth = dateFormat.format(date);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void initialize(final URL location, final ResourceBundle resources) {
    }
}