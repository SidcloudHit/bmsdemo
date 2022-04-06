/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.AjaxServlet;
import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "ManageAdminUser", urlPatterns = {"/ManageAdminUser"})
@MultipartConfig(maxFileSize = 4194304)	// upload file's size up to 4MB
public class ManageAdminUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            int edit = 0, editable = 0, user_role = 0, user_office = 0, dept = 0, district = 0;
            String user_id = "", gender = "", email = "", mobile = "", designation = "", user_password = "", hex_password = "",
                    altPhno = "", landmark = "", city = "", po = "", addrPIN = "", rcMemId = "", loginId = "";
            String ctypePhoto = "";
            String message = "", filePhotoName = "";
            InputStream isPhoto = null;
            long filePhotoSize = 0;
            int filePhotoFid = 0;
            Part filePart = null;
            CommonMethod cm = new CommonMethod();
            edit = Integer.parseInt(request.getParameter("hdnEdit"));

            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (edit == 1) {
                //this user's info getting edited
                user_id = request.getParameter("hdnUserId");
            }
            if (request.getParameter("txtUserName") != null) {
                if (request.getParameter("txtUserName").trim().length() > 0) {
                    userName = request.getParameter("txtUserName");
                    userName = SecurityClass.killchar(userName);
                } else {
                    message = "User Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "User Name can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (request.getParameter("txtEmail") != null) {
                if (request.getParameter("txtEmail").trim().length() > 0) {
                    if (cm.emailValidator(request.getParameter("txtEmail"))) {

                        if ((edit == 0) && (!cm.checkEmailExists(request.getParameter("txtEmail")))) {
                            email = request.getParameter("txtEmail");
                            email = SecurityClass.killchar(email);
                        } else if (edit == 1) {
                            if (cm.checkEmailExists(user_id, request.getParameter("txtEmail"))) {
                                email = request.getParameter("txtEmail");
                                email = SecurityClass.killchar(email);
                            } else {
                                if (!cm.checkEmailExists(request.getParameter("txtEmail")) && cm.emailValidator(request.getParameter("txtEmail"))) {
                                    email = request.getParameter("txtEmail");
                                    email = SecurityClass.killchar(email);
                                } else {
                                    message = "Either Email ID already exists or Email ID is not valid.";
                                    session.setAttribute("RegMsg", message);
                                    session.setAttribute("type", "danger");
                                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                                    return;
                                }
                            }

                        } else {
                            message = "Email ID already exists.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("Admin/manageAdminUsers.jsp");
                            return;
                        }

                    } else {
                        message = "Email ID is not valid.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageAdminUsers.jsp");
                        return;
                    }
                } else {
//                    message = "Email ID can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "Email ID can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }
            if (request.getParameter("txtMobile") != null) {
                if (request.getParameter("txtMobile").trim().length() > 0) {
                    if (cm.mobileValidator(request.getParameter("txtMobile"))) {
                        if ((edit == 0) && (!cm.checkMobileExists(request.getParameter("txtMobile")))) {
                            mobile = request.getParameter("txtMobile");
                            mobile = SecurityClass.killchar(mobile);
                        } else if (edit == 1) {
                            if (cm.checkMobileExists(user_id, request.getParameter("txtMobile"))) {
                                mobile = request.getParameter("txtMobile");
                                mobile = SecurityClass.killchar(mobile);
                            } else {
                                if (!cm.checkMobileExists(request.getParameter("txtMobile")) && cm.mobileValidator(request.getParameter("txtMobile"))) {
                                    mobile = request.getParameter("txtMobile");
                                    mobile = SecurityClass.killchar(mobile);
                                } else {
                                    message = "Either Mobile Number already exists or Mobile Number is not valid.";
                                    session.setAttribute("RegMsg", message);
                                    session.setAttribute("type", "danger");
                                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                                    return;
                                }

                            }

                        } else {
                            // email = existing mobile;
                            message = "Mobile Number already exists.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("Admin/manageAdminUsers.jsp");
                            return;
                        }
                    } else {
                        // mobile = invalid mobile;
                        message = "Mobile Number is not valid.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageAdminUsers.jsp");
                        return;
                    }
                } else {
                    // mobile = "";
//                    message = "Mobile Number can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "Mobile Number can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("radGender") != null) {
                if (request.getParameter("radGender").trim().length() > 0) {
                    gender = request.getParameter("radGender");
                    gender = SecurityClass.killchar(gender);
                }
//                else {
//                    // user_name = "";
//                    message = "Gender can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
//                }
            }
//            else {
//                message = "Gender can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("txtDesignation") != null) {
                if (request.getParameter("txtDesignation").trim().length() > 0) {
                    designation = request.getParameter("txtDesignation");
                    designation = SecurityClass.killchar(designation);
                } else {
                    message = "User Designation can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "User Designation can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (request.getParameter("txtUserLoginId") != null) {
                if (request.getParameter("txtUserLoginId").trim().length() > 0) {
                    if (cm.loginIdValidator(request.getParameter("txtUserLoginId"))) {
                        if ((edit == 0) && (!cm.checkLoginIdExists(request.getParameter("txtUserLoginId")))) {
                            loginId = request.getParameter("txtUserLoginId");
                            loginId = SecurityClass.killchar(loginId);
                        } else if (edit == 1) {
                            if (cm.checkLoginIdExists(user_id, request.getParameter("txtMobile"))) {
                                loginId = request.getParameter("txtUserLoginId");
                                loginId = SecurityClass.killchar(loginId);
                            } else {
                                if (!cm.checkLoginIdExists(request.getParameter("txtMobile")) && cm.loginIdValidator(request.getParameter("txtMobile"))) {
                                    loginId = request.getParameter("txtUserLoginId");
                                    loginId = SecurityClass.killchar(loginId);
                                } else {
                                    message = "Either Mobile Number already exists or Mobile Number is not valid.";
                                    session.setAttribute("RegMsg", message);
                                    session.setAttribute("type", "danger");
                                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                                    return;
                                }

                            }
                        } else {
                            // email = existing mobile;
                            message = "User Login ID already exists.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("Admin/manageAdminUsers.jsp");
                            return;
                        }
                    } else {
                        // mobile = invalid mobile;
                        message = "User Login ID is not valid.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageAdminUsers.jsp");
                        return;
                    }
                } else {
                    message = "User Login ID can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "User Login ID can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (request.getParameter("txtAltMobile") != null) {
                if (request.getParameter("txtAltMobile").trim().length() > 0) {
                    altPhno = request.getParameter("txtAltMobile");
                    altPhno = SecurityClass.killchar(altPhno);
                }
            }

            if (request.getParameter("ddlDepartment") != null) {
                if (request.getParameter("ddlDepartment").trim().length() > 0) {
                    dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));

                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "Department can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (request.getParameter("ddlRole") != null) {
                if (request.getParameter("ddlRole").trim().length() > 0) {
                    user_role = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlRole")));
                } else {
                    message = "User Role can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "User Role can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (request.getParameter("ddlOffice") != null) {
                if (request.getParameter("ddlOffice").trim().length() > 0) {
                    user_office = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlOffice")));
                } else {
                    message = "User Office can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }
            } else {
                message = "User Office can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            /*if (request.getParameter("txtUserPwd") != null) {
                if (request.getParameter("txtUserPwd").trim().length() > 0) {
                    user_password = request.getParameter("txtUserPwd");
                    user_password = SecurityClass.killchar(user_password);
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp.jsp");
                    return;
                }
            } else {
                message = "Password can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp.jsp");
                return;
            }*/
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                int myInt = sr.nextInt(1000000);
                user_password = String.format("%06d", myInt);

            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (request.getParameter("txtAddrLine") != null) {
                if (request.getParameter("txtAddrLine").trim().length() > 0) {
                    landmark = request.getParameter("txtAddrLine");
                    landmark = SecurityClass.killchar(landmark);
                } else {
//                    message = "House No., Road, Landmark etc.. can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "House No., Road, Landmark etc.. can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("txtAddrCity") != null) {
                if (request.getParameter("txtAddrCity").trim().length() > 0) {
                    city = request.getParameter("txtAddrCity");
                    city = SecurityClass.killchar(city);
                } else {
//                    message = "City, Town, Village etc.. can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "City, Town, Village etc.. can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("txtAddrPO") != null) {
                if (request.getParameter("txtAddrPO").trim().length() > 0) {
                    po = request.getParameter("txtAddrPO");
                    po = SecurityClass.killchar(po);
                } else {
//                    message = "Post Office can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "Post Office can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("ddlDistrict") != null) {
                if (request.getParameter("ddlDistrict").trim().length() > 0) {
                    district = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDistrict")));
                } else {
//                    message = "District can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "District can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("txtAddrPIN") != null) {
                if (request.getParameter("txtAddrPIN").trim().length() > 0) {
                    if (cm.PINValidator(request.getParameter("txtAddrPIN"))) {
                        addrPIN = request.getParameter("txtAddrPIN");
                        addrPIN = SecurityClass.killchar(addrPIN);
                    } else {
                        // email = invalid email;
//                        message = "PIN is not valid.";
//                        session.setAttribute("RegMsg", message);
//                        session.setAttribute("type", "danger");
//                        response.sendRedirect("Admin/manageAdminUsers.jsp");
//                        return;
                    }
                } else {
                    // user_name = "";
//                    message = "PIN can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "PIN can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

            if (request.getParameter("txtRcMem") != null) {
                if (request.getParameter("txtRcMem").trim().length() > 0) {
                    rcMemId = request.getParameter("txtRcMem");
                    rcMemId = SecurityClass.killchar(rcMemId);

                } else {
                    // user_name = "";
//                    message = "PIN can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }

            // Photo File
            try {
                filePart = null;
                filePart = request.getPart("filePhoto");
            } catch (Exception ex) {
                ex.printStackTrace();
                message = "Photo upload error! Please try again";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp");
                return;
            }

            if (filePart != null) {
                ctypePhoto = filePart.getContentType();
                filePhotoSize = filePart.getSize();
                filePhotoName = filePart.getSubmittedFileName();
                if (filePhotoSize > 0) {
                    if (!(ctypePhoto.equalsIgnoreCase("image/jpeg") || ctypePhoto.equalsIgnoreCase("image/png"))) {
                        message = "Please Select a JPEG or PNG file for Photo.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageAdminUsers.jsp");
                        return;
                    }
                    if ((filePhotoSize / 1024) > 250) { // filesize in kb
                        message = "Size of Photo file is more than 250 Kb, Please use smaller size.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageAdminUsers.jsp");
                        return;
                    }
                    // obtains input stream of the upload file
                    isPhoto = filePart.getInputStream();
                }
            }

//            if (edit == 0 && isPhoto == null) {
//                message = "Please Upload Photo";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }
            SecurityClass sc = new SecurityClass();
            hex_password = sc.gethexString(user_password);
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query = "";
            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                //con.setAutoCommit(false);
                if (isPhoto != null) {
                    filePhotoFid = cm.uploadFile(isPhoto, user_id, filePhotoName, filePhotoSize, ctypePhoto);
                }
                if (edit == 0) {
                    query = "INSERT INTO mas_admin_user_account(\n"
                            + "            user_id, user_password, user_name, user_role, user_office, user_email, user_mobile, \n"
                            + "            user_gender, user_alt_mobile, user_department_id, user_designation, \n"
                            + "            address1_road_landmark, address1_city_town_village, address1_po, \n"
                            + "            address1_district, address1_pin, user_photo_fid, \n"
                            + "            user_created_by, last_access_time, remarks, user_rc_member_id, user_login_id )\n"
                            + "            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?,?);";

                    ps = con.prepareStatement(query);
                    int i = 1;
                    user_id = generateUserId();
                    ps.setString(i++, user_id);
                    ps.setString(i++, hex_password);
                    ps.setString(i++, userName);
                    ps.setInt(i++, user_role);
                    ps.setInt(i++, user_office);
                    ps.setString(i++, email);
                    ps.setString(i++, mobile);
                    ps.setString(i++, gender);
                    ps.setString(i++, altPhno);
                    ps.setInt(i++, dept);
                    ps.setString(i++, designation);
                    ps.setString(i++, landmark);
                    ps.setString(i++, city);
                    ps.setString(i++, po);
                    ps.setInt(i++, district);
                    ps.setString(i++, addrPIN);
                    ps.setInt(i++, filePhotoFid);
                    ps.setString(i++, userId);
                    ps.setString(i++, user_password);
                    ps.setString(i++, rcMemId);
                    ps.setString(i++, loginId);
                } else if (edit == 1) {

                    query = "UPDATE mas_admin_user_account\n"
                            + "   SET  user_name=?, user_role=?, user_office=?, user_email=?, \n"
                            + "       user_mobile=?, user_gender=?, user_alt_mobile=?, user_department_id=?, \n"
                            + "       user_designation=?, address1_road_landmark=?, address1_city_town_village=?, \n"
                            + "       address1_po=?, address1_district=?, address1_pin=?, user_rc_member_id=?, \n"
                            + "       user_login_id=?, last_profile_update_time= CURRENT_TIMESTAMP \n"
                            + " WHERE user_id=? ";

                    ps = con.prepareStatement(query);
                    int i = 1;
                    // ps.setString(i++, user_password);
                    ps.setString(i++, userName);
                    ps.setInt(i++, user_role);
                    ps.setInt(i++, user_office);
                    ps.setString(i++, email);
                    ps.setString(i++, mobile);
                    ps.setString(i++, gender);
                    ps.setString(i++, altPhno);
                    ps.setInt(i++, dept);
                    ps.setString(i++, designation);
                    ps.setString(i++, landmark);
                    ps.setString(i++, city);
                    ps.setString(i++, po);
                    ps.setInt(i++, district);
                    ps.setString(i++, addrPIN);
                    ps.setString(i++, rcMemId);
                    ps.setString(i++, loginId);
                    ps.setString(i++, user_id);
                }
                if (ps.executeUpdate() > 0) {
                    //con.commit();
                    if (edit == 0) {
                        message = "User created successfully , password is " + user_password;
                    } else if (edit == 1) {
                        message = "User details updated successfully";
                    }

                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                } else {
                    message = "Error in saving";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp");
                    return;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                response.sendRedirect("Admin/manageAdminUsers.jsp");
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageAdminUsers.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public String generateUserId() {
        String uId = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            String query = "SELECT generate_user_id()";
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            //int i = 0;
            while (res.next()) {
                uId = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return uId;
    }
}
