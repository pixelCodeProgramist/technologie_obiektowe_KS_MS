package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Model {
    private String id;
    private String url;
    private String description;
    private Image image;
    private ImageView imageView;


    public Model(String url, String description) throws FileNotFoundException {
        this.url = url;
        this.description = description;
        this.image = new Image(new FileInputStream(url));
        this.imageView = new ImageView(this.image);
    }



    public String getDescription() {
        return description;
    }

    public ImageView getImageView() {

        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        return imageView;
    }

    public ImageView getImageView(double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
}
