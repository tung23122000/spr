package dts.com.vn.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dts.com.vn.controller.ExternalSystemController;
import dts.com.vn.service.PackageNameService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class PackageNameServiceImpl implements PackageNameService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemController.class);

    @Override
    public String packagenameText() {
        String data = "";
        String resultJson = null;
        try {
            // Read file xml
            data = FileUtils.readFileToString(new File("D:\\PROJECT\\packageName.xml"), "UTF-8");

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(data.getBytes());
            resultJson = jsonNode.get("").asText().replace("(?i)(.* FOTEST.*", "")
                    .replace(")", "");
            System.out.println(resultJson);

        } catch (IOException e) {

            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    public String editPackageNameText(String s) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String path = "D:\\PROJECT\\packageName"+ timeStamp +".xml";
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"packageName\">(?i)(.* FOTEST.*\n";
        String lastFile = ")\n" +
                "<description/>\n" +
                "</localEntry>";
        try{
            FileUtils.write(new File("setting.xml"), header + s + lastFile, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "thành công ";
    }


}

