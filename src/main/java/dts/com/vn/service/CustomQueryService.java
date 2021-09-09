package dts.com.vn.service;

import dts.com.vn.config.FileStorageConfig;
import dts.com.vn.entities.RenewData;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.properties.AppConfigProperties;
import dts.com.vn.repository.CustomQueryRepository;
import dts.com.vn.request.RenewDataRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.FileResponse;
import dts.com.vn.util.DateTimeUtil;
import dts.com.vn.util.WriteDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@PersistenceContext
public class CustomQueryService {

	@Autowired
	private CustomQueryRepository customQueryRepository;

	@Autowired
	private AppConfigProperties appConfigProperties;

	@Autowired
	private WriteDataUtils writeDataUtils;

	private final Path fileStorageLocation;

	public CustomQueryService(FileStorageConfig config) {
		this.fileStorageLocation = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RestApiException(new ApiResponse());
		}
	}

	public ApiResponse execute(RenewDataRequest renewDataRequest) throws IOException {
		ApiResponse response = null;
		List<RenewData> data = null;

		StringBuilder sql = new StringBuilder(renewDataRequest.getInputSQL());
		try {
			data = getData(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ApiResponse(ex, ErrorCode.MISSING_DATA_FIELD);
		}
		File path = new File(appConfigProperties.getAppConfigPath());
		if (path.listFiles().length > 0) {
			for (int i = 0; i < path.listFiles().length; i++) {
				if (path.listFiles()[i].getName().startsWith("CUSTOM_QUERY")) {
					// Do anything

					// READ OPTION
					InputStream is = new FileInputStream(path + "/" + path.listFiles()[i].getName());
					Properties prop = new Properties();
					prop.load(is);
					String outputFolder = String.valueOf(prop.getProperty("app.output_folder"));
					//Write FILE
					Calendar calendar = Calendar.getInstance();
					String folder = String.format(outputFolder);
					int day = calendar.get(Calendar.DATE);
					int month = calendar.get(Calendar.MONTH) + 1;
					int year = calendar.get(Calendar.YEAR);
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);
					int second = calendar.get(Calendar.SECOND);
					String strD = day < 10 ? ("0" + day) : (day + "");
					String strMonth = month < 10 ? ("0" + month) : (month + "");
					String strH = hour < 10 ? ("0" + hour) : (hour + "");
					String strM = minute < 10 ? ("0" + minute) : (minute + "");
					String strS = second < 10 ? ("0" + second) : (second + "");
					String pathFileOutput = folder + "CUSTOM_QUERY" + "-" + strD + strMonth + year + "-" + strH + strM + strS + ".txt";
					File file = new File(pathFileOutput);

					try {
						file.getParentFile().mkdirs();
						FileWriter writer = new FileWriter(file, true);
						writeDataUtils.writeDataToTXT(data, renewDataRequest.getInputTransactionCode(), writer);
						response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), file);
						writer.flush();
						writer.close();
					} catch (RestApiException ex) {
						file.delete();
						ex.printStackTrace();
						throw new RestApiException(ex);
					}
				}
			}
		}
		return response;
	}

	private List<RenewData> getData(String sql) {
		return this.customQueryRepository.selectRenewDate(sql);
	}

	/**
	 * Description - Save file to server
	 *
	 * @param file - as Multipartfile
	 * @return any
	 * @author - giangdh
	 * @created - 9/8/2021
	 */
	public String storeFileToServer(MultipartFile file) throws IOException {
		// Normalize file name
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		try {
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (IOException ex) {
			throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	/**
	 * Description - Lấy về toàn bộ file trong folder upload
	 *
	 * @param config - Đường dẫn folder
	 * @return any
	 * @author - giangdh
	 * @created - 9/9/2021
	 */
	public List<FileResponse> getAllFiles(FileStorageConfig config) throws IOException {
		List<FileResponse> result = new ArrayList<>();
		List<File> files = Files.list(Paths.get(config.getUploadDir()))
				.map(Path::toFile)
				.collect(Collectors.toList());
		for (File item : files) {
			FileResponse file = new FileResponse();
			file.setFileName(item.getName());
			SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DD_MM_YYYY_HH_mm_ss);
			String date = df.format(new Date(item.lastModified()));
			file.setUploadDate(date);
			result.add(file);
		}
		return result;
	}

}
