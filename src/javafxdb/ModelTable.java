package javafxdb;


import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;

import java.io.InputStream;
import java.sql.Date;

public class ModelTable{

    String first_name,last_name;

    String birth_day;

    String sex;
    int salary;
    Integer emp_id;
    InputStream image;
    String filepath;




    public ModelTable(String first_name, String last_name, String birth_day, String sex, int salary, InputStream image, String filepath, Integer emp_id){

        this.first_name = first_name;
        this.last_name = last_name;
        //this.birth_day= birth_day;
        this.birth_day = birth_day;
        this.sex = sex;
        this.salary = salary;
        this.emp_id=emp_id;
        this.image= image;
        this.filepath=filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(Integer emp_id) {
        this.emp_id = emp_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFname(String fnamev) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLname(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void btnclear(ActionEvent event) {

    }

    public void btnadd(ActionEvent event) {




    }


}
