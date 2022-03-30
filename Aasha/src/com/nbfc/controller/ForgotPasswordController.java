package com.nbfc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

//import org.apache.coyote.Request;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nbfc.bean.LoginBean;
import com.nbfc.bean.UserRoleBean;
import com.nbfc.model.Login;
import com.nbfc.model.MLIRegistration;
import com.nbfc.model.OtpDetailsModel;
import com.nbfc.service.ForgotpasswordService;
import com.nbfc.service.LoginService;
import com.nbfc.service.UserActivityService;
//import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding;
import com.vaannila.validator.UserValidator;


@Controller
public class ForgotPasswordController {
static Logger log = Logger.getLogger(NBFCController.class.getName());
@Autowired
private ForgotpasswordService forgotpasService;
@Autowired
UserActivityService userActivityService;
@Autowired
private LoginService loginService;
UserValidator userValidator= new UserValidator();
String userIdAndPasswordIsCorrect =null;
String email =null;


@RequestMapping(value="ForgotPassword" ,method = RequestMethod.POST)
public ModelAndView forgotPassword(@ModelAttribute("command")LoginBean loginBean,BindingResult result,HttpSession session,Model model) {
 	log.info("showLoginBeanInputForm method call as part of forgotpassword=====");
 	String userid=loginBean.getUsr_id();
 	System.out.println(userid);
 	Boolean res=userValidator.validateForUserid(loginBean, result);
 	if(res==false){
 		return new ModelAndView("login");
 		
 	}
 	model.addAttribute("success", "false");
	return new ModelAndView("Forgot_password");
	//return "login";
}
	
@RequestMapping(value="/OTPSubmissions" ,params = "action1",method = RequestMethod.POST)
public ModelAndView OTPSubmissions(@ModelAttribute("command")LoginBean loginBean,BindingResult result,Model model,HttpSession session) {
 	log.info("showLoginBeanInputForm method call as part of forgotpassword=====");
	String loginUsrId=loginBean.getUsr_id();
	
	if(session== null){
		
		System.out.println("session null......");
	}
	model.addAttribute("success", "true");
	session.setAttribute("userid", loginUsrId);
	
	userIdAndPasswordIsCorrect = forgotpasService.verifyUserID(loginBean);
	log.info("isUserExists==="+userIdAndPasswordIsCorrect);
	if(userIdAndPasswordIsCorrect.equals("invalidUser")) {
	log.info("userExists not Exit==="+userIdAndPasswordIsCorrect);	
	model.addAttribute("InvalidCredencialKey", "Invalid User ID");
	log.info("Invalid Credentials===");				
	
	
	//return "login";
}else{
	email = forgotpasService.getUsermailID(loginUsrId);
	log.info("isUserExists==="+userIdAndPasswordIsCorrect);
	
	if(email.equals("invalid")) {
		log.info("userExists not Exit==="+userIdAndPasswordIsCorrect);	
		model.addAttribute("InvalidCredencialKey", "Email ID not available");
		log.info("Invalid Credentials===");		
	}else{
		String subject = "User OTP Generation from User ID";
	//	String Otp="12345";
		String OTP = forgotpasService.getOTP(loginUsrId);
		
		System.out.println(OTP);
		String mailBody = "Dear User, \n \n Congrats,Your OTP has been generated from NBFC,corresponding to your User Id.Please find the below details."
			+ " \n "
				+ "User Id :"
				+ loginUsrId
				+ " \n "
				+ "OTP :"
				+ OTP
				+ "\n Please given OTP must not be shared with anybody." +
				" \n" +
				"you may Reset your password  by using this OTP. ";
		
		   forgotpasService.sendmail(loginUsrId,email,OTP,subject,mailBody);
		 
		   OtpDetailsModel otpDetails = prepareModelForOtpDetail(loginBean,loginUsrId, OTP);
		//	mliRegService.addMLIDetails(mliRegistration);

		  forgotpasService.insertOTPData(otpDetails);
		  System.out.println("Otp Data inserted Succesfully...");
		  model.addAttribute("success", "true");
		
		
	}
	
	
	
}
	return new ModelAndView("Forgot_password");
}

private OtpDetailsModel prepareModelForOtpDetail(LoginBean loginBean,
		String loginUsrId, String oTP) {
	OtpDetailsModel bean= new OtpDetailsModel();
	bean.setOtp(oTP);
	bean.setUser_id(loginUsrId);
	return bean;
}

@RequestMapping(value="/OTPSubmissions" ,params = "action2",method = RequestMethod.POST)
public ModelAndView checksOtpStatus(@ModelAttribute("command")LoginBean loginBean,BindingResult result,Model model,HttpSession session) {
	String otpIsCorrect="";

    String loginUsrId=loginBean.getUsr_id();
	
	otpIsCorrect = forgotpasService.verifyUserOtp(loginBean);
	log.info("isUserExists==="+otpIsCorrect);
	if(otpIsCorrect.equals("OTPExpire")) {
		 model.addAttribute("Resend", "true");
		log.info("ExpireOTP ==="+userIdAndPasswordIsCorrect);	
		model.addAttribute("InvalidCredencialKeyOTP", "OTP has been expired..");
		session.setAttribute("userid", loginUsrId);
		log.info("Invalid Credentials===");				
		return new ModelAndView("Forgot_password");
		
	}else if(otpIsCorrect.equals("InvalidOTP")) {
    model.addAttribute("Resend", "true");
	log.info("InvalidOTP ==="+userIdAndPasswordIsCorrect);	
	model.addAttribute("InvalidCredencialKeyOTP", "Invalid OTP");
	session.setAttribute("userid", loginUsrId);
	log.info("Invalid Credentials===");				
	return new ModelAndView("Forgot_password");
	//return "login";
}
	else{
	
	session.setAttribute("userid", loginUsrId);
	return new ModelAndView("confirm_password");
}
	
	
}


@RequestMapping(value="/ResetPassword" ,method = RequestMethod.POST)
public ModelAndView ResetPassword(@ModelAttribute("command")LoginBean loginBean,BindingResult result,Model model,HttpSession session) {
	Map<String, Object> modelAct = new HashMap<String, Object>();
 String userid=(String) session.getAttribute("userid");
	System.out.println(".........."+userid);
	ModelAndView modelAndView = new ModelAndView();
	
	if(loginBean.getNewPassword().equals(loginBean.getConfirm_password())){
		if(loginBean.getNewPassword().length() >= 8){
			
			boolean flag=forgotpasService.changePassword(preChangePasswordModal(loginBean,loginService.userDetails(userid)));
			if(flag){
			modelAct.put("message", "Password has been successfully created ");
			modelAndView=new ModelAndView("login",modelAct);
			}
			}else{
			
			modelAct.put("message", "Password should be menimum 8 digits !!");
			
			modelAndView=new ModelAndView("confirm_password",modelAct);
		}
			}else{
		modelAct.put("message", "Password does not match !!");	
		modelAndView=new ModelAndView("confirm_password",modelAct);
	}
	//loginService.userDetails(loginBean.getUsr_id());
	return modelAndView;
}

public Login preChangePasswordModal(LoginBean loginBean, Login login) {
	Login loginEntity = new Login();
	
	loginEntity.setADDRESS(login.getADDRESS());
	loginEntity.setCITY(login.getCITY());
	loginEntity.setDISTRICT(login.getDISTRICT());
	loginEntity.setFAX_NO(login.getFAX_NO());
	loginEntity.setHINT_ANS(login.getHINT_ANS());
	loginEntity.setHINT_QUESTION(login.getHINT_QUESTION());
	loginEntity.setLOGIN_STATUS("Y");
	loginEntity.setMEM_BNK_ID(login.getMEM_BNK_ID());
	loginEntity.setMEM_BRN_ID(login.getMEM_BRN_ID());
	loginEntity.setMEM_ZNE_ID(login.getMEM_ZNE_ID());
	loginEntity.setMOBILE_NO(login.getMOBILE_NO());
	//loginEntity.setPasswordChangeDate(login.getPasswordChangeDate());
	loginEntity.setPHONE_CODE(login.getPHONE_CODE());
	loginEntity.setPHONE_NO(login.getPHONE_NO());
	loginEntity.setPIN_CODE(login.getPIN_CODE());
	loginEntity.setSTATE(login.getSTATE());
	loginEntity.setUSR_CREATED_BY(login.getUSR_CREATED_BY());
	loginEntity.setUSR_CREATED_DT(login.getUSR_CREATED_DT());
	loginEntity.setUSR_DSG_NAME(login.getUSR_DSG_NAME());
	loginEntity.setUSR_EMAIL_ID(login.getUSR_EMAIL_ID());
	loginEntity.setUSR_FIRST_NAME(login.getUSR_FIRST_NAME());
	loginEntity.setUSR_HINT_ANSWER(login.getUSR_HINT_ANSWER());
	loginEntity.setUsr_id(login.getUsr_id());
	loginEntity.setUSR_LAST_NAME(login.getUSR_LAST_NAME());
	loginEntity.setUSR_MIDDLE_NAME(login.getUSR_MIDDLE_NAME());
	loginEntity.setUSR_MODIFIED_BY(login.getUSR_MODIFIED_BY());
	loginEntity.setUSR_MODIFIED_DT(login.getUSR_MODIFIED_DT());
	loginEntity.setUSR_OLD_USR_ID(login.getUSR_OLD_USR_ID());
	loginEntity.setUsr_password(loginBean.getUsr_password());
	loginEntity.setUSR_STATUS(login.getUSR_STATUS());
	loginEntity.setUSR_TYPE(login.getUSR_TYPE());

	loginEntity.setUsr_id(login.getUsr_id());

	loginEntity.setUsr_password(loginBean.getNewPassword());

	return loginEntity;

}


}
