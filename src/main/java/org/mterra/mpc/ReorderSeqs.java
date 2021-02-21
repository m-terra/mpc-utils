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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReorderSeqs {

    private static final String PROJECT_FOLDER_SUFFIX = "_[ProjectData]";
    private static final String ALL_SEQS_FILE_NAME = "All Sequences & Songs.xal";
    private static final String SEQ_SUFFIX = "sxq";
    private final String projectName;
    private final File srcDir;
    private final File targetDir;
    private Document document;
    private final List<SeqInfo> seqInfoList = new ArrayList<>();
    private NodeList seqNodeList;
    private NodeList songSeqIdxNodeList;

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
                        .loadSeqsAndSong()
                        .copyProject()
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
        return this;
    }

    private ReorderSeqs loadSeqsAndSong() {
        document.getDocumentElement().normalize();

        Element all = (Element) document.getDocumentElement().getElementsByTagName("AllSeqSamps").item(0);
        Element songs = (Element) all.getElementsByTagName("Songs").item(0);
        Element song = (Element) songs.getElementsByTagName("Song").item(0);
        Element name = (Element) song.getElementsByTagName("Name").item(0);
        songSeqIdxNodeList = song.getElementsByTagName("SeqIndex");
        String songName = name.getTextContent();

        Element seqs = (Element) all.getElementsByTagName("Sequences").item(0);
        seqNodeList = seqs.getElementsByTagName("Sequence");


        for (int i = 0; i < seqNodeList.getLength(); i++) {
            Element seqNode = (Element) seqNodeList.item(i);
            String seqNo = seqNode.getAttribute("number");
            Element nameEl = (Element) seqNode.getElementsByTagName("Name").item(0);
            seqInfoList.add(new SeqInfo(nameEl.getTextContent(), seqNo));
        }

        System.out.printf("Found song '%s' with '%s' entries, total sequences '%s'%n", songName, songSeqIdxNodeList.getLength(), seqNodeList.getLength());

        return this;
    }

    private ReorderSeqs copyProject() {
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
            for (SeqInfo seqInfo : seqInfoList) {
                if (seqInfo.newIdx != null) {
                    File src = new File(outputProj, seqInfo.currentIdx + "." + SEQ_SUFFIX);
                    File dest = new File(outputProj, seqInfo.newIdx + "." + SEQ_SUFFIX + "tmp");
                    FileUtils.copyFile(src, dest);

                    Element seq = (Element) seqNodeList.item(Integer.parseInt(seqInfo.newIdx) - 1);
                    Element name = (Element) seq.getElementsByTagName("Name").item(0);
                    name.setTextContent(seqInfo.name);

                    for (Integer pos : seqInfo.posInSong) {
                        Element songSeqIdx = (Element) songSeqIdxNodeList.item(pos);
                        songSeqIdx.setTextContent(seqInfo.newIdx);
                    }
                    System.out.printf("Moved sequence '%s' from index '%s' to index '%s'%n", seqInfo.currentIdx, seqInfo.newIdx);
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
