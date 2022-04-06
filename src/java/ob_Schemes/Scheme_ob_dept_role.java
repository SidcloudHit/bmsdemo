/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class Scheme_ob_dept_role {

    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public int getIs_maker() {
        return is_maker;
    }

    public void setIs_maker(int is_maker) {
        this.is_maker = is_maker;
    }

    public int getIs_checker() {
        return is_checker;
    }

    public void setIs_checker(int is_checker) {
        this.is_checker = is_checker;
    }

    public int getIs_approver() {
        return is_approver;
    }

    public void setIs_approver(int is_approver) {
        this.is_approver = is_approver;
    }
    private int dept_id;
    private String dept_name;
    private int is_maker;
    private int is_checker; 
    private int is_approver;
    
    
}
