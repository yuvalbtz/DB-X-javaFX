package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxdb.ModelTable;



import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.font.ImageGraphicAttribute;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller  implements Initializable {



    Statement st;
    PreparedStatement stp;
    //private Object FilteredList;

   private static boolean flag =false;




    public Connection getConnection() {
            Connection con;
            try {
                  Class.forName("org.sqlite.JDBC");
                  con = DriverManager.getConnection("jdbc:sqlite:mysqldb.db");
                  //Statement statements = con.createStatement();
                  //statements.execute("DROP TABLE employees");

                  if(!flag){
                     flag = true;

                     Statement statement = con.createStatement();
                     ResultSet rs = statement.executeQuery("select name from sqlite_master WHERE type ='table' AND name='employees'");
                     if (!rs.next()){
                         System.out.println("DATABASE BUILDED...");
                         Statement statement1 = con.createStatement();
                         statement1.execute("CREATE TABLE employees (\n" +
                                 "  emp_id INTEGER primary key AUTOINCREMENT,\n" +
                                 "  first_name VARCHAR(40),\n" +
                                 "  last_name VARCHAR(40),\n" +
                                 "  birth_day DATE,\n" +
                                 "  sex VARCHAR(6),\n" +
                                 "  salary INT,\n" +
                                 "  image LONGBLOB,\n" +
                                 "  filepath VARCHAR(80)\n" +
                                 " )");

                     }





                  }

                  System.out.println("Action Succes!...");
              /// con.close();
           return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // private void Browser(ActionEvent event){
     //   System.out.println("hello!");



   // }





    @FXML
    private TableView<ModelTable> tableView;

    @FXML
    private TableColumn<ModelTable, InputStream> col_imagev;

    @FXML
    private TableColumn<ModelTable, String> col_fnamev;

    @FXML
    private TableColumn<ModelTable, String> col_lnamev;


    @FXML
    private TableColumn<ModelTable, Date> col_birthdatev;


    @FXML
    private TableColumn<ModelTable, String> col_sexv;

    @FXML
    private TableColumn<ModelTable, Integer> col_salaryv;


    //@FXML
    // private TableView tableView;


    @FXML
    private InputStream is;

    @FXML
    private OutputStream os;

    @FXML
    private FileInputStream fis;


    @FXML
    private FileChooser fileChooser;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageView1;


    @FXML
    private Button showimageinfo;

    @FXML
    private File file;

    @FXML
    private String path;

    @FXML
    private File pathfile;

    @FXML
    public Button Browser;


    @FXML
    private Button Save;


    @FXML
    private TextArea textArea;

    @FXML
    private final Desktop desktop = Desktop.getDesktop();


   @FXML
    private Image image;

    @FXML
    public TextField Search;

    @FXML
    public Label count;


    @FXML
    public Button btnupdate;

    private Path way;


    @FXML
    public Button dltbtn;

    @FXML
    public Button btnclear;
    @FXML
    public Button btnadd;

    @FXML
    public TextField fnamev;
    @FXML
    public TextField lnamev;

    @FXML
    public TextField sexv;
    @FXML
    public TextField salaryv;

    @FXML
    public TextField birthdatev;

   // @FXML
          //  public DatePicker dp;


    @FXML
    RadioButton female;
    @FXML
    RadioButton male;

    @FXML
    ToggleGroup Gender;





    String filename;




    //btnclear
    private void clearFields() {
        salaryv.setText(null);
        birthdatev.setText(null);
        lnamev.setText(null);
        fnamev.setText(null);
        sexv.setText(null);

        Browser.setDisable(true);
        imageView.setImage(null);

        textArea.setText("");

        female.setSelected(false);
        male.setSelected(false);
    }





    public void btnclear(ActionEvent event) {
        clearFields();

    }

    public void btnupdate(ActionEvent event){


        if (file!=null&&path==null) {

           file = new File(file.getAbsolutePath() + "");

            Image image = new Image(file.toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setCache(true);

            imageView.setImage(image);
        }





        Connection connection = getConnection();


        String query = "update employees set first_name='"+fnamev.getText()+"', last_name='"+lnamev.getText()+"', birth_day='"+birthdatev.getText()+"', sex='"+sexv.getText()+"', salary='"+salaryv.getText()+"', image=? ,filepath=? where emp_id=?";

        PreparedStatement stp;
        Statement st;
         int rs;
        try {
            ModelTable modelTable = (ModelTable) tableView.getSelectionModel().getSelectedItem();
            Integer emp_id = modelTable.getEmp_id();

            stp = connection.prepareStatement(query);

            if (path != null) {

                fis = new FileInputStream(new File(path + ""));
                file = new File(path + "");
                stp.setBinaryStream(1, (InputStream) fis, (int) file.length());
            } else if (file != null) {
                file = new File(file.getAbsolutePath() + "");
                fis = new FileInputStream(file);
                stp.setBinaryStream(1, (InputStream) fis, (int) file.length());
            }


            stp.setString(2, file.getAbsolutePath() + "");

            stp.setInt(3, emp_id);


            rs = stp.executeUpdate();

            btnupdate.setDisable(true);

            NotificationType notificationType = NotificationType.SUCCESS;
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setTitle("עדכון משתמש");
            trayNotification.setMessage("עדכון משתמש התבצע בהצלחה!");
            trayNotification.showAndDismiss(Duration.seconds(1));
            trayNotification.setNotificationType(notificationType);


            stp.close();

        }


        catch (SQLDataException e1){

            e1.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("פעולה לא חוקית!");
            alert.setContentText("שים לב הקלט שהזנת ארוך מידי או קצר מידי לשדה הנבחר!!");
            alert.show();

        }


        catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("פעולה לא חוקית!");
            alert.setContentText("שים לב שאחרי כל עדכון משתמש יש ללחוץ על המשתמש! ");
            alert.show();

        }

        Update_table();
       // clearFields();

    }


    @FXML
    private void btnadd(ActionEvent event) throws SQLException {

        if (path!=null) {

            file = new File(path + "");

            Image image = new Image(file.toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setCache(true);

            imageView.setImage(image);

        }else if(file.toString()!=path){



            Image image = new Image(file.toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setCache(true);

            imageView.setImage(image);

        }




        Connection connection = getConnection();


        String query = "INSERT INTO employees(first_name, last_name, birth_day, sex, salary, image, filepath) values(?,?,?,?,?,?,?)";
        int rs;
        PreparedStatement st = connection.prepareStatement(query);


        try {



            st.setString(1 ,fnamev.getText());
            st.setString(2,lnamev.getText());
            st.setString(3,birthdatev.getText());
            st.setString(4,sexv.getText());
            st.setString(5,salaryv.getText());



            if (path !=null) {
                fis = new FileInputStream(new File(path + ""));
                st.setBinaryStream(6, (InputStream) fis, (int) file.length());
            }else if(file.toString()!=path){
                fis = new FileInputStream(file);
                st.setBinaryStream(6, (InputStream) fis, (int) file.length());

            }


            st.setString(7,file.getAbsolutePath());

            st.executeUpdate();


            //select from table statment.
            Update_table();
            clearFields();
            btnupdate.setDisable(true);
            btnadd.setDisable(true);

            NotificationType notificationType = NotificationType.SUCCESS;
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setTitle("הוספת משתמש");
            trayNotification.setMessage("הוספת משתשמש התבצעה בהצלחה!");
            trayNotification.showAndDismiss(Duration.seconds(1));
            trayNotification.setNotificationType(notificationType);


           st.close();


        } catch (SQLDataException e1){

            e1.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("פעולה לא חוקית!");
            alert.setContentText("שים לב הקלט שהזנת ארוך מידי או קצר מידי לשדה הנבחר!!");
            alert.show();

        } catch (Exception e ) {


                e.printStackTrace();
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("פעולה לא חוקית!");
                alert.setContentText("שים לב שאחרי כל עדכון משתמש יש ללחוץ על המשתמש!");
                alert.show();



        }
    }
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

@FXML
private void setFnamev(KeyEvent event){

    if (fnamev.getText().isEmpty()||lnamev.getText().isEmpty()||birthdatev.getText().isEmpty()||sexv.getText().isEmpty()||salaryv.getText().isEmpty()){
        Browser.setDisable(true);
    }else {
        Browser.setDisable(false);
    }

}



    @Override
    public void initialize(URL location, ResourceBundle resources) {



       Browser.setDisable(true);

       btnadd.setDisable(true);
       btnupdate.setDisable(true);



        fileChooser = new FileChooser();

                fileChooser.getExtensionFilters().addAll(


                new ExtensionFilter("Text files", "*txt"),
                new ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg","*.gif"),
                new ExtensionFilter("Audio Files","*.wav","*.mp3","*.aac","*.mp4"),
                new ExtensionFilter("All Files","*.*")

        );









       imageView.setOnMouseClicked(event -> {

        if (event.getClickCount()==2) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File(path + ""));
            } catch (IllegalArgumentException | IOException e) {

                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setHeaderText("לא נמצאה תמונה!");
                   alert.setContentText("וודא שנתיב התמונה קיים ולא שונה או נמחק!");
                   alert.show();

            }
        }

       });




        showimageinfo.setOnAction(event -> {

            file = new File(path + "");

                  way = Paths.get(file.getAbsolutePath());
            try {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(way, BasicFileAttributes.class);
                imageView1 = new ImageView();
                Image image1 = new Image(file.toURI().toString());
                imageView1.setCache(true);

                imageView1.setFitWidth(150);
                imageView1.setFitHeight(150);
                imageView1.setImage(image1);



                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("פרטי תמונה:");
                alert.setContentText("Size: '"+basicFileAttributes.size()/1000000.0+"'(MB)\n" +
                        "Type: '"+file.getName()+"'\n" +
                        "Resolution: '"+image1.getWidth()+"'x'"+image1.getHeight()+"'\n" +
                        "Creation Time: '"+basicFileAttributes.creationTime()+"'\n" +
                        "Last Access Time: '"+basicFileAttributes.lastAccessTime()+"'\n" +
                        "Last Modified: '"+basicFileAttributes.lastModifiedTime()+"'");
                alert.setGraphic(imageView1);
                alert.setTitle("מידע");
                alert.show();







            } catch (IOException e) {
                e.printStackTrace();
            }


        });




Save.setOnAction(event -> {
  file = new File(path + "");
    Desktop desktop = Desktop.getDesktop();
    try {
        desktop.open(file.getParentFile());
    } catch (IllegalArgumentException | java.io.IOException IOException ) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("תיקייה לא קיימת!");
        alert.setContentText("וודא שתיקיית התמונה קיימת ולא שונתה או נמחקה!");
        alert.show();
    }
});





        female.setOnAction(event -> {

            sexv.setText("Female");
            female.setToggleGroup(Gender);
        });

        male.setOnAction(event -> {

            sexv.setText("Male");
            male.setToggleGroup(Gender);
        });











        Browser.setOnAction(e ->{


            file = fileChooser.showOpenDialog(null);




            if(file!=null){

                btnadd.setDisable(false);
                btnupdate.setDisable(false);

                textArea.setText(file.getAbsolutePath());

                file = new File(file.getAbsolutePath() + "");

                Image image = new Image(file.toURI().toString(),imageView.getFitWidth(), imageView.getFitHeight(), true, true);
                imageView.setCache(true);
                imageView.setImage(image);

                path = null;

            }

           /*
           java.util.List<File> fileList = fileChooser.showOpenMultipleDialog(null);
            if(fileList!=null){
                fileList.stream().forEach(selectedFiles -> {
                    textArea.setText(fileList.toString());




                });
            }


*/
        });



        //tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select * from employees");
            while (rs.next()) {
                oblist.add(new ModelTable(rs.getString("first_name"), rs.getString("last_name"), rs.getString("birth_day"), rs.getString("sex"), rs.getInt("salary"), rs.getBinaryStream("image"),rs.getString("filepath") ,rs.getInt("emp_id")));


           }







        } catch (SQLException Ex) {
            Ex.printStackTrace();
            //Logger.getLogger(Controller.class.getName().log(Level.SEVERE,null,Ex));

        }


            col_fnamev.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            col_lnamev.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            col_birthdatev.setCellValueFactory(new PropertyValueFactory<>("birth_day"));
            col_sexv.setCellValueFactory(new PropertyValueFactory<>("sex"));
            col_salaryv.setCellValueFactory(new PropertyValueFactory<>("salary"));
            col_imagev.setCellValueFactory(new PropertyValueFactory<>("image"));

            tableView.setItems(oblist);
            Count_Table();
            fromtabletotextfield();
    }

    private void Update_table() {

        oblist.clear();
        try {
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select * from employees");
            while (rs.next()) {
                oblist.add(new ModelTable(rs.getString("first_name"), rs.getString("last_name"), rs.getString("birth_day"), rs.getString("sex"), rs.getInt("salary"), rs.getBinaryStream("image"),rs.getString("filepath"), rs.getInt("emp_id")));
            }
              rs.close();
        } catch (SQLException Ex) {
            Ex.printStackTrace();
           //Logger.getLogger(Controller.class.getName().log(Level.SEVERE,null,Ex));

        }

        tableView.setItems(oblist);
        Count_Table();
    }

    private void Count_Table() {

        //oblist.clear();
        try {
            Connection connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(emp_id) AS count FROM employees");

            String num;
            while (rs.next()) {
                num =(rs.getString(1));

                count.setText("Number Of Users: "+num);
                //  oblist.add(new ModelTable(rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birth_day"), rs.getString("sex"), rs.getInt("salary"),rs.getInt("emp_id")));
            }
               rs.close();
        } catch (SQLException Ex) {
            Ex.printStackTrace();
            //Logger.getLogger(Controller.class.getName().log(Level.SEVERE,null,Ex));

        }

        //tableView.setItems(oblist);



    }




  //  @FXML
   // private void dltbtn(ActionEvent event) {
   //         deleteacount();

   // }



