package com.robotsandpencils.codegen;

import java.io.*;

public class TranFileInfo {
    public static void main(String[] args) {
        String projectPath = "D:\\kscript\\swagger\\myClient\\src\\main\\";
        String sourceFolder = "src/main";
        String packageName = "com.james.gate";
        String packagePath = "com\\james\\gate\\";
        String baseResponse = "WebApiResponse";
        File apisPath = new File(projectPath + packagePath + "apis");
        if (apisPath.exists() && apisPath.isDirectory() && apisPath.listFiles() != null) {
            File[] apiFiles = apisPath.listFiles();
            for (File apiFile : apiFiles) {
                try {
//                    apiFile = new File(apisPath, "UserInfoControllerApi.kt");
                    BufferedReader br = new BufferedReader(new FileReader(apiFile));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String afterLine = line;
                        boolean addLineSymbol = true;

                        if (afterLine.trim().startsWith("import") && afterLine.contains(baseResponse)) {
                            if (afterLine.contains("«")) {
                                String beanString = afterLine.substring(afterLine.indexOf(baseResponse) + baseResponse.length());
                                beanString = beanString.replace("«", "");
                                beanString = beanString.replace("»", "");
                                if (beanString.contains("PageInfo")) {
                                    if (!sb.toString().contains("PageInfo")) {
                                        sb.append(afterLine.substring(0, afterLine.indexOf(baseResponse)) + "PageInfo" + "\n");
                                    }
                                    afterLine = afterLine.substring(0, afterLine.indexOf(baseResponse)) + beanString.replace("PageInfo", "");
                                } else {
                                    afterLine = afterLine.substring(0, afterLine.indexOf(baseResponse)) + beanString;
                                }

                            }

                        } else if (afterLine.trim().startsWith("import") && afterLine.contains(packageName + ".models.string")) {
//                            import com.james.gate.models.string
                            continue;
                        } else if (afterLine.trim().startsWith("import") && afterLine.contains(packageName + ".models.List")) {
//                           import com.james.gate.models.ListUserDetailsResp
                            continue;
                        }

                        if (afterLine.trim().endsWith(baseResponse) && !afterLine.contains("import")) {
                            afterLine = afterLine.replace(baseResponse, baseResponse + "<Any>");
                        } else if (afterLine.contains("«string»")) {
                            afterLine = afterLine.replace("«string»", "<String>");
                        } else if (afterLine.contains("«")) {
                            afterLine = afterLine.replaceAll("«", "<");
                            afterLine = afterLine.replaceAll("»", ">");
                        }
                        if (afterLine.contains("authorization: String,")) {
//                            System.out.println("afterLine.contains(\"authorization: String\")");
                            afterLine = afterLine.replace("@retrofit2.http.Header(\"Authorization\") authorization: String,", "");
                            addLineSymbol = false;
                        } else if (afterLine.contains("authorization: String")) {
                            afterLine = afterLine.replace("@retrofit2.http.Header(\"Authorization\") authorization: String", "");
//                            addLineSymbol = false;
                        }


                        if (!afterLine.contains("@param authorization")) {
                            sb.append(afterLine);
                            if (addLineSymbol) sb.append("\n");
                        }

                    }
                    br.close();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(apiFile));
//                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(apisPath, "test.kt")));
                    bw.write(sb.toString());
                    bw.close();
                    bw = new BufferedWriter(new FileWriter(new File(apisPath, "ToTalApi.kt"),true));
                    bw.write(sb.toString());
                    bw.close();
//                    return;//待删除
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//      删除models文件夹中带«的多余文件,比如  WebApiResponse«ApkConfig».kt
        File modelsPath = new File(projectPath + packagePath + "models");
        if (modelsPath.exists() && modelsPath.isDirectory()) {
            File[] modelFiles = modelsPath.listFiles();
            for (File modelFile : modelFiles) {

                if (modelFile.getName().contains("«")) {
                    System.out.println("删除文件:" + modelFile.getName());
                    modelFile.delete();
                }
            }

        }
//        BufferedReader bufferedReader = new BufferedReader(new FileReader())
    }
}
