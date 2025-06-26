package sensores.track.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendAlertEmail(String destinatario, String tipoSensor, BigDecimal ultimaLeitura, BigDecimal limiteMinimo, BigDecimal limiteMaximo) throws MessagingException {
        // Cria a mensagem de email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Configura remetente, destinatário e assunto
        helper.setFrom("sensoresprojeto@gmail.com");
        helper.setTo(destinatario);
        helper.setSubject("[ALERTA] SENSOR-TRACK");

        // Cria o corpo do email em HTML
        // Cria o corpo do email em HTML
        StringBuilder htmlBody = new StringBuilder();
        htmlBody.append("<p>Prezado(a),</p>");
        htmlBody.append("<p>O sensor do tipo <strong>").append(tipoSensor).append("</strong> identificou uma leitura fora dos limites configurados.</p>");
        htmlBody.append("<strong>Leitura:</strong> ").append(ultimaLeitura.toString()).append("</p>");
        htmlBody.append("<p><strong>Limite mínimo:</strong> ").append(limiteMinimo.toString()).append("<br>");
        htmlBody.append("<strong>Limite máximo:</strong> ").append(limiteMaximo.toString()).append("</p>");
        htmlBody.append("<p>Para mais detalhes, acesse o portal Sensor Track:<br>");
        htmlBody.append("<a href=\"https://sensor-track.netlify.app/\">https://sensor-track.netlify.app/</a></p>");
        htmlBody.append("<p>Atenciosamente,<br>A equipe Sensor-Track</p>");

        // Define o conteúdo HTML do email
        helper.setText(htmlBody.toString(), true);

        // Envia o email
        mailSender.send(message);
    }
}
