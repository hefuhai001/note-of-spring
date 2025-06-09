//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.HBox;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//import javafx.stage.Stage;
//
//public class _14_2_mediaExample extends Application {
//
//    @Override
//    public void start(Stage P) {
//        String videoPath = "file:///C:/Users/Administrator/Desktop/media/snycj.mp4";
//        Media media = new Media(videoPath);
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setAutoPlay(true);
//        MediaView mediaView = new MediaView(mediaPlayer);
//        HBox hBox = new HBox();
//        hBox.getChildren().addAll(mediaView);
//
//        Scene s = new Scene(hBox, 400, 300);
//        P.setTitle("_14_2_mediaExample");
//        P.setScene(s);
//        P.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
