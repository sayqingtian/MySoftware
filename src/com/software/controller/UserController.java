package com.software.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.activation.*;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Spring;
import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.software.pojo.User;
import com.software.service.UserService;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

// 告诉spring mvc这是一个控制器类
@Controller
@RequestMapping("")
public class UserController {
	
	private static Session session;
    private static Properties props = new Properties();
    private static final String HOST = "smtp.qq.com";
    private static int PORT = 587;
    private static final String isAUTH = "true";
    private static final String FROM = "1048682973@qq.com";

    private static final String USERNAME = "1048682973@qq.com";
    private static final String PASSWORD = "kjwkhcwtfecubece";

    private static final String TIMEOUT = "25000";
    private static final String DEBUG = "true";
	@Autowired
	UserService userService;

	@RequestMapping("listUser")
	public ModelAndView listUser(){
		ModelAndView mav = new ModelAndView();
		List<User> cs= userService.list();

		// 放入转发参数
		mav.addObject("cs", cs);
		// 放入jsp路径
		mav.setViewName("listUser");
		return mav;
	}
	
	@RequestMapping("deleteUser")
	public ModelAndView deleteUser(){
		ModelAndView mav = new ModelAndView();
		List<User> cs= userService.list();
		cs.remove(0);
		// 放入转发参数
		mav.addObject("cs", cs);
		// 放入jsp路径
		mav.setViewName("deleteUser");
		return mav;
	}
	
	@RequestMapping("Login")
	public ModelAndView Login(HttpServletRequest req) throws Exception{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("Login");
		req.getSession().removeAttribute("currUser");
		req.getSession().setAttribute("times", "1");
		//generateKeyPair();
		return mav;
	}
	
	

	
	@RequestMapping("login_validate")
    public ModelAndView login(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		ModelAndView mav = new ModelAndView();
		
		if (req.getSession().getAttribute("currUser")==null) {
			req.setCharacterEncoding("utf-8");//设置参数的编码格式
			req.getSession().setAttribute("times", "2");
			String userName =req.getParameter("username");
			String userPwd =req.getParameter("password");
			User user=new User();
			user.setName(userName);
			user.setPassword(userPwd);
			req.getSession().setAttribute("currUser", user);
			List<User> result=userService.validate(user);
			if(result.isEmpty())mav.setViewName("error");
			else { 
				mav.setViewName("welcome");
			}
		}else { 
			mav.setViewName("welcome");
		}
		
		return mav;
    }
	
	@RequestMapping("welcome")
    public ModelAndView welcome(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcome");
		return mav;
    }
	
	@RequestMapping("register")
	public ModelAndView register(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		ModelAndView mav = new ModelAndView();
		req.setCharacterEncoding("utf-8");//设置参数的编码格式
		String userName=req.getParameter("username");
		String userPwd=req.getParameter("password");
		String confirmPwd=req.getParameter("confirm-password");
		String email=req.getParameter("email");
		if(userPwd.equals(confirmPwd)) {
			User user=new User();
			user.setName(userName);
			user.setPassword(userPwd);
			user.setEmail(cryptMD5(email));
			if(userService.emailExist(user).isEmpty()) {
				userService.add(user);
				mav.setViewName("register_success");
			}else mav.setViewName("register_fail");
		}
		else mav.setViewName("register_fail");
		return mav;
	}
	
	@RequestMapping("forgetPwd")
	public ModelAndView forgetPwd(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("forgetPwd");
		return mav;
	}
	
	@RequestMapping("sendMail")
	public void sendMail(HttpServletRequest req,HttpServletResponse rsp){
	    props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", isAUTH);
        props.put("fromer", FROM);
        props.put("username", USERNAME);
        props.put("password", PASSWORD);
        props.put("mail.smtp.timeout", TIMEOUT);
        props.put("mail.debug", DEBUG);

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        
        try {
            String html = "http://localhost:8080/MySoftware/modify?emailMD5="+cryptMD5("1048682973@qq.com");
            sendEmail(req.getParameter("email").toString(), "Test", html, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void sendEmail(String to, String title, String content, boolean isHtml)
            throws FileNotFoundException, IOException, MessagingException {
        String fromer = props.getProperty("fromer");
        if (isHtml) {
            sendHtmlEmail(fromer, to, title, content);
        } else {
            sendTextEmail(fromer, to, title, content);
        }
    }

    // 发送纯文字邮件
    public static void sendTextEmail(String from, String to, String subject, String content)
            throws FileNotFoundException, IOException, MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(content);
        message.setSentDate(new Date());
        Transport.send(message);
    }

    // 发送有HTML格式邮件
    public static void sendHtmlEmail(String from, String to, String subject, String htmlConent)
            throws FileNotFoundException, IOException, MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());

        Multipart multi = new MimeMultipart();
        BodyPart html = new MimeBodyPart();
        html.setContent(htmlConent, "text/html; charset=utf-8");
        multi.addBodyPart(html);
        message.setContent(multi);
        Transport.send(message);
    }
    
