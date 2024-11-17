package example.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dept")
public class Dept {
    @Id
    private int deptno;

    @Column(name = "dname")
    private String dname;

    @Column(name = "loc")
    private String loc;

    @OneToMany(mappedBy = "dept", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public Dept() {
    }
    public Dept(int deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
