package example.model;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "empno")
    private int empno;

    @Column(name = "ename")
    private String ename;

    @Column(name = "job")
    private String job;

    @ManyToOne
    @JoinColumn(name = "deptno")
    private Dept dept;

    public Employee() {
    }

    public Employee(int empno, String ename, String job, Dept dept) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.dept = dept;
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
