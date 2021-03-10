package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Model {
    private String url;
    private String description;
    private Image image;

    public Model(String url, String description) throws FileNotFoundException {
        this.url = url;
        this.description = description;
        this.image = new Image(new FileInputStream(url));
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImageView() {
        ImageView imageView = new ImageView(this.image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        return imageView;
    }
}
