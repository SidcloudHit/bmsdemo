/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

/**
 *
 * @author Nibedita
 */
public class MasterUserRoleUrl {
    private int mapping_id;
    //private int department_id;
      
    private int scheme_id;
    private int role_id;
    private int url_id;
    private int is_active;
    private int priority_no;
    //private int is_main_pg;

    public int getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(int mapping_id) {
        this.mapping_id = mapping_id;
    }

    
    public int getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(int scheme_id) {
        this.scheme_id = scheme_id;
    }

    
//    public int getDepartment_id() {
//        return department_id;
//    }
//
//    public void setDepartment_id(int department_id) {
//        this.department_id = department_id;
//    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getUrl_id() {
        return url_id;
    }

    public void setUrl_id(int url_id) {
        this.url_id = url_id;
    }

//    public int getIs_main_pg() {
//        return is_main_pg;
//    }
//
//    public void setIs_main_pg(int is_main_pg) {
//        this.is_main_pg = is_main_pg;
//    }

  
    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getPriority_no() {
        return priority_no;
    }

    public void setPriority_no(int priority_no) {
        this.priority_no = priority_no;
    }
    
}
