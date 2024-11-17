package example;

import example.model.Employee;
import example.model.Dept;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static SessionFactory sessionFactory = null;
    static Session session = null;

    public static void main(String[] args) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.hibernate");
        logger.setLevel(Level.SEVERE);

        boolean stop = false;
        boolean isEmployee = false;

        //open connection
        openCon();

        while(!stop) {

            int operation = showMenu();
            if(operation != 5) {
                isEmployee = isEmployee();
            }

            switch (operation) {
                case 1:
                    //create
                    if(isEmployee){
                        setAndCreateEmployee();
                    }else{
                        Dept dept = setDept();
                        createDept(dept);
                    }
                    break;
                case 2:
                    //read
                    readTables(isEmployee);
                    break;
                case 3:
                    //update
                    if(isEmployee){
                        String num = askForNumEmployee();
                        int opUpdate = askUpdateEmployee();
                        if(num.equals("")){
                            System.out.println("Employee couldn't be found");
                        }else if (opUpdate == 3){
                            System.out.println("Operation aborted");
                        }else{
                            Employee emp = getEmployeeFromNum(Integer.parseInt(num));
                            String newVal = newVal(opUpdate).toUpperCase();

                            switch(opUpdate){
                                case 1:
                                    emp.setEname(newVal);
                                    break;
                                case 2:
                                    emp.setJob(newVal);
                                    break;
                            }

                            updateEmployee(emp);
                        }
                    }else{
                        String deptName = checkNameAndDeptExists().toUpperCase();
                        int opUpdate = askUpdateDept();
                        if(deptName.equals("")){
                            System.out.println("Department couldn't be found");
                        }else if (opUpdate == 3){
                            System.out.println("Operation aborted");
                        }else{
                            Dept dept = getDeptFromName(deptName);
                            String newVal = newVal(opUpdate).toUpperCase();
                            switch(opUpdate){
                                case 1:
                                    dept.setDname(newVal);
                                    break;
                                case 2:
                                    dept.setLoc(newVal);
                                    break;
                            }
                            updateDept(dept);
                        }
                    }
                    break;
                case 4:
                    //delete
                    if(isEmployee){
                        String numEmployee = checkEmployeeDelete();
                        if(confirmation(true, numEmployee)){{
                            deleteEmployee(Integer.parseInt(numEmployee));
                        }}
                    }else{
                        String toDelete = checkNameAndDeptExists();
                        if(confirmation(false, toDelete)){
                            //delete the employees on the department
                            deleteListEmployees(getEmployeesFromDeptName(toDelete));
                            //delete dept
                            Dept dept = getDeptFromName(toDelete);
                            deleteDept(dept);
                        }
                    }
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    stop = true;
                    closeCon();
                    break;
            }

            String shouldExit = "";
            while (!shouldExit.equalsIgnoreCase("y") && !shouldExit.equalsIgnoreCase("n") && operation !=5) {
                System.out.println("Would you like to stop? (y/n)");
                shouldExit = scanner.nextLine();
            }
            if (shouldExit.equalsIgnoreCase("y")) {
                System.out.println("Goodbye!");
                closeCon();
                stop = true;
            }
        }

    }
    public static int showMenu(){
        String operation = "";
        while(true){
            System.out.println("1 - Add a new employee or department");
            System.out.println("2 - Show all employees or all the departments and its employees");
            System.out.println("3 - Modify an existent employee or department");
            System.out.println("4 - Delete an employee or department");
            System.out.println("5 - Exit");

            operation = scanner.nextLine();

            if (!operation.trim().isEmpty()) {
                try{
                    int intOpt = Integer.parseInt(operation);
                    if(intOpt >0 && intOpt<6){
                        break;
                    }
                }catch (NumberFormatException e){
                    //
                }
            }
            System.out.println("Please write a valid operation");
        }
        return Integer.parseInt(operation);
    }

    public static boolean isEmployee(){
        boolean isEmployee = false;
        String answer = "";
        while(true){
            System.out.println("In which table would you like to operate? Enter 'e' for Employee or 'd' for Departments:");
            answer = scanner.nextLine();

            if (!answer.trim().isEmpty()) {

                if(answer.equalsIgnoreCase("e")){
                    isEmployee = true;
                    break;
                }else if(answer.equalsIgnoreCase("d")){
                    isEmployee = false;
                    break;
                }
            }
            System.out.println("Please write a valid operation");
        }
        return isEmployee;
    }

    public static void openCon(){
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(Dept.class)
                    .buildSessionFactory();

            session = sessionFactory.openSession();

            if (session != null) {
                System.out.println("Session successfully opened!");
            } else {
                System.out.println("Error opening session!");
            }
        } catch (Exception e) {
            System.err.println("Error initializing Hibernate: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void closeCon(){
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createEmployee(Employee employee){
        Transaction transaction  = null;

        try {
            transaction = session.beginTransaction();
            session.save(employee);

            transaction.commit();
            System.out.println("Employee created successfully");
        }catch(Exception e){
            transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public static void createDept(Dept dept) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.save(dept);
            transaction.commit();

            System.out.println("Department created successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Error creating department: " + e.getMessage());
            }
        }
    }

    public static boolean checkIfDeptExists(String name) {
        try {
            String query = "SELECT d FROM Dept d WHERE d.dname = :name";

            Dept department = session.createQuery(query, Dept.class)
                    .setParameter("name", name.toUpperCase())
                    .uniqueResult();

            return department != null;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean checkIfEmployeeExists(String num){
        try {
            String query = "SELECT e FROM Employee e WHERE e.empno = :empno";

            Employee employee = session.createQuery(query, Employee.class)
                    .setParameter("empno", Integer.parseInt(num))
                    .uniqueResult();

            return employee != null;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static Employee setAndCreateEmployee(){
        String number = "";
        String name = "";
        String job = "";
        String deptName = "";
        Employee newEmp = null;
        Dept dept = null;
        String location = "";

        while(true){
            System.out.println("Please, write the number of the new employee:");
            number = scanner.nextLine();

            if (!number.trim().isEmpty()) {
                try{
                    int intOpt = Integer.parseInt(number);
                    if(checkIfEmployeeExists(number)){
                        System.out.println("This employee number is already in use, please, use another");
                    }else{
                        break;
                    }
                }catch (NumberFormatException e){
                    //
                }
            }
            System.out.println("Please, enter a valid number");
        }

        while(true){
            System.out.println("Please, enter the name of the new employee");
            name = scanner.nextLine();

            if (!name.trim().isEmpty()) {
                if(name.length()>10){
                    System.out.println("This name is using more than the allowed characters (10) for a name, " +
                            "please enter another name. Your name has "+name.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }

        while(true){
            System.out.println("Please, enter the job of the new employee");
            job = scanner.nextLine();

            if (!job.trim().isEmpty()) {
                if(job.length()>10){
                    System.out.println("This job is using more than the allowed characters (9) for a job, " +
                            "please correct the job. Your job has "+job.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid job");
            }
        }

        while(true){
            System.out.println("Please, enter the name of the department of the new employee: ");
            deptName = scanner.nextLine();

            if (!deptName.trim().isEmpty()) {
                if(deptName.length()>14){
                    System.out.println("This department name is using more than the allowed characters (14) for a department, " +
                            "please correct the department. Yours has "+job.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid department");
            }
        }

        if(!checkIfDeptExists(deptName)){
            String shouldCreate = "";
            while (!shouldCreate.equalsIgnoreCase("y") && !shouldCreate.equalsIgnoreCase("n")) {
                System.out.println("This department does not exist, would you like to create it? (y/n). If you don't want to, this process will be aborted");
                shouldCreate = scanner.nextLine();
            }
            if (shouldCreate.equalsIgnoreCase("n")) {
                return newEmp;
            }

            //insert and save on the dept class

            //asking for location:
            while(true){
                System.out.println("Please, enter the location of the new department:");
                location = scanner.nextLine();

                if (!location.trim().isEmpty()) {
                    if(location.length()>13){
                        System.out.println("This location is using more than the allowed characters (13) for a location, " +
                                "please correct the location. Your location has "+location.length()+" characters");
                    }else{
                        break;
                    }
                }else{
                    System.out.println("Please, enter a valid location");
                }
            }
            dept = new Dept(nextDeptNo(), deptName.toUpperCase(), location.toUpperCase());
            createDept(dept);
        }else{
            //recover with select
            dept = getDeptFromName(deptName.toUpperCase());
        }

        newEmp = new Employee(Integer.parseInt(number), name.toUpperCase(), job.toUpperCase(), dept);
        createEmployee(newEmp);

        return newEmp;
    }

    public static Dept setDept(){
        String deptName = "";
        String location = "";

        //dept name
        while(true){
            System.out.println("Please, write the name of the new department: ");
            deptName = scanner.nextLine();

            if (!deptName.trim().isEmpty()) {
                if(deptName.length()>14){
                    System.out.println("This name is using more than the allowed characters (14) for a department name, " +
                            "please enter another one. Your department name has "+deptName.length()+" characters");
                }else{
                    if(checkIfDeptExists(deptName)){
                        System.out.println("This name is already in use, please, enter another");
                    }else{
                        break;
                    }
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }

        //location
        while(true){
            System.out.println("Please, write the location of the new department");
            location = scanner.nextLine();

            if (!location.trim().isEmpty()) {
                if(location.length()>13){
                    System.out.println("This location is using more than the allowed characters (13), " +
                            "please correct it. Your department name has "+location.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid location");
            }
        }

        Dept dept = new Dept(nextDeptNo(), deptName.toUpperCase(), location.toUpperCase());

        return dept;
    }

    public static int nextDeptNo() {
        int res = 10;
        try {
            Query<Integer> query = session.createQuery("SELECT d.deptno FROM Dept d ORDER BY d.deptno DESC", Integer.class);
            query.setMaxResults(1);

            Integer lastDeptno = query.uniqueResult();

            if (lastDeptno != null) {
                res = lastDeptno + 10;
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static Dept getDeptFromName(String name){
        Dept dept = null;
        try {
            session.beginTransaction();
            Query<Dept> query = session.createQuery("from Dept d where d.dname = :name", Dept.class);
            query.setParameter("name", name.toUpperCase());
            dept = query.uniqueResult();

            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return dept;
    }

    public static List<Employee> getEmployeesFromDeptName(String deptName){
        List<Employee> employees = null;
        try {
            session.beginTransaction();

            String hql = "select e from Employee e where e.dept.deptno = (select d.deptno from Dept d where d.dname = :deptname)";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("deptname", deptName.toUpperCase());

            employees = query.getResultList();

            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public static void readTables(boolean isEmployee){
        session.beginTransaction();

        if(isEmployee){
            try {
                Query<Employee> query = session.createQuery("from Employee", Employee.class);
                List<Employee> employees = query.getResultList();

                for (Employee employee : employees) {
                    System.out.println(
                            "Number: " + employee.getEmpno() + " Name: " + employee.getEname() + " Job: " + employee.getJob() + " Department: " + employee.getDept());
                }
                session.getTransaction().commit();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else{
            try{
                String query = "SELECT DISTINCT d FROM Dept d LEFT JOIN d.employees";
                List<Dept> departments = session.createQuery(query, Dept.class).getResultList();

                for (Dept dept : departments) {
                    System.out.println("Department: " + dept.getDeptno() +", "+ dept.getDname());
                    if (dept.getEmployees() != null && !dept.getEmployees().isEmpty()) {
                        System.out.println("    Employees:");
                        for (Employee emp : dept.getEmployees()) {
                            System.out.println("      Employee nº "+emp.getEmpno()+", Name: "+emp.getEname()+" Job: "+emp.getJob());
                        }
                    } else {
                        System.out.println("    No employees assigned to this department.");
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteDept (Dept dept){
        try {
            session.beginTransaction();
            session.delete(dept);
            session.getTransaction().commit();
            System.out.println("Department successfully delete");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean confirmation(boolean isEmployee, String deptName){
        while(true){
            if(isEmployee) {
                System.out.println("Are you sure you want to delete this employee? (y/n)");
            }else{
                List<Employee> list = getEmployeesFromDeptName(deptName);
                if(list.size()>0){
                    System.out.println("Are you sure you want to delete this department? (y/n)\nYou'll also delete the following employees:");
                    for(Employee e : list){
                        System.out.println("Employee num: "+e.getEmpno()+", "+e.getEname()+" - "+e.getJob());
                    }
                }else{
                    System.out.println("Are you sure you want to delete this department? (y/n)\nNo employees are assigned to it yet");
                }
            }

            String resultado = scanner.nextLine();

            if(!resultado.equalsIgnoreCase("y") && !resultado.equalsIgnoreCase("n")){
                System.out.println("Please, enter a valid operation");
            }else{
                if(resultado.equalsIgnoreCase("y")){
                    return true;
                }else{
                    System.out.println("Operation aborted");
                    return false;
                }
            }

        }
    }

    public static String checkNameAndDeptExists (){
        String deptName = "";

        while(true){
            System.out.println("Please, write the name of the department you'd like to operate on: ");
            deptName = scanner.nextLine();

            if (!deptName.trim().isEmpty()) {
                if(deptName.length()>14){
                    System.out.println("This name is using more than the allowed characters (14) for a department name, " +
                            "please enter another one. Your department name has "+deptName.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }

        if(!checkIfDeptExists(deptName)){
            System.out.println("This department doesn't exist, operation aborted");
            return "";
        }
        return deptName;
    }

    public static void deleteListEmployees(List<Employee> employeesToDelete) {
        try {
            session.beginTransaction();

            for (Employee employee : employeesToDelete) {
                session.delete(employee);
            }

            session.getTransaction().commit();
            System.out.println("Employees successfully delete");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback in case of an error
            }
        }
    }

    public static Employee getEmployeeFromNum(int empno){
        Employee employee = null;
        try {
            session.beginTransaction();
            Query<Employee> query = session.createQuery("from Employee e where e.empno = :empno", Employee.class);
            query.setParameter("empno", empno);
            employee = query.uniqueResult();

            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return employee;
    }

    public static String checkEmployeeDelete (){
        String employee = "";

        while(true){
            System.out.println("Please, write the number of the employee you'd like to delete: ");
            employee = scanner.nextLine();

            if (!employee.trim().isEmpty()) {
                try{
                    int i = Integer.parseInt(employee);
                    break;
                }catch (NumberFormatException e){
                    System.out.println("Please, enter a valid name");
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }

        if(!checkIfEmployeeExists(employee)){
            System.out.println("This employee doesn't exist, operation aborted");
            return "";
        }
        return employee;
    }

    public static void deleteEmployee(int empno){
        Employee employee = getEmployeeFromNum(empno);
        try {
            session.beginTransaction();

            session.delete(employee);

            session.getTransaction().commit();
            System.out.println("Employee successfully delete");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback in case of an error
            }
        }
    }

    public static int askUpdateEmployee(){
        String finalUpdate = "";

        while(true){
            System.out.println("What would you like to modify from this employee? Please enter one option: ");
            System.out.println("1 - Name");
            System.out.println("2 - Job");
            System.out.println("3 - Exit");

            finalUpdate = scanner.nextLine();

            if(!finalUpdate.isEmpty()){
                try{
                    int i = Integer.parseInt(finalUpdate);
                    if(i>0 && i<=3){
                        return i;
                    }
                }catch(NumberFormatException e){
                    System.out.println("This is not a valid number, please, select one");
                }
            }
            System.out.println("This is not a valid operation, please, select one");
        }

    }

    public static String askForNumEmployee(){
        String ask = "";
        while(true){
            System.out.println("Please, write the number of the employee you'd like to update: ");
            ask = scanner.nextLine();

            if (!ask.trim().isEmpty()) {
                if(ask.length()>10){
                    System.out.println("This name is using more than the allowed characters (14) for a name, " +
                            "please enter another one. Your name has "+ask.length()+" characters");
                }else{

                    if(!checkIfEmployeeExists(ask)){
                        System.out.println("Employee with the number "+ask+" couldn't be found");
                    }else{
                        break;
                    }
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }

        return ask;
    }

    public static String newVal (int operation){
        String ask = "";
        int max = 0;
        switch(operation){
            case 1:
                max = 10;
                break;
            case 2:
                max = 9;
                break;
        }

        while(true){
            System.out.println("Please, type the new value you'd like to update: ");
            ask = scanner.nextLine();

            if (!ask.trim().isEmpty()) {
                if(ask.length()>max){
                    System.out.println("Your new value is using more than the allowed characters ("+max+") for that data. " +
                            "Your value has "+ask.length()+" characters");
                }else{
                    break;
                }
            }else{
                System.out.println("Please, enter a valid name");
            }
        }
        return ask;
    }

    public static void updateEmployee(Employee employee){
        try {

            session.beginTransaction();
            session.update(employee);
            System.out.println("Employee successfully updated");
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback in case of an error
            }
        }
    }

    public static int askUpdateDept(){
        String finalUpdate = "";
        while(true){
            System.out.println("What would you like to modify from this department? Please enter one option: ");
            System.out.println("1 - Name");
            System.out.println("2 - Location");
            System.out.println("3 - Exit");

            finalUpdate = scanner.nextLine();

            if(!finalUpdate.isEmpty()){
                try{
                    int i = Integer.parseInt(finalUpdate);
                    if(i>0 && i<=3){
                        return i;
                    }
                }catch(NumberFormatException e){
                    System.out.println("This is not a valid number, please, select one");
                }
            }
            System.out.println("This is not a valid operation, please, select one");
        }
    }

    public static void updateDept(Dept dept){
        try {

            session.beginTransaction();
            session.update(dept);
            System.out.println("Department successfully updated");
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback in case of an error
            }
        }
    }
}