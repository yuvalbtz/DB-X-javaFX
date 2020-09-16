package sample;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.controlsfx.control.PropertySheet;

import static sample.gluonApplicationextends.PRIMARY_VIEW;
import static sample.gluonApplicationextends.SECONDARY_VIEW;

public class drawerApplication {

    public static void buildDrawer(MobileApplication app) {
        NavigationDrawer drawer = app.getDrawer();

        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);

        final PropertySheet.Item primaryItem = new ViewItem("Primary", MaterialDesignIcon.HOME.graphic(), PRIMARY_VIEW, ViewStackPolicy.SKIP);
        final PropertySheet.Item secondaryItem = new ViewItem("Secondary", MaterialDesignIcon.DASHBOARD.graphic(), SECONDARY_VIEW);
        drawer.getItems().addAll(primaryItem, secondaryItem);

        if (Platform.isDesktop()) {
            final PropertySheet.Item quitItem = new PropertySheet.Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }


















}