    public static String cryptMD5(String str) {  
        if (str == null || str.length() == 0) {  
            throw new IllegalArgumentException("String to encript cannot be null or zero length");  
        }  
        StringBuffer hexString = new StringBuffer();  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(str.getBytes());  
            byte[] hash = md.digest();  
            for (int i = 0; i < hash.length; i++) {  
                if ((0xff & hash[i]) < 0x10) {  
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));  
                } else {  
                    hexString.append(Integer.toHexString(0xFF & hash[i]));  
                }  
            }  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return hexString.toString();  
    }  
    
    @RequestMapping("modify")
	public ModelAndView modify(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("modify");
		return mav;
	}
    
    @RequestMapping("doModify")
	public ModelAndView doModify(HttpServletRequest req,HttpServletResponse rsp){
		ModelAndView mav = new ModelAndView();
		String emailMD5=req.getParameter("emailMD5");
		String userPwd=req.getParameter("password");
		String confirmPwd=req.getParameter("confirm-password");
		if(userPwd.equals(confirmPwd)) {
			User user=new User();
			user.setPassword(userPwd);
			user.setEmail(emailMD5);
			int res=userService.update(user);
			mav.setViewName("modify_success");
		}
		else mav.setViewName("modify_fail");
		return mav;
	}
    
	private static void generateKeyPair() throws Exception {
	        
	//      /** RSA算法要求有一个可信任的随机数源 */
	//      SecureRandom secureRandom = new SecureRandom();
	      
	      /** 为RSA算法创建一个KeyPairGenerator对象 */
	      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	      
	      /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
	//      keyPairGenerator.initialize(KEYSIZE, secureRandom);
	      keyPairGenerator.initialize(1024);
	      
	      /** 生成密匙对 */
	      KeyPair keyPair = keyPairGenerator.generateKeyPair();
	      
	      /** 得到公钥 */
	      Key publicKey = keyPair.getPublic();
	      
	      /** 得到私钥 */
	      Key privateKey = keyPair.getPrivate();
	      
	      ObjectOutputStream oos1 = null;
	      ObjectOutputStream oos2 = null;
	      try {
	          /** 用对象流将生成的密钥写入文件 */
	          oos1 = new ObjectOutputStream(new FileOutputStream("F://PublicKey"));
	          oos2 = new ObjectOutputStream(new FileOutputStream("F://PrivateKey"));
	          oos1.writeObject(publicKey);
	          oos2.writeObject(privateKey);
	      } catch (Exception e) {
	    	  System.out.println("wpf");
	          e.printStackTrace();
	      }
	      finally{
	          /** 清空缓存，关闭文件输出流 */
	          oos1.close();
	          oos2.close();
	      }
	  }
    
    /**
     * 加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String source,String filename) throws Exception {
    	//generateKeyPair();
        Key publicKey;
        ObjectInputStream ois = null;
        try {
            /** 将文件中的公钥对象读出 */
            ois = new ObjectInputStream(new FileInputStream(
            		filename));
            publicKey = (Key) ois.readObject();
        } catch (Exception e) {
            throw e;
        }
        finally{
            ois.close();
        }
        
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    /**
     * 解密算法
     * @param cryptograph    密文
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptograph,String filename) throws Exception {
        Key privateKey;
        ObjectInputStream ois = null;
        try {
            /** 将文件中的私钥对象读出 */
            ois = new ObjectInputStream(new FileInputStream(
            		filename));
            privateKey = (Key) ois.readObject();
        } catch (Exception e) {
            throw e;
        }
        finally{
            ois.close();
        }
        
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);
        
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }
    
    @RequestMapping("verify")
	public ModelAndView verify(HttpServletRequest rq) throws Exception{
		ModelAndView mav = new ModelAndView();
		String str=rq.getSession().getAttribute("currUser").toString()+" "+new Date();
		mav.addObject("txt", cryptMD5(str));
		mav.addObject("encrypttxt",encrypt(cryptMD5(str), "F://PublicKey"));
		mav.setViewName("verify");
		return mav;
	}
    
    @RequestMapping("doverify")
	public ModelAndView doverify(HttpServletRequest rq){
		ModelAndView mav = new ModelAndView();
		System.out.println(rq.getParameter("hidden"));
		
		if (rq.getParameter("information").equals(rq.getParameter("hidden"))) {
			rq.getSession().setAttribute("verifyUser", new User());
			mav.setViewName("verify_success");
			
		}else {
			mav.setViewName("verify_fail");
		}	
		return mav;
	}
    
    @RequestMapping("download")
    public ModelAndView download(HttpServletRequest rq){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("download");
		rq.getSession().removeAttribute("verifyUser");
		return mav;
    }
    @RequestMapping("download_error")
    public ModelAndView download_error(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("download_error");
		return mav;
    }
}
