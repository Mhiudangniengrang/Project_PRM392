package com.example.project_prm.Model;


public class SliderModel {

    private String url;

    // Constructor không đối số, với giá trị mặc định là chuỗi rỗng
    public SliderModel() {
        this.url = "";
    }

    // Constructor có đối số
    public SliderModel(String url) {
        this.url = url;
    }

    // Getter và Setter
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Override phương thức toString() (nếu cần, tương tự như data class Kotlin)
    @Override
    public String toString() {
        return "SliderModel{" +
                "url='" + url + '\'' +
                '}';
    }
}
