package org.mterra.mpc.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;
import org.mterra.mpc.model.ProjectInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {

    public static void createDirs(String dir) {
        if (StringUtils.isNotBlank(dir)) {
            File targetDir = new File(dir);
            if (!targetDir.exists()) {
                if (!targetDir.mkdirs()) {
                    throw new RuntimeException("Cannot create target directory " + targetDir.getAbsolutePath());
                }
            }
        }
    }

    public static void copyProject(ProjectInfo projectInfo, File targetDir) {
        try {
            Path orig = projectInfo.getProjectFile().toPath();
            Path target = Paths.get(targetDir.getPath(), projectInfo.getProjectFile().getName());
            FileUtils.copyFile(orig.toFile(), target.toFile());

            orig = projectInfo.getProjectDataFolder().toPath();
            target = Paths.get(targetDir.getPath(), projectInfo.getProjectDataFolder().getName());
            FileUtils.copyDirectory(orig.toFile(), target.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMapFile(File targetFile, Map<String, String> values) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            targetFile.createNewFile();
            for (Map.Entry<String, String> entry : values.entrySet()) {
                writer.write(entry.getValue() + "\t" + entry.getKey() + "\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static NodeList evaluateXPath(Document document, String xpathExpression) {
        try {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);
            return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> evaluateXPathToStrings(Document document, String xpathExpression) {
        try {
            NodeList nodes = evaluateXPath(document, xpathExpression);
            List<String> values = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }
            return values;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ProjectInfo> getProjectsInDirectory(String scanDirPath) {
        List<ProjectInfo> result = new ArrayList<>();
        File scanDir = new File(scanDirPath);
        for (File srcDir : scanDir.listFiles()) {
            if (srcDir.getName().endsWith(MpcUtils.PROJECT_FOLDER_SUFFIX)) {
                ProjectInfo projectInfo = new ProjectInfo(srcDir);
                result.add(projectInfo);
            }
        }
        return result;
    }
}
