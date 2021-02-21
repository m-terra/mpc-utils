package org.mterra.mpc.seq;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mterra.mpc.MpcUtils;
import org.mterra.mpc.util.ProjectHelper;
import org.w3c.dom.Element;

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

public class Reorderer {

    public static void inPath(String scanDirPath, String targetDirPath) {
        File scanDir = new File(scanDirPath);
        File targetDir = new File(targetDirPath);
        for (File srcDir : scanDir.listFiles()) {
            System.out.printf("Checking %s%n", srcDir.getName());
            if (srcDir.getName().contains(MpcUtils.PROJECT_FOLDER_SUFFIX)) {
                String projectName = StringUtils.substringBefore(srcDir.getName(), MpcUtils.PROJECT_FOLDER_SUFFIX);
                System.out.printf("Found project '%s'%n", projectName);

                Reorderer inst = new Reorderer();
                ProjectInfo projectInfo = inst.loadInfos(srcDir);
                inst.calculateNewOrder(projectInfo);
                ProjectHelper.copyProject(srcDir, targetDir, projectName);
                inst.updateFiles(projectInfo, srcDir, targetDir, projectName);

            }
        }
    }

    private ProjectInfo loadInfos(File srcDir) {
        try {
            File allSeqs = new File(srcDir, MpcUtils.ALL_SEQS_FILE_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.document = db.parse(allSeqs);
            projectInfo.loadSeqsAndSong();
            return projectInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateNewOrder(ProjectInfo projectInfo) {
        boolean hasAir = projectInfo.seqInfoMap.values().stream()
                .anyMatch(seqInfo -> "air".equalsIgnoreCase(seqInfo.name));
        System.out.printf("Song has Air sequence '%s'%n", hasAir);
        for (SeqInfo seqInfo : projectInfo.seqInfoMap.values()) {
            if (hasAir) {
                seqInfo.newIdx = String.valueOf(seqInfo.posInSong.get(0));
            } else {
                seqInfo.newIdx = String.valueOf(seqInfo.posInSong.get(0) - 1);
            }
        }
        //todo newIdx close gaps
    }

    private void updateFiles(ProjectInfo projectInfo, File srcDir, File targetDir, String projectName) {
        File outputProj = new File(targetDir, projectName + MpcUtils.PROJECT_FOLDER_SUFFIX);
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = tf.newTransformer();

            File output = new File(outputProj, MpcUtils.ALL_SEQS_FILE_NAME);
            FileOutputStream outStream = new FileOutputStream(output);
            projectInfo.document.setXmlStandalone(true);
            transformer.transform(new DOMSource(projectInfo.document), new StreamResult(outStream));

            for (SeqInfo seqInfo : projectInfo.seqInfoMap.values()) {
                if (seqInfo.needsMoving()) {
                    File src = new File(outputProj, seqInfo.currentIdx + "." + MpcUtils.SEQ_SUFFIX);
                    File dest = new File(outputProj, seqInfo.newIdx + "." + MpcUtils.SEQ_SUFFIX + "tmp");
                    FileUtils.copyFile(src, dest);

                    Element seq = projectInfo.getSequenceNodeByNumber(seqInfo.newIdx);
                    Element name = (Element) seq.getElementsByTagName("Name").item(0);
                    name.setTextContent(seqInfo.name);

                    for (Integer pos : seqInfo.posInSong) {
                        Element songSeqIdx = (Element) projectInfo.songSeqIdxNodeList.item(pos);
                        songSeqIdx.setTextContent(seqInfo.newIdx);
                    }
                    System.out.printf("Moved sequence '%s' from index '%s' to index '%s'%n", seqInfo.name, seqInfo.currentIdx, seqInfo.newIdx);
                } else {
                    System.out.printf("Sequence '%s' keeps index '%s'%n", seqInfo.name, seqInfo.currentIdx);
                }
            }
            Collection<File> seqFiles = FileUtils.listFiles(srcDir, new String[]{MpcUtils.SEQ_SUFFIX + "tmp"}, false);
            for (File src : seqFiles) {
                Path fileToMovePath = Paths.get(src.getPath());
                Path targetPath = Paths.get(StringUtils.substringBefore(src.getPath(), "tmp"));
                Files.move(fileToMovePath, targetPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
