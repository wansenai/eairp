/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 取网卡物理地址-
 *  1.在Windows,Linux系统下均可用；
 *  2.通过ipconifg,ifconfig获得计算机信息；
 *  3.再用模式匹配方式查找MAC地址，与操作系统的语言无关>
 *  <p>
 *  Description: <取计算机名--从环境变量中取>
 *  abstract 限制继承/创建实例
 *  </p>
 **/
public abstract class ComputerInfo {
    private static String macAddressStr = null;
    private static String computerName = System.getenv().get("COMPUTERNAME");

    private static final String[] windowsCommand = { "ipconfig", "/all" };
    private static final String[] linuxCommand = { "/sbin/ifconfig", "-a" };
    private static final Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
            Pattern.CASE_INSENSITIVE);

    /**
     * 获取多个网卡地址
     *
     * @return
     * @throws IOException
     */
    private final static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<String>();
        final String os = System.getProperty("os.name");
        final String command[];

        if (os.startsWith("Windows")) {
            command = windowsCommand;
        } else if (os.startsWith("Linux")) {
            command = linuxCommand;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        // 执行命令
        final Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (String line = null; (line = bufReader.readLine()) != null;) {
            Matcher matcher = macPattern.matcher(line);
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1));
                // macAddressList.add(matcher.group(1).replaceAll("[-:]",
                // ""));//去掉MAC中的“-”
            }
        }

        process.destroy();
        bufReader.close();
        return macAddressList;
    }

    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     *
     * @return
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.equals("")) {
            StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            try {
                List<String> macList = getMacAddressList();
                for (Iterator<String> iter = macList.iterator(); iter.hasNext();) {
                    String amac = iter.next();
                    if (!amac.equals("0000000000E0")) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            macAddressStr = sb.toString();

        }

        return macAddressStr;
    }

    /**
     * 获取电脑名
     *
     * @return
     */
    public static String getComputerName() {
        if (computerName == null || computerName.equals("")) {
            computerName = System.getenv().get("COMPUTERNAME");
        }
        return computerName;
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String getIpAddrAndName() throws IOException {
        return InetAddress.getLocalHost().toString();
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String getIpAddr() throws IOException {
        return InetAddress.getLocalHost().getHostAddress().toString();
    }

    /**
     * 获取电脑唯一标识
     *
     * @return
     */
    public static String getComputerID() {
        String id = getMacAddress();
        if (id == null || id.equals("")) {
            try {
                id = getIpAddrAndName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return computerName;
    }

    /**
     * 限制创建实例
     */
    private ComputerInfo() {

    }
}
