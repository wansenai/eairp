/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.utils.email;

import com.wansenai.utils.constants.EmailConstant;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.internet.*;
import java.util.List;

public class EmailUtils {

    private String smtpHost; // 邮件服务器地址
    private String sendUserName; // 发件人的用户名
    private String sendUserPass; // 发件人密码

    private MimeMessage mimeMsg; // 邮件对象
    private Multipart mp;// 附件添加的组件

    private void init() {
        // 创建一个密码验证器
        MyAuthenticator authenticator = null;
        authenticator = new MyAuthenticator(sendUserName, sendUserPass);

        // 实例化Properties对象
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true"); // 需要身份验证
        //此属性待测试先注释
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        // 建立会话
        Session session = Session.getDefaultInstance(props, authenticator);
        // 置true可以在控制台（console)上看到发送邮件的过程
        session.setDebug(true);
        // 用session对象来创建并初始化邮件对象
        mimeMsg = new MimeMessage(session);
        // 生成附件组件的实例
        mp = new MimeMultipart();
    }

    private EmailUtils(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject,
                       String mailBody, List<String> attachments) {
        this.smtpHost = smtpHost;
        this.sendUserName = sendUserName;
        this.sendUserPass = sendUserPass;

        init();
        setFrom(sendUserName);
        setTo(to);
        setCC(cc);
        setBody(mailBody);
        setSubject(mailSubject);
        if (attachments != null) {
            for (String attachment : attachments) {
                addFileAffix(attachment);
            }
        }

    }

