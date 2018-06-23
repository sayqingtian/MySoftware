package cn.cherish.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtil {

    // properties配置文件地址
    //private static final String PROPERTIES_PATH = "standard_data.properties";

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

    // 初始化session
    static {
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
    }

    public static void main(String[] args) {
        try {
            String html = "<html><head>"+
                    "</head><body>"+
                    "<audio src='http://219.128.78.22/m10.music.126.net/20160423105749/3cee5688a7dc87d28a265fd992ecb0a2/ymusic/8c94/b9af/69e3/7ebe35b8e00154120822550b21b0c9c5.mp3?wshc_tag=1&wsts_tag=571aded1&wsid_tag=b73f773e&wsiphost=ipdbm' autoplay='autoplay' controls='controls' loop='-1'>爱你</audio>"+
                    "<video controls='controls'>"+
                    "<source src='http://v2.mukewang.com/45ad4643-87d7-444b-a3b9-fbf32de63811/H.mp4?auth_key=1461379796-0-0-e86cefa71cef963875fd68f8a419dd8a' type='video/mp4' />"+
                    "Your browser does not support the video tag."+
                    "</video>"+
                    "<h1>Hello,nice to fuck you!</h1>"+
                    "<span style='color:red;font-size:36px;'>并抓了一把你的小鸡鸡</span>"+
                    "</body></html>";

            //sendEmail("785427346@qq.com", "yeah", html, true);

            sendEmail("1048682973@qq.com", "Test", html, false);
            //sendFileEmail("785427346@qq.com", "yeah", html, new File("E:/xiaoming.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 
     * @Title sendEmail
     * @Description 通过isHtml判断发送的邮件的内容
     * @param to 邮件接收者
     * @param content 邮件内容
     * @param isHtml 是否发送html
     * @throws MessagingException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws EmailException
     */
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
}