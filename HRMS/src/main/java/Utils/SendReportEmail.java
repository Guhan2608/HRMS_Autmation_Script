package Utils;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.testng.Reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class SendReportEmail {
    public static void sendEmail() {
        final String fromEmail = "prasath.12.jp@gmail.com";   // sender Gmail
        final String appPassword = "neme nezo nrcw hwwz";     // app password (NOT normal password!)
        String toEmail = "guhaneswaran@abi-tech.com.sg";             // receiver

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Sales Commission - Regression suite - Automation Report- "+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            // Body
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(
                    "Hi Team,\n\nPlease find attached the latest TestNG automation report." +
                            "\n\n Additionally I had attached the excel for your reference please review it." +
                            "\n Report for following input: "+
                            "\n \n Country : Malaysia \n Brand : MTB \n Store Code : O78 \n Month and Year : April -2025"+
                            "\n\nRegards," +
                            "\nGuhaneswaran " +
                            "\n Automation Tester"
            );
// Attachment (TestNG report)
            MimeBodyPart attachmentPart1 = new MimeBodyPart();

// ✅ Use correct file path directly — DO NOT use System.getProperty with a full path
            String reportPath = "D:\\IntelJ_IDE\\Program\\Valiram\\test-output\\emailable-report.html";
            // it was for Excel\
            MimeBodyPart attachmentPart2 = new MimeBodyPart();

// ✅ Optional: Check if the file exists before attaching (recommended)
            File reportFile = new File(reportPath);
            if (!reportFile.exists()) {
                throw new FileNotFoundException("❌ Report file not found at: " + reportPath);
            }

            attachmentPart1.attachFile(reportFile);
//            attachmentPart2.attachFile(SalesCommision.filePath);

            // Combine parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart1);
            multipart.addBodyPart(attachmentPart2);

            message.setContent(multipart);

            // Send
            Transport.send(message);
            Reporter.log("Report sent successfully!", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