@FXML
private void deleteacount(ActionEvent event) {

        Connection connection = getConnection();



        try {

            ObservableList<ModelTable> model = (ObservableList<ModelTable>) tableView.getSelectionModel().getSelectedItems();


           ModelTable modelTable = (ModelTable) tableView.getSelectionModel().getSelectedItem();

           System.out.println(model);

          Integer g =  modelTable.getEmp_id();


           //Array b = [];
            //model.addAll();
            //System.out.println(modelTable);



            String query = "delete from employees where emp_id in (?)";
            PreparedStatement st;
            int rs;
            st = connection.prepareStatement(query);
            st.setInt(1, g);


            rs = st.executeUpdate();

            Update_table();

            imageView.setImage(null);

            textArea.setText("");
            NotificationType notificationType = NotificationType.SUCCESS;
            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setTitle("מחיקת משתמש");
            trayNotification.setMessage("מחיקת משתמש התבצעה בהצלחה!");
            trayNotification.showAndDismiss(Duration.seconds(1));
            trayNotification.setNotificationType(notificationType);

              st.close();

        } catch (Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("פעולה לא חוקית!");
        alert.setContentText("לא נבחר משתמש!");
        alert.show();


    }




}

    private void fromtabletotextfield() {
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                int mod=2;
                if(event.getClickCount()== 2) {
                    ModelTable modelTable = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    fnamev.setText(modelTable.getFirst_name());
                    lnamev.setText(modelTable.getLast_name());
                    birthdatev.setText(modelTable.getBirth_day().toString());
                    sexv.setText(modelTable.getSex());
                    salaryv.setText(String.valueOf(modelTable.getSalary()));
                    Browser.setDisable(false);

                    btnupdate.setDisable(false);
                    btnadd.setDisable(false);



                    showimage(modelTable.getEmp_id());
                    showfilepath(modelTable.getEmp_id());


                    if (sexv.getText().equals("Female")){

                        female.setSelected(true);
                    }else if (sexv.getText().equals("Male")){
                        male.setSelected(true);

                    }



                    //mod++;
                }
                else if(event.getClickCount()==1){
                   // ObservableList<ModelTable> Model = tableView.getSelectionModel().getSelectedItems();

                    ModelTable modelTable = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

                    showimage(modelTable.getEmp_id());
                    showfilepath(modelTable.getEmp_id());


                    btnupdate.setDisable(true);
                    btnadd.setDisable(true);

                   fnamev.setText("");
                   lnamev.setText("");

                   salaryv.setText("");
                   sexv.setText("");
                   birthdatev.setText("");

                   Browser.setDisable(true);

                    female.setSelected(false);
                    male.setSelected(false);






                }

            }

            private void showimage(Integer emp_id) {

                try {
                    stp = getConnection().prepareStatement("select image from employees where emp_id=?");

                    stp.setString(1, emp_id.toString());
                    ResultSet rs = stp.executeQuery();

                    if(rs.next()){
                        is = rs.getBinaryStream(1);
                        os = new FileOutputStream(new File("photo.jpg"));

                        byte[] contents = new byte[1024];


                        int size = 0;


                        while ((size = is.read(contents))!=-1){
                            os.write(contents, 0, size);
                        }



                        image = new Image("file:photo.jpg", imageView.getFitWidth(), imageView.getFitHeight(), true, true);
                        imageView.setCache(true);
                        imageView.setImage(image);




                    }






                   stp.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }


               private void showfilepath(Integer emp_id){

                   try {
                       stp = getConnection().prepareStatement("select filepath from employees where emp_id=?");

                       stp.setString(1, emp_id.toString());
                       ResultSet rs = stp.executeQuery();

                       if (rs.next()) {
                           path = rs.getString(1);
                           textArea.setText(path + "");




                             //  image = new Image(file.toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);      imageView.setCache(true);
                              // imageView.setImage(image);
                           }





                          stp.close();
                       } catch (SQLException e) {
                       e.printStackTrace();
                   }

                   //   textArea.setText("");

                         //  }


                     //  if (file == null&& path!=null) {
                        //   file = new File(path);

                       //    image = new Image(file.toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
                       //    imageView.setCache(true);
                       //    imageView.setImage(image);
                      // }

                       }




        });
    }


@FXML
    FilteredList<ModelTable>filteredList = new FilteredList<>(oblist, e -> true);
     public void Search(KeyEvent event){

        Search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super ModelTable>) modelTableFilteredList -> {
                if (newValue == null || newValue.isEmpty()) {

                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (modelTableFilteredList.getEmp_id().toString().contains(newValue)) {
                    return true;

                } else if (modelTableFilteredList.getFirst_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (modelTableFilteredList.getLast_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;

            });
            SortedList<ModelTable> modelTableSortedList = new SortedList<>(filteredList);
            modelTableSortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(modelTableSortedList);
        }));



     }






}