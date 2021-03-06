package org.mterra.mpc.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.model.Bpm;
import org.mterra.mpc.model.ProjectInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Helper {

    public static void createDirs(String directoryPath) {
        if (StringUtils.isNotBlank(directoryPath)) {
            File targetDir = new File(directoryPath);
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

    public static void writeMapFile(File targetFile, List<Bpm> bpms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            if (targetFile.exists() || targetFile.createNewFile()) {
                for (Bpm bpm : bpms) {
                    writer.write(bpm.getBpm() + "\t" + bpm.getProjectName() + "\n");
                }
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

    public static List<ProjectInfo> getProjectsInDirectory(String directoryPath) {
        List<ProjectInfo> result = new ArrayList<>();
        File scanDir = new File(directoryPath);
        for (File srcDir : scanDir.listFiles()) {
            if (srcDir.getName().endsWith(Constants.PROJECT_FOLDER_SUFFIX)) {
                ProjectInfo projectInfo = new ProjectInfo(srcDir);
                if (projectInfo.getProjectFile().exists()) {
                    result.add(projectInfo);
                }
            }
        }
        return result;
    }

    public static void writeDocument(Document document, File targetFile) {
        try {
            FileOutputStream outStream = new FileOutputStream(targetFile);
            document.setXmlStandalone(true);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(outStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFilesByExtension(File directory, String extension) {
        Collection<?> toDelete = FileUtils.listFiles(directory, new String[]{extension}, false);
        for (Object el : toDelete) {
            if (el instanceof File) {
                File file = (File) el;
                if (!file.delete()) {
                    System.out.printf("Unable to delete '%s'%n", el);
                }
            }
        }
    }
}
