package org.mterra.mpc;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReorderSeqs {

    private static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    private static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    private static final String SEQ_SUFFIX = "sxq";
    private final String projectName;
    private final File srcDir;
    private final File targetDir;
    private final Map<String, String> seqFilesMap = new HashMap<>();
    private Document document;

    private ReorderSeqs(String projectName, File srcDir, File targetDir) {
        this.projectName = projectName;
        this.srcDir = srcDir;
        this.targetDir = targetDir;
    }

    public static void inPath(String srcDirPath, String targetDirPath) {
        File srcDir = new File(srcDirPath);
        File targetDir = new File(targetDirPath);
        for (File dir : srcDir.listFiles()) {
            System.out.printf("Checking %s%n", dir.getName());
            if (dir.getName().contains(PROJECT_FOLDER_SUFFIX)) {
                String projectName = StringUtils.substringBefore(dir.getName(), PROJECT_FOLDER_SUFFIX);
                System.out.printf("Found project '%s'%n", projectName);
                new ReorderSeqs(projectName, dir, targetDir)
                        .loadDoc()
                        .reorderSeqs()
                        .writeResult()
                        .updateFiles();

            }
        }
    }

    private ReorderSeqs loadDoc() {
        try {
            File allSeqs = new File(srcDir, ALL_SEQS_FILE_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(allSeqs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Collection<File> seqFiles = FileUtils.listFiles(srcDir, new String[]{SEQ_SUFFIX}, false);
        for (File src : seqFiles) {
            String seq = StringUtils.substringBefore(src.getName(), "." + SEQ_SUFFIX);
            seqFilesMap.put(seq, seq);
        }
        return this;
    }

    private ReorderSeqs reorderSeqs() {
        // MPCVObject/AllSeqSamps/Songs
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        Element song = (Element) songs.getElementsByTagName("Song").item(0);
        Element name = (Element) song.getElementsByTagName("Name").item(0);
        NodeList seqIdxs = song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        System.out.printf("Found song '%s' with '%s' sequences used%n", songName, seqIdxs.getLength());

        return this;
    }

    private ReorderSeqs writeResult() {
        try {
            targetDir.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Path orig = Paths.get(srcDir.getParentFile().getPath(), projectName + ".xpj");
            Path target = Paths.get(targetDir.getPath(), projectName + ".xpj");
            FileUtils.copyFile(orig.toFile(), target.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Path orig = Paths.get(srcDir.getParentFile().getPath(), projectName + PROJECT_FOLDER_SUFFIX);
            Path target = Paths.get(targetDir.getPath(), projectName + PROJECT_FOLDER_SUFFIX);
            FileUtils.copyDirectory(orig.toFile(), target.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private ReorderSeqs updateFiles() {
        File outputProj = new File(targetDir, projectName + PROJECT_FOLDER_SUFFIX);
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = tf.newTransformer();

            File output = new File(outputProj, ALL_SEQS_FILE_NAME);
            FileOutputStream outStream = new FileOutputStream(output);
            document.setXmlStandalone(true);
            transformer.transform(new DOMSource(document), new StreamResult(outStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            for (Map.Entry<String, String> entry : seqFilesMap.entrySet()) {
                if (!entry.getKey().equals(entry.getValue())) {
                    File src = new File(outputProj, entry.getKey() + "." + SEQ_SUFFIX);
                    File dest = new File(outputProj, entry.getValue() + "." + SEQ_SUFFIX + "tmp");
                    FileUtils.copyFile(src, dest);
                }
            }
            Collection<File> seqFiles = FileUtils.listFiles(srcDir, new String[]{SEQ_SUFFIX + "tmp"}, false);
            for (File src : seqFiles) {
                Path fileToMovePath = Paths.get(src.getPath());
                Path targetPath = Paths.get(StringUtils.substringBefore(src.getPath(), "tmp"));
                Files.move(fileToMovePath, targetPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

}