    /**
     * 邮件实体
     *
     * @param smtpHost     邮件服务器地址
     * @param sendUserName 发件邮件地址
     * @param sendUserPass 发件邮箱密码
     * @param to           收件人，多个邮箱地址以半角逗号分隔
     * @param cc           抄送，多个邮箱地址以半角逗号分隔
     * @param mailSubject  邮件主题
     * @param mailBody     邮件正文
     * @param attachments  附件路径
     * @return
     */
    public static EmailUtils entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc,
                                    String mailSubject, String mailBody, List<String> attachments) {
        return new EmailUtils(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, attachments);
    }

    /**
     * 设置邮件主题
     *
     * @param mailSubject
     * @return
     */
    private boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
     *
     * @param mailBody
     * @return
     */
    private boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody,
                    "text/html;charset=UTF-8");
            // 在组件上添加邮件文本
            mp.addBodyPart(bp);
        } catch (Exception e) {
            System.err.println("设置邮件正文时发生错误！" + e);
            return false;
        }
        return true;
    }

    /**
     * 添加一个附件
     *
     * @param filename 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
     * @return
     */
    public boolean addFileAffix(String filename) {
        try {
            if (filename != null && filename.length() > 0) {
                BodyPart bp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(filename);
                bp.setDataHandler(new DataHandler(fileds));
                bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null)); // 解决附件名称乱码
                mp.addBodyPart(bp);// 添加附件
            }
        } catch (Exception e) {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
            return false;
        }
        return true;
    }

    /**
     * 设置发件人地址
     *
     * @param from 发件人地址
     * @return
     */
    private boolean setFrom(String from) {
        try {
            mimeMsg.setFrom(new InternetAddress(from));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置收件人地址
     *
     * @param to 收件人的地址
     * @return
     */
    private boolean setTo(String to) {
        if (to == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置抄送
     *
     * @param cc
     * @return
     */
    private boolean setCC(String cc) {
        if (cc == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * no object DCH for MIME type multipart/mixed报错解决
     */
    private void solveError() {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap(
                "multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        Thread.currentThread().setContextClassLoader(EmailUtils.class.getClassLoader());
    }

    /**
     * 发送邮件
     *
     * @return
     */
    public String send() throws Exception {
        mimeMsg.setContent(mp);
        mimeMsg.saveChanges();
        System.out.println("正在发送邮件....");
        solveError();
        try {
            Transport.send(mimeMsg);
            return "发送邮件成功！";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送邮件异常！";
        }
    }

    /**
     * 修改密码通知邮件模板
     *
     * @param code              验证码
     * @param to                收件人邮箱
     */
    public static void forgetPasswordEmailNotice(String code, String to) throws Exception {
        String subject = "【万森ERP系统】: 更改密码说明"; // 主题

        String body = "<pre>\n" +
                "您好,\n" +
                "\n" +
                "   您已要求重置与此电子邮件地址 ("+ to + ")关联的 万森ERP系统 帐户的密码.\n\n" +
                "   本次验证码是：<b>" + code + "</b>, 该验证码3分钟有效\n\n" +
                "   如果您没有发起此请求，请忽略此电子邮件, \n" +
                "   谢谢, \n\n\n" +
                "   万森AI团队." +
                "</pre>";
        EmailUtils emailUtils = EmailUtils.entity(EmailConstant.EMAIL_HOST, EmailConstant.EMAIL_USER_NAME, EmailConstant.EMAIL_PASSWORD, to, null, subject, body, null);
        emailUtils.send();
    }

    public static void resetEmailNotice(String code, String to) throws Exception {
        String subject = "【万森ERP系统】: 绑定邮箱地址"; // 主题

        String body = "<pre>\n" +
                "您好,\n" +
                "\n" +
                "   您已要求绑定与此电子邮件地址 ("+ to + ")关联的 万森ERP系统帐户.\n\n" +
                "   本次验证码是：<b>" + code + "</b>, 该验证码3分钟有效\n\n" +
                "   如果您没有发起此请求，请忽略此电子邮件, \n" +
                "   谢谢, \n\n\n" +
                "   万森AI团队." +
                "</pre>";
        EmailUtils emailUtils = EmailUtils.entity(EmailConstant.EMAIL_HOST, EmailConstant.EMAIL_USER_NAME, EmailConstant.EMAIL_PASSWORD, to, null, subject, body, null);
        emailUtils.send();
    }

    public static void loginEmailNotice(String code, String to) throws Exception {
        String subject = "【万森ERP系统】: 邮箱登录验证码"; // 主题

        String body = "<pre>\n" +
                "您好,\n" +
                "\n" +
                "   您正在登录与此电子邮件地址 ("+ to + ")关联的 万森ERP系统帐户.\n\n" +
                "   本次验证码是：<b>" + code + "</b>, 该验证码3分钟有效\n\n" +
                "   如果您没有发起此请求，请忽略此电子邮件, \n" +
                "   谢谢, \n\n\n" +
                "   万森AI团队." +
                "</pre>";
        EmailUtils emailUtils = EmailUtils.entity(EmailConstant.EMAIL_HOST, EmailConstant.EMAIL_USER_NAME, EmailConstant.EMAIL_PASSWORD, to, null, subject, body, null);
        emailUtils.send();
    }

    /**
     * 项目合同通知邮件模板
     *
     * @param projectName           项目名称
     * @param date                  时间
     * @param to                    收件人邮箱
     */
    public static void projectExpirationEmailNotice(String projectName, String date, String to) throws Exception {
        String subject = "【万森ERP系统】: 项目合同到期通知"; // 主题

        String body = "<pre>\n" +
                "您好,\n" +
                "\n" +
                "   公司签订的<b>" + projectName + "</b>[项目]合同于"+ date +"到期.\n\n" +
                "   合同期满后, 准备续订(终止)该合同\n" +
                "   谢谢, \n\n\n" +
                "   新融合检测项目 团队." +
                "</pre>";
        EmailUtils emailUtils = EmailUtils.entity(EmailConstant.EMAIL_HOST, EmailConstant.EMAIL_USER_NAME, EmailConstant.EMAIL_PASSWORD, to, null, subject, body, null);
        emailUtils.send();
    }

    /**
     * 季度采样登记人通知邮件模板
     *
     * @param projectName           项目名称
     * @param quarter               季度
     * @param to                    收件人邮箱
     */
    public static void sampleRegistrantEmailNotice(String projectName, String quarter, String to) throws Exception {
        String subject = "【山东新融和检测有限公司】: 季度签约通知"; // 主题

        String body = "<pre>\n" +
                "您好,\n" +
                "\n" +
                "   公司签订的<b>" + projectName + "</b>[项目]合同已到达第"+ quarter +"签约时间，请处理!\n\n" +
                "   合同期满后, 准备续订(终止)该合同\n" +
                "   谢谢, \n\n\n" +
                "   新融合检测项目 团队." +
                "</pre>";
        EmailUtils emailUtils = EmailUtils.entity(EmailConstant.EMAIL_HOST, EmailConstant.EMAIL_USER_NAME, EmailConstant.EMAIL_PASSWORD, to, null, subject, body, null);
        emailUtils.send();
    }
}
