package sample;

import javafx.fxml.FXMLLoader;

import javax.swing.text.View;
import java.io.IOException;

public class firstVeiw {

    public View getView() {
        try {
            View view = FXMLLoader.load(PrimaryView.class.getResource("primary.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }











}
