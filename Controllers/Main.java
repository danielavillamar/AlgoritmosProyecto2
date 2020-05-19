package Controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application
{
    private static Stage primaryStage;
    
    public void start(final Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            final BorderPane root = (BorderPane)FXMLLoader.load(this.getClass().getResource("/Views/Login.fxml"));
            final Scene scene = new Scene((Parent)root, 400.0, 550.0);
            primaryStage.setTitle("Proyecto2");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToUserEdit(final String userName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/UserEdit.fxml"));
            final Parent newScene = (Parent)loader.load();
            final UserEditController uec = (UserEditController)loader.getController();
            uec.setUserLoggedIn(userName);
            uec.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToLogin() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/Login.fxml"));
            final Parent newScene = (Parent)loader.load();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToSearch(final String userName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/SearchUser.fxml"));
            final Parent newScene = (Parent)loader.load();
            final SearchUserController suc = (SearchUserController)loader.getController();
            suc.setUserLoggedIn(userName);
            suc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToMovie(final String userName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/SearchMovie.fxml"));
            final Parent newScene = (Parent)loader.load();
            final SearchMovieController smc = (SearchMovieController)loader.getController();
            smc.setUserLoggedIn(userName);
            smc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToSelectedMovie(final String userName, final String movieId) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/SelectedMovie.fxml"));
            final Parent newScene = (Parent)loader.load();
            final SelectedMovieController ssmc = (SelectedMovieController)loader.getController();
            ssmc.setUserLoggedIn(userName);
            ssmc.setMovieId(movieId);
            ssmc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToMyList(final String userName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/MyList.fxml"));
            final Parent newScene = (Parent)loader.load();
            final MyListController mlc = (MyListController)loader.getController();
            mlc.setUserLoggedIn(userName);
            mlc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToRecommendedByList(final String userName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/RecommendedByList.fxml"));
            final Parent newScene = (Parent)loader.load();
            final RecommendedByListController rblc = (RecommendedByListController)loader.getController();
            rblc.setUserLoggedIn(userName);
            rblc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToRecommendedByMovie(final String userName, final String movieId) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/RecommendedByMovie.fxml"));
            final Parent newScene = (Parent)loader.load();
            final RecommendedByMovieController rbmc = (RecommendedByMovieController)loader.getController();
            rbmc.setUserLoggedIn(userName);
            rbmc.setMovieId(movieId);
            rbmc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void changeToList(final String userName, final String selectedUserName) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Views/List.fxml"));
            final Parent newScene = (Parent)loader.load();
            final ListController lc = (ListController)loader.getController();
            lc.setUserLoggedIn(userName);
            lc.setSelectedUserName(selectedUserName);
            lc.llenarClase();
            final Scene scene = new Scene(newScene, 400.0, 550.0);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        launch(args);
    }
}
