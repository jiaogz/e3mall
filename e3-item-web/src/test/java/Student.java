public class Student {

    private int id;
    private String name;
    private int age;
    private String adrre;

    public Student(){}

    public Student(int id, String name, int age, String adrre) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.adrre = adrre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAdrre() {
        return adrre;
    }

    public void setAdrre(String adrre) {
        this.adrre = adrre;
    }
}
